/*
 * BlocksData.java
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

import com.wolvencraft.yasp.Settings;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.tables.Detailed.PlacedBlocks;
import com.wolvencraft.yasp.db.tables.Detailed.DestroyedBlocks;
import com.wolvencraft.yasp.db.tables.Normal.TotalBlocksTable;
import com.wolvencraft.yasp.util.Util;

/**
 * Data store that records all block interactions on the server.
 * @author bitWolfy
 *
 */
public class BlocksData implements DataStore {
    
    private int playerId;
    private List<TotalBlocksEntry> normalData;
    private List<DetailedData> detailedData;
    
    /**
     * <b>Default constructor</b><br />
     * Creates an empty data store to save the statistics until database synchronization.
     */
    public BlocksData(int playerId) {
        this.playerId = playerId;
        normalData = new ArrayList<TotalBlocksEntry>();
        detailedData = new ArrayList<DetailedData>();
    }
    
    @Override
    public List<NormalData> getNormalData() {
        List<NormalData> temp = new ArrayList<NormalData>();
        for(TotalBlocksEntry value : normalData) temp.add(value);
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
     * @param type Material type
     * @param blockData Damage value of the item
     * @return Corresponding entry
     */
    public TotalBlocksEntry getNormalData(Material type, byte blockData) {
        for(TotalBlocksEntry entry : normalData) {
            if(entry.equals(type, blockData)) return entry;
        }
        TotalBlocksEntry entry = new TotalBlocksEntry(playerId, type, blockData);
        normalData.add(entry);
        return entry;
    }
    
    /**
     * Registers the broken block in the data stores
     * @param location Location of the block
     * @param type Material of the block
     * @param data Damage value of the material
     */
    public void blockBreak(Location location, Material type, byte data) {
        getNormalData(type, data).addBroken();
        detailedData.add(new DetailedDestroyerdBlocksEntry(location, type, data));
    }
    
    /**
     * Registers the placed block in the data stores
     * @param location Location of the block
     * @param type Material of the block
     * @param data Damage value of the material
     */
    public void blockPlace(Location location, Material type, byte data) {
        getNormalData(type, data).addPlaced();
        detailedData.add(new DetailedPlacedBlocksEntry(location, type, data));
    }
    
    
    /**
     * Represents an entry in the PVP data store.
     * It is dynamic, i.e. it can be edited once it has been created.
     * @author bitWolfy
     *
     */
    public class TotalBlocksEntry implements NormalData {
        
        private int type;
        private int data;
        private int broken;
        private int placed;

        /**
         * <b>Default constructor</b><br />
         * Creates a new TotalItemsEntry based on the data provided
         * @param materialType
         * @param data
         */
        public TotalBlocksEntry(int playerId, Material materialType, byte data) {
            this.type = materialType.getId();
            if(Settings.ItemsWithMetadata.checkAgainst(this.type)) this.data = data;
            
            this.broken = 0;
            this.placed = 0;
            
            fetchData(playerId);
        }
        
        @Override
        public void fetchData(int playerId) {
            QueryResult result = Query.table(TotalBlocksTable.TableName.toString())
                    .column(TotalBlocksTable.Destroyed.toString())
                    .column(TotalBlocksTable.Placed.toString())
                    .condition(TotalBlocksTable.PlayerId.toString(), playerId)
                    .condition(TotalBlocksTable.Material.toString(), Util.getBlockString(type, data))
                    .select();
            
            if(result == null) {
                Query.table(TotalBlocksTable.TableName.toString())
                    .value(TotalBlocksTable.PlayerId.toString(), playerId)
                    .value(TotalBlocksTable.Material.toString(), Util.getBlockString(type, data))
                    .value(TotalBlocksTable.Destroyed.toString(), broken)
                    .value(TotalBlocksTable.Placed.toString(), placed)
                    .insert();
            } else {
                broken = result.getValueAsInteger(TotalBlocksTable.Destroyed.toString());
                placed = result.getValueAsInteger(TotalBlocksTable.Placed.toString());
            }
        }

        @Override
        public boolean pushData(int playerId) {
            boolean result = Query.table(TotalBlocksTable.TableName.toString())
                .value(TotalBlocksTable.Destroyed.toString(), broken)
                .value(TotalBlocksTable.Placed.toString(), placed)
                .condition(TotalBlocksTable.PlayerId.toString(), playerId)
                .condition(TotalBlocksTable.Material.toString(), Util.getBlockString(type, data))
                .update(true);
            if(Settings.LocalConfiguration.Cloud.asBoolean()) fetchData(playerId);
            return result;
        }
        
        /**
         * Checks if the object corresponds to provided parameters
         * @param materialType
         * @param data
         * @return <b>true</b> if the conditions are met, <b>false</b> otherwise
         */
        public boolean equals(Material materialType, byte data) {
            if(Settings.ItemsWithMetadata.checkAgainst(type)) {
                return this.type == materialType.getId() && this.data == data;
            }
            return this.type == materialType.getId();
        }
        
        /**
         * Increments the number of blocks to the total number of blocks destroyed
         */
        public void addBroken() {
            broken ++;
        }
        
        /**
         * Increments the number of blocks to the total number of blocks placed
         */
        public void addPlaced() {
            placed ++;
        }
    }
    
    
    /**
     * Represents an entry in the Detailed data store.
     * It is static, i.e. it cannot be edited once it has been created.
     * @author bitWolfy
     *
     */
    public class DetailedDestroyerdBlocksEntry implements DetailedData {
        
        private int type;
        private int data;
        private Location location;
        private long timestamp;
        
        /**
         * <b>Default constructor</b><br />
         * Creates a new DetailedDestroyedBlocksEntry based on the data provided
         * @param location
         * @param materialType
         * @param data
         */
        public DetailedDestroyerdBlocksEntry(Location location, Material materialType, byte data) {
            this.type = materialType.getId();
            if(Settings.ItemsWithMetadata.checkAgainst(this.type)) this.data = data;
            
            this.location = location;
            this.timestamp = Util.getTimestamp();
        }

        @Override
        public boolean pushData(int playerId) {
            return Query.table(DestroyedBlocks.TableName.toString())
                .value(DestroyedBlocks.PlayerId.toString(), playerId)
                .value(DestroyedBlocks.Material.toString(), Util.getBlockString(type, data))
                .value(DestroyedBlocks.World.toString(), location.getWorld().getName())
                .value(DestroyedBlocks.XCoord.toString(), location.getBlockX())
                .value(DestroyedBlocks.YCoord.toString(), location.getBlockY())
                .value(DestroyedBlocks.ZCoord.toString(), location.getBlockZ())
                .value(DestroyedBlocks.Timestamp.toString(), timestamp)
                .insert();
        }

    }
    
    
    /**
     * Represents an entry in the Detailed data store.
     * It is static, i.e. it cannot be edited once it has been created.
     * @author bitWolfy
     *
     */
    public class DetailedPlacedBlocksEntry implements DetailedData {
        
        private int type;
        private int data;
        private Location location;
        private long timestamp;

        /**
         * <b>Default constructor</b><br />
         * Creates a new DetailedPlacedBlocksEntry based on the data provided
         * @param location
         * @param materialType
         * @param data
         */
        public DetailedPlacedBlocksEntry(Location location, Material materialType, byte data) {
            this.type = materialType.getId();
            this.data = data;
            
            this.location = location;
            this.timestamp = Util.getTimestamp();
        }

        @Override
        public boolean pushData(int playerId) {
            return Query.table(PlacedBlocks.TableName.toString())
                .value(PlacedBlocks.PlayerId.toString(), playerId)
                .value(PlacedBlocks.Material.toString(), Util.getBlockString(type, data))
                .value(PlacedBlocks.World.toString(), location.getWorld().getName())
                .value(PlacedBlocks.XCoord.toString(), location.getBlockX())
                .value(PlacedBlocks.YCoord.toString(), location.getBlockY())
                .value(PlacedBlocks.ZCoord.toString(), location.getBlockZ())
                .value(PlacedBlocks.Timestamp.toString(), timestamp)
                .insert();
        }
        
    }
    
}
