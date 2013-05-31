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

package com.wolvencraft.yasp.db.data.blocks;

import org.bukkit.Location;
import org.bukkit.block.BlockState;

import com.wolvencraft.yasp.db.data.AdvancedDataStore;
import com.wolvencraft.yasp.db.data.DetailedData;

/**
 * Data store that records all block interactions on the server.
 * @author bitWolfy
 *
 */
public class BlocksData extends AdvancedDataStore<TotalBlocksEntry, DetailedData> {
    
    public BlocksData(int playerId) {
        super(playerId, DataStoreType.Blocks);
    }

    /**
     * Returns the specific entry from the data store.<br />
     * If the entry does not exist, it will be created.
     * @param block BlockState of the block
     * @return Corresponding entry
     */
    private TotalBlocksEntry getNormalData(BlockState block) {
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
        detailedData.add(new DetailedDestroyedBlocksEntry(location, block));
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
    
}
