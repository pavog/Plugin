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

package com.mctrakr.modules.data.hooks.admincmd;

import org.bukkit.entity.Player;

import com.mctrakr.database.Query;
import com.mctrakr.managers.HookManager;
import com.mctrakr.modules.data.NormalData;
import com.mctrakr.modules.data.hooks.admincmd.Tables.AdminCmdTable;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock.HookType;

public class AdminCmdTotalStats extends NormalData {
    
    public AdminCmdTotalStats(OnlineSession session) {
        super(session);
        fetchData();
    }
    
    @Override
    public void fetchData() {
        if(Query.table(AdminCmdTable.TableName)
                .condition(AdminCmdTable.PlayerId, session.getId())
                .exists()) return;
        
        if(!session.isOnline()) return;
        Player player = session.getBukkitPlayer();
        
        AdminCmdHook hook = (AdminCmdHook) HookManager.getHook(HookType.AdminCmd);
        if(hook == null) return;
        
        Query.table(AdminCmdTable.TableName)
            .value(AdminCmdTable.PlayerId, session.getId())
            .value(AdminCmdTable.Afk, hook.isAfk(player))
            .value(AdminCmdTable.Vanished, hook.isInvisible(player))
            .value(AdminCmdTable.BanReason, hook.getBan(session.getName()))
            .insert();
    }

    @Override
    public boolean pushData() {
        if(!session.isOnline()) return false;
        Player player = session.getBukkitPlayer();
        
        AdminCmdHook hook = (AdminCmdHook) HookManager.getHook(HookType.AdminCmd);
        if(hook == null) return false;
        
        return Query.table(AdminCmdTable.TableName)
            .value(AdminCmdTable.Afk, hook.isAfk(player))
            .value(AdminCmdTable.Vanished, hook.isInvisible(player))
            .value(AdminCmdTable.BanReason, hook.getBan(session.getName()))
            .condition(AdminCmdTable.PlayerId, session.getId())
            .update();
    }
    
}
