/*
 * VanishPlayerStats.java
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

package com.mctrakr.modules.hooks.vanish;

import org.bukkit.entity.Player;

import com.mctrakr.database.Query;
import com.mctrakr.managers.HookManager;
import com.mctrakr.modules.DataStore.NormalData;
import com.mctrakr.modules.hooks.vanish.Tables.VanishTable;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock.HookType;

public class VanishPlayerStats extends NormalData {
    
    public VanishPlayerStats(OnlineSession session) {
        super(session);
        fetchData();
    }
    
    @Override
    public void fetchData() {
        if(Query.table(VanishTable.TableName)
                .condition(VanishTable.PlayerId, session.getId())
                .exists()) return;
        
        if(!session.isOnline()) return;
        Player player = session.getBukkitPlayer();
        
        VanishHook hook = (VanishHook) HookManager.getHook(HookType.Vanish);
        if(hook == null) return;
        
        Query.table(VanishTable.TableName)
            .value(VanishTable.PlayerId, session.getId())
            .value(VanishTable.IsVanished, hook.isVanished(player))
            .insert();
    }

    @Override
    public boolean pushData() {
        if(!session.isOnline()) return false;
        Player player = session.getBukkitPlayer();
        
        VanishHook hook = (VanishHook) HookManager.getHook(HookType.Vanish);
        if(hook == null) return false;
        
        return Query.table(VanishTable.TableName)
            .value(VanishTable.IsVanished, hook.isVanished(player))
            .condition(VanishTable.PlayerId, session.getId())
            .update();
    }
    
}
