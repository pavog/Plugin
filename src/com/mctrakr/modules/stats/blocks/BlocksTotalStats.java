/*
 * BlocksTotalStats.java
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

package com.mctrakr.modules.stats.blocks;

import org.bukkit.block.BlockState;

import com.mctrakr.cache.MaterialCache;
import com.mctrakr.database.Query;
import com.mctrakr.database.Query.QueryResult;
import com.mctrakr.modules.DataStore.NormalData;
import com.mctrakr.modules.stats.blocks.Tables.TotalBlocksTable;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.Constants.ItemsWithMetadata;

/**
 * Represents an entry in the PVP data store.
 * It is dynamic, i.e. it can be edited once it has been created.
 * @author bitWolfy
 *
 */
public class BlocksTotalStats extends NormalData {
    
    private BlockState block;
    private int broken, placed;
    
    public BlocksTotalStats(OnlineSession session, BlockState block) {
        super(session);
        this.block = block;
        broken = 0;
        placed = 0;
        
        fetchData();
    }
    
    @Override
    public void fetchData() {
        QueryResult result = Query.table(TotalBlocksTable.TableName)
                .column(TotalBlocksTable.Destroyed)
                .column(TotalBlocksTable.Placed)
                .condition(TotalBlocksTable.PlayerId, session.getId())
                .condition(TotalBlocksTable.MaterialId, MaterialCache.parse(block))
                .select();
        
        if(result == null) {
            Query.table(TotalBlocksTable.TableName)
                .value(TotalBlocksTable.PlayerId, session.getId())
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
    public boolean pushData() {
        boolean result = Query.table(TotalBlocksTable.TableName)
            .value(TotalBlocksTable.Destroyed, broken)
            .value(TotalBlocksTable.Placed, placed)
            .condition(TotalBlocksTable.PlayerId, session.getId())
            .condition(TotalBlocksTable.MaterialId, MaterialCache.parse(block))
            .update();
        return result;
    }
    
    /**
     * Checks if the object corresponds to provided parameters
     * @param block Block to compare to
     * @return <b>true</b> if the conditions are met, <b>false</b> otherwise
     */
    public boolean equals(BlockState block) {
        if(ItemsWithMetadata.contains(block.getTypeId())) {
            return block.getType().equals(this.block.getType()) && block.getData().getData() == this.block.getData().getData();
        } else {
            return block.getType().equals(this.block.getType());
        }
    }
    
    /**
     * Increments the number of blocks to the total number of blocks broken
     */
    public void addBroken() {
        broken++;
    }
    
    /**
     * Increments the number of blocks to the total number of blocks placed
     */
    public void addPlaced() {
        placed++;
    }
}
