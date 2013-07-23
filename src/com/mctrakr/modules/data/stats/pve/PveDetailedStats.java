/*
 * PveDetailedStats.java
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

package com.mctrakr.modules.data.stats.pve;

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.mctrakr.cache.EntityCache;
import com.mctrakr.cache.MaterialCache;
import com.mctrakr.database.Query;
import com.mctrakr.modules.data.DetailedData;
import com.mctrakr.modules.data.stats.pve.Tables.PveDetailsTable;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.util.Util;

public class PveDetailedStats {
    
    /**
     * An immutable PVE entry
     * @author bitWolfy
     *
     */
    @Getter(AccessLevel.PUBLIC)
    public static class PVEEntry extends DetailedData {
        
        private final EntityType creatureType;
        private final MaterialData weapon;
        private final Location location;
        private final boolean playerKilled;
        private final long timestamp;
        
        public PVEEntry (OnlineSession session, EntityType creatureType, Location location, ItemStack weapon) {
            super(session);
            this.creatureType = creatureType;
            this.weapon = weapon.getData();
            this.location = location.clone();
            playerKilled = false;
            timestamp = Util.getTimestamp();
        }
        
        /**
         * <b>Creature killed a player</b><br />
         * Creates a new DetailedPVEEntry where the creature killed a player.
         * @param creatureType Type of the creature
         * @param location Location of the event
         */
        public PVEEntry (OnlineSession session, EntityType creatureType, Location location) {
            super(session);
            this.creatureType = creatureType;
            this.weapon = new MaterialData(Material.AIR);
            this.location = location.clone();
            playerKilled = true;
            timestamp = Util.getTimestamp();
        }
        
        @Override
        public boolean pushData() {
            return Query.table(PveDetailsTable.TableName)
                    .value(PveDetailsTable.PlayerId, session.getId())
                    .value(PveDetailsTable.CreatureId, EntityCache.parse(creatureType))
                    .value(PveDetailsTable.PlayerKilled, playerKilled)
                    .value(PveDetailsTable.MaterialId, MaterialCache.parse(weapon))
                    .value(PveDetailsTable.World, location.getWorld().getName())
                    .value(PveDetailsTable.XCoord, location.getBlockX())
                    .value(PveDetailsTable.YCoord, location.getBlockY())
                    .value(PveDetailsTable.ZCoord, location.getBlockZ())
                    .value(PveDetailsTable.Timestamp, timestamp)
                    .insert();
        }
    }
}
