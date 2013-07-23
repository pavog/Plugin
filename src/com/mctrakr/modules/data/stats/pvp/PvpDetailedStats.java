/*
 * PvpDetailedStats.java
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

package com.mctrakr.modules.data.stats.pvp;

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.mctrakr.cache.MaterialCache;
import com.mctrakr.database.Query;
import com.mctrakr.modules.data.DetailedData;
import com.mctrakr.modules.data.stats.pvp.Tables.PvpDetailsTable;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.util.Util;

public class PvpDetailedStats {
    
    /**
     * An immutable PVP entry
     * @author bitWolfy
     *
     */
    @Getter(AccessLevel.PUBLIC)
    public static class PVPEntry extends DetailedData {
        
        private final int victimId;
        private final MaterialData weapon;
        private final Location location;
        private final long timestamp;
        
        public PVPEntry(OnlineSession session, Location location, int victimId, ItemStack weapon) {
            super(session);
            this.victimId = victimId;
            this.weapon = weapon.getData();
            this.location = location.clone();
            timestamp = Util.getTimestamp();
        }
        
        @Override
        public boolean pushData() {
            return Query.table(PvpDetailsTable.TableName)
                    .value(PvpDetailsTable.KillerId, session.getId())
                    .value(PvpDetailsTable.VictimId, victimId)
                    .value(PvpDetailsTable.MaterialId, MaterialCache.parse(weapon))
                    .value(PvpDetailsTable.World, location.getWorld().getName())
                    .value(PvpDetailsTable.XCoord, location.getBlockX())
                    .value(PvpDetailsTable.YCoord, location.getBlockY())
                    .value(PvpDetailsTable.ZCoord, location.getBlockZ())
                    .value(PvpDetailsTable.Timestamp, timestamp)
                    .insert();
        }
    }
}
