/*
 * TownyPlayerStats.java
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

package com.mctrakr.modules.data.hooks.towny;

import com.mctrakr.database.Query;
import com.mctrakr.managers.HookManager;
import com.mctrakr.modules.data.NormalData;
import com.mctrakr.modules.data.hooks.towny.Tables.TownyTable;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock.HookType;

public class TownyPlayerStats extends NormalData {
    
    public TownyPlayerStats(OnlineSession session) {
        super(session);
        fetchData();
    }
    
    @Override
    public void fetchData() {
        if(Query.table(TownyTable.TableName)
                .condition(TownyTable.PlayerId, session.getId())
                .exists()) return;
        
        TownyHook hook = (TownyHook) HookManager.getHook(HookType.Towny);
        if(hook == null) return;
        
        Query.table(TownyTable.TableName)
            .value(TownyTable.PlayerId, session.getId())
            .value(TownyTable.PlayerData, hook.getPlayerData(session.getName()))
            .insert();
    }

    @Override
    public boolean pushData() {
        TownyHook hook = (TownyHook) HookManager.getHook(HookType.Towny);
        if(hook == null) return false;
        
        return Query.table(TownyTable.TableName)
            .value(TownyTable.PlayerData, hook.getPlayerData(session.getName()))
            .condition(TownyTable.PlayerId, session.getId())
            .update();
    }
    
}
