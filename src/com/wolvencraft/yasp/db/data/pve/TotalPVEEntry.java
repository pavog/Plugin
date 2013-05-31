/*
 * TotalPVEEntry.java
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

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.data.NormalData;
import com.wolvencraft.yasp.db.tables.Normal.TotalPVEKillsTable;
import com.wolvencraft.yasp.settings.LocalConfiguration;
import com.wolvencraft.yasp.util.cache.EntityCache;
import com.wolvencraft.yasp.util.cache.MaterialCache;

/**
 * Represents an entry in the PVE data store.
 * It is dynamic, i.e. it can be edited once it has been created.
 * @author bitWolfy
 *
 */
public class TotalPVEEntry extends NormalData {
    
    private EntityType creatureType;
    private ItemStack weapon;
    private int playerDeaths;
    private int creatureDeaths;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new TotalPVE object based on the player and creature in question
     * @param playerId Player in question
     * @param creatureType Creature in question
     * @param weapon Weapon used
     */
    public TotalPVEEntry(int playerId, EntityType creatureType, ItemStack weapon) {
        this.creatureType = creatureType;
        this.weapon = weapon.clone();
        this.weapon.setAmount(1);
        playerDeaths = 0;
        creatureDeaths = 0;
        
        fetchData(playerId);
    }
    
    @Override
    public void fetchData(int playerId) {
        if(!LocalConfiguration.Standalone.asBoolean()) {
            clearData(playerId);
            return;
        }
        
        QueryResult result = Query.table(TotalPVEKillsTable.TableName)
                .column(TotalPVEKillsTable.PlayerKilled)
                .column(TotalPVEKillsTable.CreatureKilled)
                .condition(TotalPVEKillsTable.PlayerId, playerId)
                .condition(TotalPVEKillsTable.CreatureId, EntityCache.parse(creatureType))
                .condition(TotalPVEKillsTable.MaterialId, MaterialCache.parse(weapon))
                .select();
        if(result == null) {
            Query.table(TotalPVEKillsTable.TableName)
                .value(TotalPVEKillsTable.PlayerId, playerId)
                .value(TotalPVEKillsTable.CreatureId, EntityCache.parse(creatureType))
                .value(TotalPVEKillsTable.MaterialId, MaterialCache.parse(weapon))
                .value(TotalPVEKillsTable.PlayerKilled, playerDeaths)
                .value(TotalPVEKillsTable.CreatureKilled, creatureDeaths)
                .insert();
        } else {
            playerDeaths = result.asInt(TotalPVEKillsTable.PlayerKilled);
            creatureDeaths = result.asInt(TotalPVEKillsTable.CreatureKilled);
        }
    }

    @Override
    public boolean pushData(int playerId) {
        boolean result = Query.table(TotalPVEKillsTable.TableName)
                .value(TotalPVEKillsTable.PlayerKilled, playerDeaths)
                .value(TotalPVEKillsTable.CreatureKilled, creatureDeaths)
                .condition(TotalPVEKillsTable.PlayerId, playerId)
                .condition(TotalPVEKillsTable.CreatureId, EntityCache.parse(creatureType))
                .condition(TotalPVEKillsTable.MaterialId, MaterialCache.parse(weapon))
                .update(LocalConfiguration.Standalone.asBoolean());
        fetchData(playerId);
        return result;
    }
    
    @Override
    public void clearData(int playerId) {
        playerDeaths = 0;
        creatureDeaths = 0;
    }

    /**
     * Matches data provided in the arguments with the one in the entry.
     * @param creatureType Type of the creature
     * @param weapon Weapon used in the event
     * @return <b>true</b> if the data matches, <b>false</b> otherwise.
     */
    public boolean equals(EntityType creatureType, ItemStack weapon) {
        if(!this.creatureType.equals(creatureType)) return false;
        ItemStack comparableWeapon = weapon.clone();
        comparableWeapon.setAmount(1);
        return comparableWeapon.equals(this.weapon);
    }
    
    /**
     * Increments the number of times the player has died
     */
    public void addPlayerDeaths() {
        playerDeaths++;
    }
    
    /**
     * Increments the number of times the creature has died
     */
    public void addCreatureDeaths() {
        creatureDeaths++;
    }
}
