/*
 * VaultPlayerStats.java
 * 
 * Statistics
 * Copyright (C) 2013 bitWolfy <http://www.wolvencraft.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.mctrakr.modules.hooks.vault;

import com.mctrakr.database.Query;
import com.mctrakr.managers.HookManager;
import com.mctrakr.modules.DataStore.NormalData;
import com.mctrakr.modules.hooks.vault.Tables.VaultTable;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock.HookType;

public class VaultPlayerStats extends NormalData {
    
    public VaultPlayerStats(OnlineSession session) {
        super(session);
        fetchData();
    }
    
    @Override
    public void fetchData() {
        VaultHook hook = (VaultHook) HookManager.getHook(HookType.Vault);
        if(hook == null) return;
        
        if(Query.table(VaultTable.TableName)
                .condition(VaultTable.PlayerId, session.getId())
                .exists()) return;
        
        Query.table(VaultTable.TableName)
             .value(VaultTable.PlayerId, session.getId())
             .value(VaultTable.Balance, hook.getBalance(session.getName()))
             .value(VaultTable.GroupName, SerializableGroup.serialize(session.getName(), hook))
             .insert();
    }
    
    @Override
    public boolean pushData() {
        VaultHook hook = (VaultHook) HookManager.getHook(HookType.Vault);
        if(hook == null) return false;
        
        return Query.table(VaultTable.TableName)
            .value(VaultTable.Balance, hook.getBalance(session.getName()))
            .value(VaultTable.GroupName, SerializableGroup.serialize(session.getName(), hook))
            .condition(VaultTable.PlayerId, session.getId())
            .update();
    }
    
}
