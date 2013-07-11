/*
 * BanHammerPlayerEntry.java
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

package com.mctrakr.modules.hooks.banhammer;

import com.mctrakr.database.Query;
import com.mctrakr.managers.HookManager;
import com.mctrakr.modules.DataStore.NormalData;
import com.mctrakr.modules.hooks.banhammer.Tables.BanHammerTable;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock.HookType;

public class BanHammerTotalStats extends NormalData {
    
    public BanHammerTotalStats(OnlineSession session) {
        super(session);
        fetchData();
    }
    
    @Override
    public void fetchData() {
        if(Query.table(BanHammerTable.TableName)
                .condition(BanHammerTable.PlayerId, session.getId())
                .exists()) return;
        
        BanHammerHook hook = (BanHammerHook) HookManager.getHook(HookType.BanHammer);
        if(hook == null) return;
        
        Query.table(BanHammerTable.TableName)
            .value(BanHammerTable.PlayerId, session.getId())
            .value(BanHammerTable.Bans, hook.getBan(session.getName()))
            .insert();
    }

    @Override
    public boolean pushData() {
        BanHammerHook hook = (BanHammerHook) HookManager.getHook(HookType.BanHammer);
        if(hook == null) return false;
        
        return Query.table(BanHammerTable.TableName)
            .value(BanHammerTable.Bans, hook.getBan(session.getName()))
            .condition(BanHammerTable.PlayerId, session.getId())
            .update();
    }
    
}
