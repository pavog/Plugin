/*
 * DetailedPVPEntry.java
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

package com.wolvencraft.yasp.db.data.pvp;

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.data.DetailedData;
import com.wolvencraft.yasp.db.tables.Detailed.PVPKills;
import com.wolvencraft.yasp.util.Util;
import com.wolvencraft.yasp.util.cache.MaterialCache;

/**
 * Represents an entry in the Detailed data store.
 * It is static, i.e. it cannot be edited once it has been created.
 * @author bitWolfy
 *
 */
@Getter(AccessLevel.PUBLIC)
public class DetailedPVPEntry extends DetailedData {
    
    private int victimId;
    private ItemStack weapon;
    private Location location;
    private long timestamp;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new DetailedPVPEntry based on the data provided
     * @param location
     * @param victimId
     * @param weapon
     */
    public DetailedPVPEntry(Location location, int victimId, ItemStack weapon) {
        this.victimId = victimId;
        this.weapon = weapon.clone();
        this.weapon.setAmount(1);
        this.location = location.clone();
        timestamp = Util.getTimestamp();
    }
    
    @Override
    public boolean pushData(int killerId) {
        return Query.table(PVPKills.TableName)
                .value(PVPKills.KillerId, killerId)
                .value(PVPKills.VictimId, victimId)
                .value(PVPKills.MaterialId, MaterialCache.parse(weapon))
                .value(PVPKills.World, location.getWorld().getName())
                .value(PVPKills.XCoord, location.getBlockX())
                .value(PVPKills.YCoord, location.getBlockY())
                .value(PVPKills.ZCoord, location.getBlockZ())
                .value(PVPKills.Timestamp, timestamp)
                .insert();
    }

}
