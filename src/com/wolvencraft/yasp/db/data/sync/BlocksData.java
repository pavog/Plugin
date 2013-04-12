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
import org.bukkit.block.BlockState;

import com.wolvencraft.yasp.Settings;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.tables.Detailed.PlacedBlocks;
import com.wolvencraft.yasp.db.tables.Detailed.DestroyedBlocks;
import com.wolvencraft.yasp.db.tables.Normal.TotalBlocksTable;
import com.wolvencraft.yasp.util.Util;
import com.wolvencraft.yasp.util.cache.MaterialCache;

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
    public DataStoreType getType() {
        return DataStoreType.Blocks;
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
     * @param block BlockState of the block
     * @return Corresponding entry
     */
    public TotalBlocksEntry getNormalData(BlockState block) {
        for(TotalBlocksEntry entry : normalData) {
            if(entry.equals(block)) return entry;
        }
        TotalBlocksEntry entry = new TotalBlocksEntry(playerId, block);
        normalData.add(entry);
        return entry;
    }
    
    /**
     * Registers the broken block in the data stores
     * @param location Location of the block
     * @param block BlockState of the block
     */
    public void blockBreak(Location location, BlockState block) {
        getNormalData(block).addBroken();
        detailedData.add(new DetailedDestroyerdBlocksEntry(location, block));
    }
    
    /**
     * Registers the placed block in the data stores
     * @param location Location of the block
     * @param block BlockState of the block
     */
    public void blockPlace(Location location, BlockState block) {
        getNormalData(block).addPlaced();
        detailedData.add(new DetailedPlacedBlocksEntry(location, block));
    }
    
    
    /**
     * Represents an entry in the PVP data store.
     * It is dynamic, i.e. it can be edited once it has been created.
     * @author bitWolfy
     *
     */
    public class TotalBlocksEntry implements NormalData {
        
        private BlockState block;
        
        private int broken;
        private int placed;

        /**
         * <b>Default constructor</b><br />
         * Creates a new TotalItemsEntry based on the data provided
         * @param block BlockState of the block
         */
        public TotalBlocksEntry(int playerId, BlockState block) {
            this.block = block;
            
            this.broken = 0;
            this.placed = 0;
            
            fetchData(playerId);
        }
        
        @Override
        public void fetchData(int playerId) {
            QueryResult result = Query.table(TotalBlocksTable.TableName)
                    .column(TotalBlocksTable.Destroyed)
                    .column(TotalBlocksTable.Placed)
                    .condition(TotalBlocksTable.PlayerId, playerId)
                    .condition(TotalBlocksTable.Material, MaterialCache.parse(block))
                    .select();
            
            if(result == null) {
                Query.table(TotalBlocksTable.TableName)
                    .value(TotalBlocksTable.PlayerId, playerId)
                    .value(TotalBlocksTable.Material, MaterialCache.parse(block))
                    .value(TotalBlocksTable.Destroyed, broken)
                    .value(TotalBlocksTable.Placed, placed)
                    .insert();
            } else {
                broken = result.asInt(TotalBlocksTable.Destroyed);
                placed = result.asInt(TotalBlocksTable.Placed);
            }
        }

        @Override
        public boolean pushData(int playerId) {
            boolean result = Query.table(TotalBlocksTable.TableName)
                .value(TotalBlocksTable.Destroyed, broken)
                .value(TotalBlocksTable.Placed, placed)
                .condition(TotalBlocksTable.PlayerId, playerId)
                .condition(TotalBlocksTable.Material, MaterialCache.parse(block))
                .update();
            if(Settings.LocalConfiguration.Cloud.asBoolean()) fetchData(playerId);
            return result;
        }
        
        /**
         * Checks if the object corresponds to provided parameters
         * @param item ItemStack to compare to
         * @return <b>true</b> if the conditions are met, <b>false</b> otherwise
         */
        public boolean equals(BlockState block) {
            return block.equals(this.block);
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
        
        private BlockState block;
        private Location location;
        private long timestamp;
        
        /**
         * <b>Default constructor</b><br />
         * Creates a new DetailedDestroyedBlocksEntry based on the data provided
         * @param location Location of the block
         * @param block BlockState of the block
         */
        public DetailedDestroyerdBlocksEntry(Location location, BlockState block) {
            this.block = block;
            
            this.location = location;
            this.timestamp = Util.getTimestamp();
        }

        @Override
        public boolean pushData(int playerId) {
            return Query.table(DestroyedBlocks.TableName)
                .value(DestroyedBlocks.PlayerId, playerId)
                .value(DestroyedBlocks.Material, MaterialCache.parse(block))
                .value(DestroyedBlocks.World, location.getWorld().getName())
                .value(DestroyedBlocks.XCoord, location.getBlockX())
                .value(DestroyedBlocks.YCoord, location.getBlockY())
                .value(DestroyedBlocks.ZCoord, location.getBlockZ())
                .value(DestroyedBlocks.Timestamp, timestamp)
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
        
        private BlockState block;
        private Location location;
        private long timestamp;

        /**
         * <b>Default constructor</b><br />
         * Creates a new DetailedPlacedBlocksEntry based on the data provided
         * @param location Location of the block
         * @param block BlockState of the block
         */
        public DetailedPlacedBlocksEntry(Location location, BlockState block) {
            this.block = block;
            
            this.location = location;
            this.timestamp = Util.getTimestamp();
        }

        @Override
        public boolean pushData(int playerId) {
            return Query.table(PlacedBlocks.TableName)
                .value(PlacedBlocks.PlayerId, playerId)
                .value(PlacedBlocks.Material, MaterialCache.parse(block))
                .value(PlacedBlocks.World, location.getWorld().getName())
                .value(PlacedBlocks.XCoord, location.getBlockX())
                .value(PlacedBlocks.YCoord, location.getBlockY())
                .value(PlacedBlocks.ZCoord, location.getBlockZ())
                .value(PlacedBlocks.Timestamp, timestamp)
                .insert();
        }
        
    }
    
}
