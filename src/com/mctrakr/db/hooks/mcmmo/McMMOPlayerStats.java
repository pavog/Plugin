/*
 * McMMOPlayerStats.java
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

package com.mctrakr.db.hooks.mcmmo;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.mctrakr.db.Query;
import com.mctrakr.db.data.NormalData;
import com.mctrakr.db.hooks.mcmmo.Tables.McMMOTable;
import com.mctrakr.managers.HookManager;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock.HookType;

public class McMMOPlayerStats extends NormalData {
    
    private String playerName;
    
    public McMMOPlayerStats(OnlineSession session) {
        this.playerName = session.getName();
        fetchData(session.getId());
    }
    
    @Override
    public void fetchData(int playerId) {
        if(Query.table(McMMOTable.TableName)
                .condition(McMMOTable.PlayerId, playerId)
                .exists()) return;
        
        Player player = Bukkit.getPlayerExact(playerName);
        if(player == null) return;
        
        McMMOHook hook = (McMMOHook) HookManager.getHook(HookType.McMMO);
        if(hook == null) return;
        
        Query.table(McMMOTable.TableName)
            .value(McMMOTable.PlayerId, playerId)
            .value(McMMOTable.Experience, hook.getExp(player))
            .value(McMMOTable.Levels, hook.getLevel(player))
            .value(McMMOTable.Party, hook.getParty(player))
            .insert();
    }

    @Override
    public boolean pushData(int playerId) {
        Player player = Bukkit.getPlayerExact(playerName);
        if(player == null) return false;
        
        McMMOHook hook = (McMMOHook) HookManager.getHook(HookType.McMMO);
        if(hook == null) return false;
        
        return Query.table(McMMOTable.TableName)
            .value(McMMOTable.Experience, hook.getExp(player))
            .value(McMMOTable.Levels, hook.getLevel(player))
            .value(McMMOTable.Party, hook.getParty(player))
            .condition(McMMOTable.PlayerId, playerId)
            .update();
    }
    
}
