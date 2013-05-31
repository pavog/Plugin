/*
 * WorldGuardPlayerEntry.java
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

package com.wolvencraft.yasp.db.data.hooks.worldguard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.data.NormalData;
import com.wolvencraft.yasp.db.tables.Hook.WorldGuardTable;
import com.wolvencraft.yasp.util.hooks.WorldGuardHook;

/**
 * Tracks the information about player's current position in the region and its flags
 * @author bitWolfy
 *
 */
public class WorldGuardPlayerEntry extends NormalData {
    
    private String playerName;
    
    public WorldGuardPlayerEntry(int playerId, Player player) {
        playerName = player.getName();
        
        fetchData(playerId);
    }
    
    @Override
    public void fetchData(int playerId) {
        if(Query.table(WorldGuardTable.TableName)
                .condition(WorldGuardTable.PlayerId, playerId)
                .exists()) return;
        
        Query.table(WorldGuardTable.TableName)
             .value(WorldGuardTable.PlayerId, playerId)
             .value(WorldGuardTable.RegionName, "")
             .value(WorldGuardTable.RegionFlags, "")
             .insert();
    }

    @Override
    public boolean pushData(int playerId) {
        Player player = Bukkit.getServer().getPlayerExact(playerName);
        if(player == null) return false;
        
        return Query.table(WorldGuardTable.TableName)
             .value(WorldGuardTable.RegionName, WorldGuardHook.getRegions(player.getLocation()))
             .value(WorldGuardTable.RegionFlags, WorldGuardHook.getFlags(player.getLocation()))
             .condition(WorldGuardTable.PlayerId, playerId)
             .update();
    }

    @Override
    @Deprecated
    public void clearData(int playerId) { }
    
    
    
}
