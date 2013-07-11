/*
 * PveTotalStats.java
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

package com.mctrakr.modules.stats.pve;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.mctrakr.cache.EntityCache;
import com.mctrakr.cache.MaterialCache;
import com.mctrakr.database.Query;
import com.mctrakr.database.Query.QueryResult;
import com.mctrakr.modules.DataStore.NormalData;
import com.mctrakr.modules.stats.pve.Tables.PveTotalsTable;
import com.mctrakr.session.OnlineSession;

/**
 * Represents an entry in the PVE data store.
 * It is dynamic, i.e. it can be edited once it has been created.
 * @author bitWolfy
 *
 */
public class PveTotalStats extends NormalData {
    
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
    public PveTotalStats(OnlineSession session, EntityType creatureType, ItemStack weapon) {
        super(session);
        this.creatureType = creatureType;
        this.weapon = weapon.clone();
        this.weapon.setAmount(1);
        playerDeaths = 0;
        creatureDeaths = 0;
        
        fetchData();
    }
    
    @Override
    public void fetchData() {
        QueryResult result = Query.table(PveTotalsTable.TableName)
                .column(PveTotalsTable.PlayerKilled)
                .column(PveTotalsTable.CreatureKilled)
                .condition(PveTotalsTable.PlayerId, session.getId())
                .condition(PveTotalsTable.CreatureId, EntityCache.parse(creatureType))
                .condition(PveTotalsTable.MaterialId, MaterialCache.parse(weapon))
                .select();
        if(result == null) {
            Query.table(PveTotalsTable.TableName)
                .value(PveTotalsTable.PlayerId, session.getId())
                .value(PveTotalsTable.CreatureId, EntityCache.parse(creatureType))
                .value(PveTotalsTable.MaterialId, MaterialCache.parse(weapon))
                .value(PveTotalsTable.PlayerKilled, playerDeaths)
                .value(PveTotalsTable.CreatureKilled, creatureDeaths)
                .insert();
        } else {
            playerDeaths = result.asInt(PveTotalsTable.PlayerKilled);
            creatureDeaths = result.asInt(PveTotalsTable.CreatureKilled);
        }
    }

    @Override
    public boolean pushData() {
        boolean result = Query.table(PveTotalsTable.TableName)
                .value(PveTotalsTable.PlayerKilled, playerDeaths)
                .value(PveTotalsTable.CreatureKilled, creatureDeaths)
                .condition(PveTotalsTable.PlayerId, session.getId())
                .condition(PveTotalsTable.CreatureId, EntityCache.parse(creatureType))
                .condition(PveTotalsTable.MaterialId, MaterialCache.parse(weapon))
                .update();
        return result;
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
