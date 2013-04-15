/*
 * PVEData.java
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
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.Settings;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.tables.Detailed.PVEKills;
import com.wolvencraft.yasp.db.tables.Normal.TotalPVEKillsTable;
import com.wolvencraft.yasp.util.Util;
import com.wolvencraft.yasp.util.cache.EntityCache;
import com.wolvencraft.yasp.util.cache.MaterialCache;

/**
 * Data collector that records all PVE statistics on the server for a specific player.
 * @author bitWolfy
 *
 */
public class PVEData implements DataStore{

    private int playerId;
    private List<TotalPVEEntry> normalData;
    private List<DetailedData> detailedData;
    
    /**
     * <b>Default constructor</b><br />
     * Creates an empty data store to save the statistics until database synchronization.
     */
    public PVEData(int playerId) {
        this.playerId = playerId;
        normalData = new ArrayList<TotalPVEEntry>();
        detailedData = new ArrayList<DetailedData>();
    }
    
    @Override
    public DataStoreType getType() {
        return DataStoreType.PVE;
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
     * Returns a specific entry from the data store.<br />
     * If an entry does not exist, it will be created.
     * @param type Entity type of the creature
     * @param weapon Weapon used in the event
     * @return Corresponding entry
     */
    public TotalPVEEntry getNormalData(EntityType type, ItemStack weapon) {
        for(TotalPVEEntry entry : normalData) {
            if(entry.equals(type, weapon)) return entry;
        }
        TotalPVEEntry entry = new TotalPVEEntry(playerId, type, weapon);
        normalData.add(entry);
        return entry;
    }
    
    /**
     * Registers the creature death in the data store
     * @param victim Creature killed
     * @param weapon Weapon used by killer
     */
    public void playerKilledCreature(Entity victim, ItemStack weapon) {
        getNormalData(victim.getType(), weapon).addCreatureDeaths();
        detailedData.add(new DetailedPVEEntry(victim.getType(), victim.getLocation(), weapon));
    }
    
    /**
     * Registers the player death in the data store
     * @param killer Creature that killed the player
     * @param weapon Weapon used by killer
     */
    public void creatureKilledPlayer(Entity killer, ItemStack weapon) {
        getNormalData(killer.getType(), weapon).addPlayerDeaths();
        detailedData.add(new DetailedPVEEntry(killer.getType(), killer.getLocation()));
    }
    
    
    /**
     * Represents an entry in the PVE data store.
     * It is dynamic, i.e. it can be edited once it has been created.
     * @author bitWolfy
     *
     */
    public class TotalPVEEntry implements NormalData {
        
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
            this.weapon = weapon;
            this.weapon.setAmount(1);
            
            this.playerDeaths = 0;
            this.creatureDeaths = 0;
            
            fetchData(playerId);
        }
        
        @Override
        public void fetchData(int playerId) {
            QueryResult result = Query.table(TotalPVEKillsTable.TableName)
                    .column(TotalPVEKillsTable.PlayerKilled)
                    .column(TotalPVEKillsTable.CreatureKilled)
                    .condition(TotalPVEKillsTable.PlayerId, playerId)
                    .condition(TotalPVEKillsTable.CreatureId, EntityCache.parse(creatureType))
                    .condition(TotalPVEKillsTable.Material, MaterialCache.parse(weapon))
                    .select();
            if(result == null) {
                Query.table(TotalPVEKillsTable.TableName)
                    .value(TotalPVEKillsTable.PlayerId, playerId)
                    .value(TotalPVEKillsTable.CreatureId, EntityCache.parse(creatureType))
                    .value(TotalPVEKillsTable.Material, MaterialCache.parse(weapon))
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
                    .condition(TotalPVEKillsTable.Material, MaterialCache.parse(weapon))
                    .update();
            if(Settings.LocalConfiguration.Cloud.asBoolean()) fetchData(playerId);
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
    
    /**
     * Represents an entry in the Detailed data store.
     * It is static, i.e. it cannot be edited once it has been created.
     * @author bitWolfy
     *
     */
    public class DetailedPVEEntry implements DetailedData {
        
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
            this.weapon = weapon;
            this.weapon.setAmount(1);
            this.location = location;
            this.playerKilled = false;
            this.timestamp = Util.getTimestamp();
        }
        
        /**
         * <b>Creature killed a player</b><br />
         * Creates a new DetailedPVEEntry where the creature killed a player.
         * @param creatureType Type of the creature
         * @param location Location of the event
         */
        public DetailedPVEEntry (EntityType creatureType, Location location) {
            this.creatureType = creatureType;
            this.weapon = new ItemStack(Material.AIR, 1);
            this.location = location;
            this.playerKilled = true;
            this.timestamp = Util.getTimestamp();
        }
        
        @Override
        public boolean pushData(int playerId) {
            return Query.table(PVEKills.TableName)
                    .value(PVEKills.PlayerId, playerId)
                    .value(PVEKills.CreatureId, EntityCache.parse(creatureType))
                    .value(PVEKills.PlayerKilled, playerKilled)
                    .value(PVEKills.Material, MaterialCache.parse(weapon))
                    .value(PVEKills.World, location.getWorld().getName())
                    .value(PVEKills.XCoord, location.getBlockX())
                    .value(PVEKills.YCoord, location.getBlockY())
                    .value(PVEKills.ZCoord, location.getBlockZ())
                    .value(PVEKills.Timestamp, timestamp)
                    .insert();
        }

    }
    
}
