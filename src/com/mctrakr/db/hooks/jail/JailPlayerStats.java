/*
 * JailPlayerStats.java
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

package com.mctrakr.db.hooks.jail;

import org.bukkit.entity.Player;

import com.mctrakr.db.Query;
import com.mctrakr.db.data.NormalData;
import com.mctrakr.db.data.DataStore.HookType;
import com.mctrakr.db.hooks.jail.Tables.JailTable;
import com.mctrakr.managers.HookManager;

public class JailPlayerStats extends NormalData {
    
    private String playerName;
    
    public JailPlayerStats (Player player, int playerId) {
        this.playerName = player.getName();
        
        fetchData(playerId);
    }
    
    @Override
    public void fetchData(int playerId) {
        if(Query.table(JailTable.TableName)
                .condition(JailTable.PlayerId, playerId)
                .exists()) return;
        
        JailHook hook = (JailHook) HookManager.getHook(HookType.Jail);
        if(hook == null) return;
        
        Query.table(JailTable.TableName)
            .value(JailTable.PlayerId, playerId)
            .value(JailTable.IsJailed, hook.isJailed(playerName))
            .value(JailTable.Jailer, hook.getJailer(playerName))
            .value(JailTable.RemainingTime, hook.getRemainingTime(playerName))
            .insert();
    }

    @Override
    public boolean pushData(int playerId) {
        JailHook hook = (JailHook) HookManager.getHook(HookType.Jail);
        if(hook == null) return false;
        
        return Query.table(JailTable.TableName)
            .value(JailTable.IsJailed, hook.isJailed(playerName))
            .value(JailTable.Jailer, hook.getJailer(playerName))
            .value(JailTable.RemainingTime, hook.getRemainingTime(playerName))
            .condition(JailTable.PlayerId, playerId)
            .update();
    }
    
}
