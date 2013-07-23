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

package com.mctrakr.modules.data.hooks.worldguard;

import org.bukkit.entity.Player;

import com.mctrakr.database.Query;
import com.mctrakr.managers.HookManager;
import com.mctrakr.modules.data.NormalData;
import com.mctrakr.modules.data.hooks.worldguard.Tables.WorldGuardTable;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock.HookType;

/**
 * Tracks the information about player's current position in the region and its flags
 * @author bitWolfy
 *
 */
public class WorldGuardPlayerStats extends NormalData {
    
    public WorldGuardPlayerStats(OnlineSession session) {
        super(session);
        fetchData();
    }
    
    @Override
    public void fetchData() {
        if(Query.table(WorldGuardTable.TableName)
                .condition(WorldGuardTable.PlayerId, session.getId())
                .exists()) return;
        
        Query.table(WorldGuardTable.TableName)
             .value(WorldGuardTable.PlayerId, session.getId())
             .value(WorldGuardTable.RegionName, "")
             .value(WorldGuardTable.RegionFlags, "")
             .insert();
    }

    @Override
    public boolean pushData() {
        if(!session.isOnline()) return false;
        Player player = session.getBukkitPlayer();
        
        WorldGuardHook hook = (WorldGuardHook) HookManager.getHook(HookType.WorldGuard);
        if(hook == null) return false;
        
        return Query.table(WorldGuardTable.TableName)
             .value(WorldGuardTable.RegionName, hook.getRegions(player.getLocation()))
             .value(WorldGuardTable.RegionFlags, hook.getFlags(player.getLocation()))
             .condition(WorldGuardTable.PlayerId, session.getId())
             .update();
    }
    
}
