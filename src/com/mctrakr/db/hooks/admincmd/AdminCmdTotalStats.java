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

package com.mctrakr.db.hooks.admincmd;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.mctrakr.db.Query;
import com.mctrakr.db.data.NormalData;
import com.mctrakr.db.hooks.admincmd.Tables.AdminCmdTable;
import com.mctrakr.managers.HookManager;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock.HookType;

public class AdminCmdTotalStats extends NormalData {
    
    private String playerName;
    
    public AdminCmdTotalStats(OnlineSession session) {
        this.playerName = session.getName();
        fetchData(session.getId());
    }
    
    @Override
    public void fetchData(int playerId) {
        if(Query.table(AdminCmdTable.TableName)
                .condition(AdminCmdTable.PlayerId, playerId)
                .exists()) return;
        
        Player player = Bukkit.getPlayerExact(playerName);
        if(player == null) return;
        
        AdminCmdHook hook = (AdminCmdHook) HookManager.getHook(HookType.AdminCmd);
        if(hook == null) return;
        
        Query.table(AdminCmdTable.TableName)
            .value(AdminCmdTable.PlayerId, playerId)
            .value(AdminCmdTable.Afk, hook.isAfk(player))
            .value(AdminCmdTable.Vanished, hook.isInvisible(player))
            .value(AdminCmdTable.BanReason, hook.getBan(playerName))
            .insert();
    }

    @Override
    public boolean pushData(int playerId) {
        Player player = Bukkit.getPlayerExact(playerName);
        if(player == null) return false;
        
        AdminCmdHook hook = (AdminCmdHook) HookManager.getHook(HookType.AdminCmd);
        if(hook == null) return false;
        
        return Query.table(AdminCmdTable.TableName)
            .value(AdminCmdTable.Afk, hook.isAfk(player))
            .value(AdminCmdTable.Vanished, hook.isInvisible(player))
            .value(AdminCmdTable.BanReason, hook.getBan(playerName))
            .condition(AdminCmdTable.PlayerId, playerId)
            .update();
    }
    
}
