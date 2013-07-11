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

package com.mctrakr.modules.stats.blocks;

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.block.BlockState;

import com.mctrakr.cache.MaterialCache;
import com.mctrakr.database.Query;
import com.mctrakr.modules.DataStore.DetailedData;
import com.mctrakr.modules.stats.blocks.Tables.DetailedBrokenBlocksTable;
import com.mctrakr.modules.stats.blocks.Tables.DetailedPlacedBlocksTable;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.util.Util;

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
        
        public BlockBreakEntry(OnlineSession session, BlockState block) {
            super(session);
            this.block = block;
            timestamp = Util.getTimestamp();
        }

        @Override
        public boolean pushData() {
            Location location = block.getLocation();
            return Query.table(DetailedBrokenBlocksTable.TableName)
                .value(DetailedBrokenBlocksTable.PlayerId, session.getId())
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
        
        public BlockPlaceEntry(OnlineSession session, BlockState block) {
            super(session);
            this.block = block;
            timestamp = Util.getTimestamp();
        }

        @Override
        public boolean pushData() {
            Location location = block.getLocation();
            return Query.table(DetailedPlacedBlocksTable.TableName)
                .value(DetailedPlacedBlocksTable.PlayerId, session.getId())
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
