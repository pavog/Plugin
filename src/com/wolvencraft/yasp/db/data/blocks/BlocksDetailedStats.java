/*
 * BlocksDetailedStats.java
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
import com.wolvencraft.yasp.db.data.blocks.Tables.DetailedBrokenBlocksTable;
import com.wolvencraft.yasp.db.data.blocks.Tables.DetailedPlacedBlocksTable;
import com.wolvencraft.yasp.util.Util;
import com.wolvencraft.yasp.util.cache.MaterialCache;

public class BlocksDetailedStats {
    
    /**
     * An immutable block breaking entry
     * @author bitWolfy
     *
     */
    @Getter(AccessLevel.PUBLIC)
    public static class BlockBreakEntry extends DetailedData {
        
        private final BlockState block;
        private final long timestamp;
        
        public BlockBreakEntry(BlockState block) {
            this.block = block;
            timestamp = Util.getTimestamp();
        }

        @Override
        public boolean pushData(int playerId) {
            Location location = block.getLocation();
            return Query.table(DetailedBrokenBlocksTable.TableName)
                .value(DetailedBrokenBlocksTable.PlayerId, playerId)
                .value(DetailedBrokenBlocksTable.MaterialId, MaterialCache.parse(block))
                .value(DetailedBrokenBlocksTable.World, location.getWorld().getName())
                .value(DetailedBrokenBlocksTable.XCoord, location.getBlockX())
                .value(DetailedBrokenBlocksTable.YCoord, location.getBlockY())
                .value(DetailedBrokenBlocksTable.ZCoord, location.getBlockZ())
                .value(DetailedBrokenBlocksTable.Timestamp, timestamp)
                .insert();
        }
    }
    
    /**
     * An immutable block placement entry
     * @author bitWolfy
     *
     */
    @Getter(AccessLevel.PUBLIC)
    public static class BlockPlaceEntry extends DetailedData {
        
        private final BlockState block;
        private final long timestamp;
        
        public BlockPlaceEntry(BlockState block) {
            this.block = block;
            timestamp = Util.getTimestamp();
        }

        @Override
        public boolean pushData(int playerId) {
            Location location = block.getLocation();
            return Query.table(DetailedPlacedBlocksTable.TableName)
                .value(DetailedPlacedBlocksTable.PlayerId, playerId)
                .value(DetailedPlacedBlocksTable.MaterialId, MaterialCache.parse(block))
                .value(DetailedPlacedBlocksTable.World, location.getWorld().getName())
                .value(DetailedPlacedBlocksTable.XCoord, location.getBlockX())
                .value(DetailedPlacedBlocksTable.YCoord, location.getBlockY())
                .value(DetailedPlacedBlocksTable.ZCoord, location.getBlockZ())
                .value(DetailedPlacedBlocksTable.Timestamp, timestamp)
                .insert();
        }
    }
}
