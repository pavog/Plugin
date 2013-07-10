/*
 * MobArenaPlayerStats.java
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

package com.mctrakr.db.hooks.mobarena;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.mctrakr.db.Query;
import com.mctrakr.db.data.NormalData;
import com.mctrakr.db.hooks.mobarena.Tables.MobArenaTable;
import com.mctrakr.managers.HookManager;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock.HookType;

public class MobArenaPlayerStats extends NormalData {
    
    private String playerName;
    
    public MobArenaPlayerStats(OnlineSession session) {
        this.playerName = session.getName();
        fetchData(session.getId());
    }
    
    @Override
    public void fetchData(int playerId) {
        if(Query.table(MobArenaTable.TableName)
                .condition(MobArenaTable.PlayerId, playerId)
                .exists()) return;
        
        Player player = Bukkit.getPlayerExact(playerName);
        if(player == null) return;
        
        MobArenaHook hook = (MobArenaHook) HookManager.getHook(HookType.MobArena);
        if(hook == null) return;
        
        Query.table(MobArenaTable.TableName)
            .value(MobArenaTable.PlayerId, playerId)
            .value(MobArenaTable.IsPlaying, hook.isPlaying(player))
            .value(MobArenaTable.CurrentArena, hook.getArenaName(player))
            .insert();
    }

    @Override
    public boolean pushData(int playerId) {
        Player player = Bukkit.getPlayerExact(playerName);
        if(player == null) return false;
        
        MobArenaHook hook = (MobArenaHook) HookManager.getHook(HookType.MobArena);
        if(hook == null) return false;
        
        return Query.table(MobArenaTable.TableName)
            .value(MobArenaTable.IsPlaying, hook.isPlaying(player))
            .value(MobArenaTable.CurrentArena, hook.getArenaName(player))
            .condition(MobArenaTable.PlayerId, playerId)
            .update();
    }
    
}
