/*
 * AdminCmdTotalStats.java
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

package com.mctrakr.db.hooks.commandbook;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.mctrakr.db.Query;
import com.mctrakr.db.data.NormalData;
import com.mctrakr.db.data.DataStore.HookType;
import com.mctrakr.db.hooks.commandbook.Tables.CommandBookTable;
import com.mctrakr.managers.HookManager;

public class CommandBookTotalStats extends NormalData {
    
    private String playerName;
    
    public CommandBookTotalStats (Player player, int playerId) {
        this.playerName = player.getName();
        
        fetchData(playerId);
    }
    
    @Override
    public void fetchData(int playerId) {
        if(Query.table(CommandBookTable.TableName)
                .condition(CommandBookTable.PlayerId, playerId)
                .exists()) return;
        
        Player player = Bukkit.getPlayerExact(playerName);
        if(player == null) return;
        
        CommandBookHook hook = (CommandBookHook) HookManager.getHook(HookType.CommandBook);
        if(hook == null) return;
        
        Query.table(CommandBookTable.TableName)
            .value(CommandBookTable.PlayerId, playerId)
            .value(CommandBookTable.Afk, hook.isAFK(player))
            .value(CommandBookTable.God, hook.isGodMode(player))
            .insert();
    }

    @Override
    public boolean pushData(int playerId) {
        Player player = Bukkit.getPlayerExact(playerName);
        if(player == null) return false;
        
        CommandBookHook hook = (CommandBookHook) HookManager.getHook(HookType.CommandBook);
        if(hook == null) return false;
        
        return Query.table(CommandBookTable.TableName)
            .value(CommandBookTable.Afk, hook.isAFK(player))
            .value(CommandBookTable.God, hook.isGodMode(player))
            .condition(CommandBookTable.PlayerId, playerId)
            .update();
    }
    
}
