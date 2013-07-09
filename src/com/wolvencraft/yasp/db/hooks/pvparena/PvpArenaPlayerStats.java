/*
 * PvpArenaPlayerStats.java
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

package com.wolvencraft.yasp.db.hooks.pvparena;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.data.NormalData;
import com.wolvencraft.yasp.db.data.DataStore.HookType;
import com.wolvencraft.yasp.db.hooks.pvparena.Tables.PvpArenaTable;
import com.wolvencraft.yasp.managers.HookManager;

public class PvpArenaPlayerStats extends NormalData {
    
    private String playerName;
    
    public PvpArenaPlayerStats (Player player, int playerId) {
        this.playerName = player.getName();
        
        fetchData(playerId);
    }
    
    @Override
    public void fetchData(int playerId) {
        if(Query.table(PvpArenaTable.TableName)
                .condition(PvpArenaTable.PlayerId, playerId)
                .exists()) return;
        
        Player player = Bukkit.getPlayerExact(playerName);
        if(player == null) return;
        
        PvpArenaHook hook = (PvpArenaHook) HookManager.getHook(HookType.PvpArena);
        if(hook == null) return;
        
        Query.table(PvpArenaTable.TableName)
            .value(PvpArenaTable.PlayerId, playerId)
            .value(PvpArenaTable.IsPlaying, hook.isPlaying(player))
            .value(PvpArenaTable.CurrentArena, hook.getArenaName(player))
            .insert();
    }

    @Override
    public boolean pushData(int playerId) {
        Player player = Bukkit.getPlayerExact(playerName);
        if(player == null) return false;
        
        PvpArenaHook hook = (PvpArenaHook) HookManager.getHook(HookType.PvpArena);
        if(hook == null) return false;
        
        return Query.table(PvpArenaTable.TableName)
            .value(PvpArenaTable.IsPlaying, hook.isPlaying(player))
            .value(PvpArenaTable.CurrentArena, hook.getArenaName(player))
            .condition(PvpArenaTable.PlayerId, playerId)
            .update();
    }
    
}
