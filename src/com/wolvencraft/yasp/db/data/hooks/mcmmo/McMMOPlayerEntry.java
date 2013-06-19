/*
 * McMMOPlayerEntry.java
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

package com.wolvencraft.yasp.db.data.hooks.mcmmo;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.data.NormalData;
import com.wolvencraft.yasp.db.tables.Hook.McMMOTable;
import com.wolvencraft.yasp.util.hooks.McMMOHook;

public class McMMOPlayerEntry extends NormalData {
    
    private String playerName;
    
    public McMMOPlayerEntry (Player player, int playerId) {
        this.playerName = player.getName();
        
        fetchData(playerId);
    }
    
    @Override
    public void fetchData(int playerId) {
        if(Query.table(McMMOTable.TableName)
                .condition(McMMOTable.PlayerId, playerId)
                .exists()) return;
        
        Player player = Bukkit.getPlayerExact(playerName);
        if(player == null) return;
        
        Query.table(McMMOTable.TableName)
            .value(McMMOTable.PlayerId, playerId)
            .value(McMMOTable.Experience, McMMOHook.getExp(player))
            .value(McMMOTable.Levels, McMMOHook.getLevel(player))
            .value(McMMOTable.Party, McMMOHook.getParty(player))
            .insert();
    }

    @Override
    public boolean pushData(int playerId) {
        Player player = Bukkit.getPlayerExact(playerName);
        if(player == null) return false;
        
        return Query.table(McMMOTable.TableName)
            .value(McMMOTable.Experience, McMMOHook.getExp(player))
            .value(McMMOTable.Levels, McMMOHook.getLevel(player))
            .value(McMMOTable.Party, McMMOHook.getParty(player))
            .condition(McMMOTable.PlayerId, playerId)
            .update();
    }

    @Override
    @Deprecated
    public void clearData(int playerId) { }
    
}
