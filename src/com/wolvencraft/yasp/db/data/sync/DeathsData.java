/*
 * DeathsData.java
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

package com.wolvencraft.yasp.db.data.sync;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.wolvencraft.yasp.Settings;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.tables.Detailed.DeathPlayers;
import com.wolvencraft.yasp.db.tables.Normal.TotalDeathPlayersTable;
import com.wolvencraft.yasp.util.Util;

/**
 * Data collector that records all item statistics on the server for a specific player.
 * @author bitWolfy
 *
 */
public class DeathsData implements DataStore {
    
    private int playerId;
    private List<TotalDeathsEntry> normalData;
    private List<DetailedData> detailedData;

    /**
     * <b>Default constructor</b><br />
     * Creates an empty data store to save the statistics until database synchronization.
     */
    public DeathsData(int playerId) {
        this.playerId = playerId;
        normalData = new ArrayList<TotalDeathsEntry>();
        detailedData = new ArrayList<DetailedData>();
    }
    
    @Override
    public List<NormalData> getNormalData() {
        List<NormalData> temp = new ArrayList<NormalData>();
        for(NormalData value : normalData) temp.add(value);
        return temp;
    }
    
    @Override
    public List<DetailedData> getDetailedData() {
        List<DetailedData> temp = new ArrayList<DetailedData>();
        for(DetailedData value : detailedData) temp.add(value);
        return temp;
    }
    
    @Override
    public void sync() {
        for(NormalData entry : getNormalData()) {
            if(entry.pushData(playerId)) normalData.remove(entry);
        }
        
        for(DetailedData entry : getDetailedData()) {
            if(entry.pushData(playerId)) detailedData.remove(entry);
        }
    }
    
    @Override
    public void dump() {
        for(NormalData entry : getNormalData()) {
            normalData.remove(entry);
        }
        
        for(DetailedData entry : getDetailedData()) {
            detailedData.remove(entry);
        }
    }

    /**
     * Returns the specific entry from the data store.<br />
     * If the entry does not exist, it will be created.
     * @param cause
     * @return Corresponding entry
     */
    public TotalDeathsEntry getNormalData(DamageCause cause) {
        for(TotalDeathsEntry entry : normalData) {
            if(entry.equals(cause)) return entry;
        }
        TotalDeathsEntry entry = new TotalDeathsEntry(playerId, cause);
        normalData.add(entry);
        return entry;
    }
    
    /**
     * Registers the player death in the data store
     * @param location Location of the event
     * @param cause Death cause
     */
    public void playerDied(Location location, DamageCause cause) {
        getNormalData(cause).addTimes();
        detailedData.add(new DetailedDeathEntry(location, cause));
    }
    
    
    /**
     * Represents the total number of times a player died of a particular cause.<br />
     * Each entry must have a unique player and a unique death cause.
     * @author bitWolfy
     *
     */
    public class TotalDeathsEntry implements NormalData {
        
        private DamageCause cause;
        private int times;
        
        public TotalDeathsEntry(int playerId, DamageCause cause) {
            this.cause = cause;
            this.times = 0;
            
            fetchData(playerId);
        }
        
        @Override
        public void fetchData(int playerId) {
           QueryResult result = Query.table(TotalDeathPlayersTable.TableName.toString())
                    .column(TotalDeathPlayersTable.Times.toString())
                    .condition(TotalDeathPlayersTable.PlayerId.toString(), playerId)
                    .condition(TotalDeathPlayersTable.Cause.toString(), cause.name())
                    .select();
            
            if(result == null) {
                Query.table(TotalDeathPlayersTable.TableName.toString())
                    .value(TotalDeathPlayersTable.PlayerId.toString(), playerId)
                    .value(TotalDeathPlayersTable.Cause.toString(), cause.name())
                    .value(TotalDeathPlayersTable.Times.toString(), times)
                    .insert();
            } else {
                times = result.getValueAsInteger(TotalDeathPlayersTable.Times.toString());
            }
        }

        @Override
        public boolean pushData(int playerId) {
            boolean result = Query.table(TotalDeathPlayersTable.TableName.toString())
                    .value(TotalDeathPlayersTable.Times.toString(), times)
                    .condition(TotalDeathPlayersTable.PlayerId.toString(), playerId)
                    .condition(TotalDeathPlayersTable.Cause.toString(), cause.name())
                    .update();
            if(Settings.LocalConfiguration.Cloud.asBoolean()) fetchData(playerId);
            return result;
        }

        /**
         * Checks if the DamageCause corresponds to this entry 
         * @param cause DamageCause to check
         * @return b>true</b> if the data matches, <b>false</b> otherwise.
         */
        public boolean equals(DamageCause cause) {
            return this.cause.equals(cause);
        }
        
        /**
         * Returns the death cause
         * @return <b>DamageCause</b> death cause
         */
        public DamageCause getCause() {
            return cause;
        }
        
        /**
         * Increments the number of times a player died from the specified cause.
         */
        public void addTimes() {
            times++;
        }
        
    }
    
    
    /**
     * Represents an entry in the Detailed data store.
     * It is static, i.e. it cannot be edited once it has been created.
     * @author bitWolfy
     *
     */
    public class DetailedDeathEntry implements DetailedData {
        
        /**
         * <b>Default constructor</b><br />
         * Creates a new DetailedDeathPlayersEntry based on the data provided
         * @param location
         * @param deathCause
         */
        public DetailedDeathEntry(Location location, DamageCause deathCause) {
            this.deathCause = deathCause.name();
            this.location = location;
            this.timestamp = Util.getTimestamp();
        }
        
        private String deathCause;
        private Location location;
        private long timestamp;

        @Override
        public boolean pushData(int playerId) {
            return Query.table(DeathPlayers.TableName.toString())
                    .value(DeathPlayers.PlayerId.toString(), playerId)
                    .value(DeathPlayers.Cause.toString(), deathCause)
                    .value(DeathPlayers.World.toString(), location.getWorld().getName())
                    .value(DeathPlayers.XCoord.toString(), location.getBlockX())
                    .value(DeathPlayers.YCoord.toString(), location.getBlockY())
                    .value(DeathPlayers.ZCoord.toString(), location.getBlockZ())
                    .value(DeathPlayers.Timestamp.toString(), timestamp)
                    .insert();
        }

    }
    
}
