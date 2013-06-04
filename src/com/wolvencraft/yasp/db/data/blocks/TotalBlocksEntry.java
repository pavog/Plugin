/*
 * TotalBlocksEntry.java
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

package com.wolvencraft.yasp.db.data.blocks;

import org.bukkit.block.BlockState;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.data.NormalData;
import com.wolvencraft.yasp.db.tables.Normal.TotalBlocksTable;
import com.wolvencraft.yasp.settings.Constants.ItemsWithMetadata;
import com.wolvencraft.yasp.settings.RemoteConfiguration;
import com.wolvencraft.yasp.util.cache.MaterialCache;

/**
 * Represents an entry in the PVP data store.
 * It is dynamic, i.e. it can be edited once it has been created.
 * @author bitWolfy
 *
 */
public class TotalBlocksEntry extends NormalData {
    
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
        broken = 0;
        placed = 0;
        
        fetchData(playerId);
    }
    
    @Override
    public void fetchData(int playerId) {
        if(RemoteConfiguration.MergedDataTracking.asBoolean()) {
            clearData(playerId);
            return;
        }
        
        QueryResult result = Query.table(TotalBlocksTable.TableName)
                .column(TotalBlocksTable.Destroyed)
                .column(TotalBlocksTable.Placed)
                .condition(TotalBlocksTable.PlayerId, playerId)
                .condition(TotalBlocksTable.MaterialId, MaterialCache.parse(block))
                .select();
        
        if(result == null) {
            Query.table(TotalBlocksTable.TableName)
                .value(TotalBlocksTable.PlayerId, playerId)
                .value(TotalBlocksTable.MaterialId, MaterialCache.parse(block))
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
            .condition(TotalBlocksTable.MaterialId, MaterialCache.parse(block))
            .update(RemoteConfiguration.MergedDataTracking.asBoolean());
        fetchData(playerId);
        return result;
    }
    
    @Override
    public void clearData(int playerId) {
        broken = 0;
        placed = 0;
    }
    
    /**
     * Checks if the object corresponds to provided parameters
     * @param block Block to compare to
     * @return <b>true</b> if the conditions are met, <b>false</b> otherwise
     */
    public boolean equals(BlockState block) {
        if(ItemsWithMetadata.checkAgainst(block.getTypeId())) {
            return block.getType().equals(this.block.getType()) && block.getData().getData() == this.block.getData().getData();
        } else {
            return block.getType().equals(this.block.getType());
        }
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
