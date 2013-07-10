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

package com.mctrakr.db.hooks.banhammer;

import com.mctrakr.db.Query;
import com.mctrakr.db.data.DataStore.HookType;
import com.mctrakr.db.data.NormalData;
import com.mctrakr.db.hooks.banhammer.Tables.BanHammerTable;
import com.mctrakr.managers.HookManager;
import com.mctrakr.session.OnlineSession;

public class BanHammerTotalStats extends NormalData {
    
    private String playerName;
    
    public BanHammerTotalStats(OnlineSession session) {
        this.playerName = session.getName();
        fetchData(session.getId());
    }
    
    @Override
    public void fetchData(int playerId) {
        if(Query.table(BanHammerTable.TableName)
                .condition(BanHammerTable.PlayerId, playerId)
                .exists()) return;
        
        BanHammerHook hook = (BanHammerHook) HookManager.getHook(HookType.BanHammer);
        if(hook == null) return;
        
        Query.table(BanHammerTable.TableName)
            .value(BanHammerTable.PlayerId, playerId)
            .value(BanHammerTable.Bans, hook.getBan(playerName))
            .insert();
    }

    @Override
    public boolean pushData(int playerId) {
        BanHammerHook hook = (BanHammerHook) HookManager.getHook(HookType.BanHammer);
        if(hook == null) return false;
        
        return Query.table(BanHammerTable.TableName)
            .value(BanHammerTable.Bans, hook.getBan(playerName))
            .condition(BanHammerTable.PlayerId, playerId)
            .update();
    }
    
}
