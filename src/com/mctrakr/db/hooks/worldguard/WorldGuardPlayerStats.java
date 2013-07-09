/*
 * WorldGuardPlayerStats.java
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

package com.mctrakr.db.hooks.worldguard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.mctrakr.db.Query;
import com.mctrakr.db.data.NormalData;
import com.mctrakr.db.data.DataStore.HookType;
import com.mctrakr.db.hooks.worldguard.Tables.WorldGuardTable;
import com.mctrakr.managers.HookManager;

/**
 * Tracks the information about player's current position in the region and its flags
 * @author bitWolfy
 *
 */
public class WorldGuardPlayerStats extends NormalData {
    
    private String playerName;
    
    public WorldGuardPlayerStats(int playerId, Player player) {
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
        
        WorldGuardHook hook = (WorldGuardHook) HookManager.getHook(HookType.WorldGuard);
        if(hook == null) return false;
        
        return Query.table(WorldGuardTable.TableName)
             .value(WorldGuardTable.RegionName, hook.getRegions(player.getLocation()))
             .value(WorldGuardTable.RegionFlags, hook.getFlags(player.getLocation()))
             .condition(WorldGuardTable.PlayerId, playerId)
             .update();
    }
    
}
