/*
 * TotalPVPEntry.java
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

import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.data.NormalData;
import com.wolvencraft.yasp.db.tables.Normal.TotalPVPKillsTable;
import com.wolvencraft.yasp.settings.RemoteConfiguration;
import com.wolvencraft.yasp.util.cache.MaterialCache;

/**
 * Represents an entry in the PVP data store.
 * It is dynamic, i.e. it can be edited once it has been created.
 * @author bitWolfy
 *
 */
public class TotalPVPEntry extends NormalData {
    
    private int victimId;
    private ItemStack weapon;
    private int times;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new TotalPVP object based on the killer and victim in question
     * @param playerId Player who killed the victim
     * @param victimId Player who was killed
     * @param weapon Weapon used
     */
    public TotalPVPEntry(int playerId, int victimId, ItemStack weapon) {
        this.victimId = victimId;
        this.weapon = weapon.clone();
        this.weapon.setAmount(1);
        times = 0;
        
        fetchData(playerId);
    }
    
    @Override
    public void fetchData(int killerId) {
        if(RemoteConfiguration.MergedDataTracking.asBoolean()) {
            clearData(killerId);
            return;
        }
        
        QueryResult result = Query.table(TotalPVPKillsTable.TableName)
                .column(TotalPVPKillsTable.Times)
                .condition(TotalPVPKillsTable.PlayerId, killerId)
                .condition(TotalPVPKillsTable.VictimId, victimId)
                .condition(TotalPVPKillsTable.MaterialId, MaterialCache.parse(weapon))
                .select();
        if(result == null) {
            Query.table(TotalPVPKillsTable.TableName)
                .value(TotalPVPKillsTable.PlayerId, killerId)
                .value(TotalPVPKillsTable.VictimId, victimId)
                .value(TotalPVPKillsTable.MaterialId, MaterialCache.parse(weapon))
                .value(TotalPVPKillsTable.Times, times)
                .insert();
        } else {
            times = result.asInt(TotalPVPKillsTable.Times);
        }
    }

    @Override
    public boolean pushData(int killerId) {
        boolean result = Query.table(TotalPVPKillsTable.TableName)
                .value(TotalPVPKillsTable.Times, times)
                .condition(TotalPVPKillsTable.PlayerId, killerId)
                .condition(TotalPVPKillsTable.VictimId, victimId)
                .condition(TotalPVPKillsTable.MaterialId, MaterialCache.parse(weapon))
                .update(RemoteConfiguration.MergedDataTracking.asBoolean());
        fetchData(killerId);
        return result;
    }
    
    @Override
    public void clearData(int playerId) {
        times = 0;
    }
    
    /**
     * Matches data provided in the arguments with the one in the entry.
     * @param victimId ID of the victim
     * @param weapon Weapon used in the PVP event
     * @return <b>true</b> if the data matches, <b>false</b> otherwise.
     */
    public boolean equals(int victimId, ItemStack weapon) {
        if(this.victimId != victimId) return false;
        ItemStack comparableWeapon = weapon.clone();
        comparableWeapon.setAmount(1);
        return comparableWeapon.equals(weapon);
        
    }
    
    /**
     * Increments the number of times the victim was killed
     */
    public void addTimes() {
        times++;
    }
    
}
