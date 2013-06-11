/*
 * DetailedPlacedBlocksEntry.java
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

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.block.BlockState;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.data.DetailedData;
import com.wolvencraft.yasp.db.tables.Detailed.PlacedBlocks;
import com.wolvencraft.yasp.util.Util;
import com.wolvencraft.yasp.util.cache.MaterialCache;

/**
 * Represents an entry in the Detailed data store.
 * It is static, i.e. it cannot be edited once it has been created.
 * @author bitWolfy
 *
 */
@Getter(AccessLevel.PUBLIC)
public class DetailedBlockPlaceEntry extends DetailedData {
    
    private BlockState block;
    private long timestamp;

    /**
     * <b>Default constructor</b><br />
     * Creates a new DetailedPlacedBlocksEntry based on the data provided
     * @param block BlockState of the block
     */
    public DetailedBlockPlaceEntry(BlockState block) {
        this.block = block;
        timestamp = Util.getTimestamp();
    }

    @Override
    public boolean pushData(int playerId) {
        Location location = block.getLocation();
        return Query.table(PlacedBlocks.TableName)
            .value(PlacedBlocks.PlayerId, playerId)
            .value(PlacedBlocks.MaterialId, MaterialCache.parse(block))
            .value(PlacedBlocks.World, location.getWorld().getName())
            .value(PlacedBlocks.XCoord, location.getBlockX())
            .value(PlacedBlocks.YCoord, location.getBlockY())
            .value(PlacedBlocks.ZCoord, location.getBlockZ())
            .value(PlacedBlocks.Timestamp, timestamp)
            .insert();
    }
    
}
