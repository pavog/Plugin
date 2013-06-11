/*
 * DetailedDestroyedBlocksEntry.java
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

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.data.DetailedData;
import com.wolvencraft.yasp.db.tables.Detailed.DestroyedBlocks;
import com.wolvencraft.yasp.util.Util;
import com.wolvencraft.yasp.util.cache.MaterialCache;

/**
 * Represents an entry in the Detailed data store.
 * It is static, i.e. it cannot be edited once it has been created.
 * @author bitWolfy
 *
 */
public class DetailedBlockBrokenEntry extends DetailedData {
    
    private BlockState block;
    private Location location;
    private long timestamp;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new DetailedDestroyedBlocksEntry based on the data provided
     * @param location Location of the block
     * @param block BlockState of the block
     */
    public DetailedBlockBrokenEntry(Location location, BlockState block) {
        this.block = block;
        this.location = location.clone();
        timestamp = Util.getTimestamp();
    }

    @Override
    public boolean pushData(int playerId) {
        return Query.table(DestroyedBlocks.TableName)
            .value(DestroyedBlocks.PlayerId, playerId)
            .value(DestroyedBlocks.MaterialId, MaterialCache.parse(block))
            .value(DestroyedBlocks.World, location.getWorld().getName())
            .value(DestroyedBlocks.XCoord, location.getBlockX())
            .value(DestroyedBlocks.YCoord, location.getBlockY())
            .value(DestroyedBlocks.ZCoord, location.getBlockZ())
            .value(DestroyedBlocks.Timestamp, timestamp)
            .insert();
    }

}
