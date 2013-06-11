/*
 * DetailedPVEEntry.java
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

package com.wolvencraft.yasp.db.data.pve;

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.data.DetailedData;
import com.wolvencraft.yasp.db.tables.Detailed.PVEKills;
import com.wolvencraft.yasp.util.Util;
import com.wolvencraft.yasp.util.cache.EntityCache;
import com.wolvencraft.yasp.util.cache.MaterialCache;

/**
 * Represents an entry in the Detailed data store.
 * It is static, i.e. it cannot be edited once it has been created.
 * @author bitWolfy
 *
 */
@Getter(AccessLevel.PUBLIC)
public class DetailedPVEEntry extends DetailedData {
    
    private EntityType creatureType;
    private ItemStack weapon;
    private Location location;
    private boolean playerKilled;
    private long timestamp;
    
    /**
     * <b>Player killed a creature</b><br />
     * Creates a new DetailedPVEEntry where the player killed a creature.
     * @param creatureType Type of the creature
     * @param location Location of the event
     * @param weapon Weapon used
     */
    public DetailedPVEEntry (EntityType creatureType, Location location, ItemStack weapon) {
        this.creatureType = creatureType;
        this.weapon = weapon.clone();
        this.weapon.setAmount(1);
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
    public DetailedPVEEntry (EntityType creatureType, Location location) {
        this.creatureType = creatureType;
        weapon = new ItemStack(Material.AIR, 1);
        this.location = location.clone();
        playerKilled = true;
        timestamp = Util.getTimestamp();
    }
    
    @Override
    public boolean pushData(int playerId) {
        return Query.table(PVEKills.TableName)
                .value(PVEKills.PlayerId, playerId)
                .value(PVEKills.CreatureId, EntityCache.parse(creatureType))
                .value(PVEKills.PlayerKilled, playerKilled)
                .value(PVEKills.MaterialId, MaterialCache.parse(weapon))
                .value(PVEKills.World, location.getWorld().getName())
                .value(PVEKills.XCoord, location.getBlockX())
                .value(PVEKills.YCoord, location.getBlockY())
                .value(PVEKills.ZCoord, location.getBlockZ())
                .value(PVEKills.Timestamp, timestamp)
                .insert();
    }

}
