/*
 * ItemsDetailedStats.java
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

package com.mctrakr.modules.stats.items;

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.mctrakr.cache.MaterialCache;
import com.mctrakr.database.Query;
import com.mctrakr.modules.DataStore.DetailedData;
import com.mctrakr.modules.stats.items.Tables.DetailedItemsConsumedTable;
import com.mctrakr.modules.stats.items.Tables.DetailedItemsDroppedTable;
import com.mctrakr.modules.stats.items.Tables.DetailedItemsPickedUpTable;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.util.Util;

public class ItemsDetailedStats {
    
    /**
     * An immutable item drop entry
     * @author bitWolfy
     *
     */
    @Getter(AccessLevel.PUBLIC)
    public static class ItemDropEntry extends DetailedData {
        
        private final ItemStack stack;
        private final Location location;
        private final long timestamp;
        
        public ItemDropEntry(OnlineSession session, Location location, ItemStack stack) {
            super(session);
            this.stack = stack.clone();
            this.stack.setAmount(1);
            this.location = location.clone();
            timestamp = Util.getTimestamp();
        }
        
        @Override
        public boolean pushData() {
            return Query.table(DetailedItemsDroppedTable.TableName)
                    .value(DetailedItemsDroppedTable.PlayerId, session.getId())
                    .value(DetailedItemsDroppedTable.MaterialId, MaterialCache.parse(stack))
                    .value(DetailedItemsDroppedTable.World, location.getWorld().getName())
                    .value(DetailedItemsDroppedTable.XCoord, location.getBlockX())
                    .value(DetailedItemsDroppedTable.YCoord, location.getBlockY())
                    .value(DetailedItemsDroppedTable.ZCoord, location.getBlockZ())
                    .value(DetailedItemsDroppedTable.Timestamp, timestamp)
                    .insert();
        }
    }
    
    /**
     * An immutable item pickup entry
     * @author bitWolfy
     *
     */
    @Getter(AccessLevel.PUBLIC)
    public static class ItemPickupEntry extends DetailedData {
        
        private final ItemStack stack;
        private final Location location;
        private final long timestamp;
        
        public ItemPickupEntry(OnlineSession session, Location location, ItemStack stack) {
            super(session);
            this.stack = stack.clone();
            this.stack.setAmount(1);
            this.location = location.clone();
            timestamp = Util.getTimestamp();
        }
        
        @Override
        public boolean pushData() {
            return Query.table(DetailedItemsPickedUpTable.TableName)
                    .value(DetailedItemsPickedUpTable.PlayerId, session.getId())
                    .value(DetailedItemsPickedUpTable.Material, MaterialCache.parse(stack))
                    .value(DetailedItemsPickedUpTable.World, location.getWorld().getName())
                    .value(DetailedItemsPickedUpTable.XCoord, location.getBlockX())
                    .value(DetailedItemsPickedUpTable.YCoord, location.getBlockY())
                    .value(DetailedItemsPickedUpTable.ZCoord, location.getBlockZ())
                    .value(DetailedItemsPickedUpTable.Timestamp, timestamp)
                    .insert();
        }
    }
    
    /**
     * An immutable item consume entry
     * @author bitWolfy
     *
     */
    @Getter(AccessLevel.PUBLIC)
    public static class ItemConsumeEntry extends DetailedData {

        private final ItemStack stack;
        private final Location location;
        private final long timestamp;
        
        public ItemConsumeEntry(OnlineSession session, Location location, ItemStack stack) {
            super(session);
            this.stack = stack.clone();
            this.stack.setAmount(1);
            this.location = location.clone();
            timestamp = Util.getTimestamp();
        }
        
        @Override
        public boolean pushData() {
            return Query.table(DetailedItemsConsumedTable.TableName)
                    .value(DetailedItemsConsumedTable.PlayerId, session.getId())
                    .value(DetailedItemsConsumedTable.MaterialId, MaterialCache.parse(stack))
                    .value(DetailedItemsConsumedTable.World, location.getWorld().getName())
                    .value(DetailedItemsConsumedTable.XCoord, location.getBlockX())
                    .value(DetailedItemsConsumedTable.YCoord, location.getBlockY())
                    .value(DetailedItemsConsumedTable.ZCoord, location.getBlockZ())
                    .value(DetailedItemsConsumedTable.Timestamp, timestamp)
                    .insert();
        }
    }
}
