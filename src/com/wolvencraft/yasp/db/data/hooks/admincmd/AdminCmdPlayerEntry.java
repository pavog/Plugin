/*
 * AdminCmdPlayerEntry.java
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

package com.wolvencraft.yasp.db.data.hooks.admincmd;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.data.NormalData;
import com.wolvencraft.yasp.db.tables.Hook.AdminCmdTable;
import com.wolvencraft.yasp.util.hooks.AdminCmdHook;

public class AdminCmdPlayerEntry extends NormalData {
    
    private String playerName;
    
    public AdminCmdPlayerEntry (Player player, int playerId) {
        this.playerName = player.getName();
        
        fetchData(playerId);
    }
    
    @Override
    public void fetchData(int playerId) {
        if(Query.table(AdminCmdTable.TableName)
                .condition(AdminCmdTable.PlayerId, playerId)
                .exists()) return;
        
        Player player = Bukkit.getPlayerExact(playerName);
        if(player == null) return;
        
        Query.table(AdminCmdTable.TableName)
            .value(AdminCmdTable.PlayerId, playerId)
            .value(AdminCmdTable.Afk, AdminCmdHook.isAfk(player))
            .value(AdminCmdTable.Vanished, AdminCmdHook.isInvisible(player))
            .value(AdminCmdTable.BanReason, AdminCmdHook.getBan(playerName))
            .insert();
    }

    @Override
    public boolean pushData(int playerId) {
        Player player = Bukkit.getPlayerExact(playerName);
        if(player == null) return false;
        
        return Query.table(AdminCmdTable.TableName)
            .value(AdminCmdTable.Afk, AdminCmdHook.isAfk(player))
            .value(AdminCmdTable.Vanished, AdminCmdHook.isInvisible(player))
            .value(AdminCmdTable.BanReason, AdminCmdHook.getBan(playerName))
            .condition(AdminCmdTable.PlayerId, playerId)
            .update();
    }

    @Override
    @Deprecated
    public void clearData(int playerId) { }
    
}
