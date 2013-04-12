/*
 * ItemsData.java
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
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.Settings;
import com.wolvencraft.yasp.Settings.ItemsWithMetadata;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.tables.Detailed.DroppedItems;
import com.wolvencraft.yasp.db.tables.Detailed.PickedupItems;
import com.wolvencraft.yasp.db.tables.Detailed.UsedItems;
import com.wolvencraft.yasp.db.tables.Normal.TotalItemsTable;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.Util;
import com.wolvencraft.yasp.util.cache.MaterialCache;

/**
 * Data store that records all item interactions on the server.
 * @author bitWolfy
 *
 */
public class ItemsData implements DataStore {
    
    private int playerId;
    private List<TotalItemsEntry> normalData;
    private List<DetailedData> detailedData;

    /**
     * <b>Default constructor</b><br />
     * Creates an empty data store to save the statistics until database synchronization.
     */
    public ItemsData(int playerId) {
        this.playerId = playerId;
        normalData = new ArrayList<TotalItemsEntry>();
        detailedData = new ArrayList<DetailedData>();
    }
    
    @Override
    public DataStoreType getType() {
        return DataStoreType.Items;
    }

    @Override
    public List<NormalData> getNormalData() {
        List<NormalData> temp = new ArrayList<NormalData>();
        for(NormalData value : normalData) temp.add(value);
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
     * @param itemStack Item stack
     * @return Corresponding entry
     */
    public TotalItemsEntry getNormalData(ItemStack itemStack) {
        for(TotalItemsEntry entry : normalData) {
            if(entry.equals(itemStack)) return entry;
        }
        TotalItemsEntry entry = new TotalItemsEntry(playerId, itemStack);
        normalData.add(entry);
        return entry;
    }
    
    /**
     * Registers the dropped item in the data stores
     * @param location Location of the event
     * @param itemStack Stack of items in question
     */
    public void itemDrop(Location location, ItemStack itemStack) {
        int amount = itemStack.getAmount();
        getNormalData(itemStack).addDropped(amount);
        for(int i = 0; i < amount; i++) {
            detailedData.add(new DetailedDroppedItemsEntry(location, itemStack));
        }
    }
    
    /**
     * Registers the picked up item in the data stores
     * @param location Location of the event
     * @param itemStack Stack of items in question
     */
    public void itemPickUp(Location location, ItemStack itemStack) {
        int amount = itemStack.getAmount();
        getNormalData(itemStack).addPickedUp(amount);
        for(int i = 0; i < amount; i++) {
            detailedData.add(new DetailedPickedupItemsEntry(location, itemStack));
        }
    }
    
    /**
     * Registers the used item in the data stores
     * @param location Location of the event
     * @param itemStack Stack of items in question
     */
    public void itemUse(Location location, ItemStack itemStack) {
        int amount = itemStack.getAmount();
        getNormalData(itemStack).addUsed(amount);
        for(int i = 0; i < amount; i++) {
            detailedData.add(new DetailedUsedItemsEntry(location, itemStack));
        }
    }
    
    /**
     * Registers the crafted item in the data stores
     * @param location Location of the event
     * @param itemStack Stack of items in question
     */
    public void itemCraft(Location location, ItemStack itemStack) {
        getNormalData(itemStack).addCrafted(itemStack.getAmount());
    }
    
    /**
     * Registers the smelted item in the data stores
     * @param location Location of the event
     * @param itemStack Stack of items in question
     */
    public void itemSmelt(Location location, ItemStack itemStack) {
        getNormalData(itemStack).addSmelted(itemStack.getAmount());
    }
    
    /**
     * Registers the broken item in the data stores
     * @param location Location of the event
     * @param itemStack Stack of items in question
     */
    public void itemBreak(Location location, ItemStack itemStack) {
        getNormalData(itemStack).addBroken(itemStack.getAmount());
    }
    
    /**
     * Registers the enchanted item in the data stores
     * @param location Location of the event
     * @param itemStack Stack of items in question
     */
    public void itemEnchant(Location location, ItemStack itemStack) {
        getNormalData(itemStack).addEnchanted(itemStack.getAmount());
    }
    
    
    /**
     * Represents the total number of items player dropped and picked up.<br />
     * Each entry must have a unique player - material ID combination.
     * @author bitWolfy
     *
     */
    public class TotalItemsEntry implements NormalData {
        
        private ItemStack stack;
        private int dropped;
        private int pickedUp;
        private int used;
        private int crafted;
        private int broken;
        private int smelted;
        private int enchanted;
        
        /**
         * <b>Default constructor</b><br />
         * Creates a new TotalItemsEntry based on the data provided
         * @param stack Item stack
         */
        public TotalItemsEntry(int playerId, ItemStack stack) {
            Message.debug(Level.FINE, "Creating a new instance of TotalItemsEntry");
            this.stack = stack;
            this.stack.setAmount(1);
            
            this.dropped = 0;
            this.pickedUp = 0;
            this.used = 0;
            this.crafted = 0;
            this.broken = 0;
            this.smelted = 0;
            this.enchanted = 0;
            
            fetchData(playerId);
        }
        
        @Override
        public void fetchData(int playerId) {
            QueryResult result = Query.table(TotalItemsTable.TableName)
                    .column(TotalItemsTable.Dropped)
                    .column(TotalItemsTable.PickedUp)
                    .column(TotalItemsTable.Used)
                    .column(TotalItemsTable.Crafted)
                    .column(TotalItemsTable.Broken)
                    .column(TotalItemsTable.Smelted)
                    .column(TotalItemsTable.Enchanted)
                    .condition(TotalItemsTable.PlayerId, playerId)
                    .condition(TotalItemsTable.Material, MaterialCache.parse(stack))
                    .select();
            
            if(result == null) {
                Query.table(TotalItemsTable.TableName)
                    .value(TotalItemsTable.PlayerId, playerId)
                    .value(TotalItemsTable.Material, MaterialCache.parse(stack))
                    .value(TotalItemsTable.Dropped, dropped)
                    .value(TotalItemsTable.PickedUp, pickedUp)
                    .value(TotalItemsTable.Used, used)
                    .value(TotalItemsTable.Crafted, crafted)
                    .value(TotalItemsTable.Broken, broken)
                    .value(TotalItemsTable.Smelted, smelted)
                    .value(TotalItemsTable.Enchanted, enchanted)
                    .insert();
            } else {
                dropped = result.asInt(TotalItemsTable.Dropped);
                pickedUp = result.asInt(TotalItemsTable.PickedUp);
                used = result.asInt(TotalItemsTable.Used);
                crafted = result.asInt(TotalItemsTable.Crafted);
                broken = result.asInt(TotalItemsTable.Broken);
                smelted = result.asInt(TotalItemsTable.Smelted);
                enchanted = result.asInt(TotalItemsTable.Enchanted);
            }
        }

        @Override
        public boolean pushData(int playerId) {
            boolean result = Query.table(TotalItemsTable.TableName)
                    .value(TotalItemsTable.Dropped, dropped)
                    .value(TotalItemsTable.PickedUp, pickedUp)
                    .value(TotalItemsTable.Used, used)
                    .value(TotalItemsTable.Crafted, crafted)
                    .value(TotalItemsTable.Broken, broken)
                    .value(TotalItemsTable.Smelted, smelted)
                    .value(TotalItemsTable.Enchanted, enchanted)
                    .condition(TotalItemsTable.PlayerId, playerId)
                    .condition(TotalItemsTable.Material, MaterialCache.parse(stack))
                    .update();
            if(Settings.LocalConfiguration.Cloud.asBoolean()) fetchData(playerId);
            return result;
        }
        
        /**
         * Checks if the ItemStack corresponds to this entry 
         * @param stack ItemStack to check
         * @return b>true</b> if the data matches, <b>false</b> otherwise.
         */
        public boolean equals(ItemStack stack) {
            if(stack.getType().equals(Material.POTION)) {
                return stack.getType().equals(this.stack.getType()) && stack.getDurability() == this.stack.getDurability();
            } else if(ItemsWithMetadata.checkAgainst(stack.getTypeId())) {
                return stack.getType().equals(this.stack.getType()) && stack.getData().getData() == this.stack.getData().getData();
            } else {
                return stack.getType().equals(this.stack.getType());
            }
        }
        
        /**
         * Increments the number of items dropped
         * @param amount Number of items
         */
        public void addDropped(int amount) {
            dropped += amount;
        }
        
        /**
         * Increments the number of items picked up
         * @param amount Number of items
         */
        public void addPickedUp(int amount) {
            pickedUp += amount;
        }
        
        /**
         * Increments the number of items used.<br />
         * Currently only tracks food consumption
         * @param amount Number of items
         */
        public void addUsed(int amount) {
            used += amount;
        }
        
        /**
         * Increments the number of items crafted
         * @param amount Number of items
         */
        public void addCrafted(int amount) {
            crafted += amount;
        }
        
        /**
         * Increments the number of tools broken
         * @param amount Number of items
         */
        public void addBroken(int amount) {
            broken += amount;
        }
        
        /**
         * Increments the number of items smelted
         * @param amount Number of items
         */
        public void addSmelted(int amount) {
            smelted += amount;
        }
        
        /**
         * Increments the number of items enchanted
         * @param amount Number of items
         */
        public void addEnchanted(int amount) {
            enchanted += amount;
        }
    }
    
    
    /**
     * Represents an entry in the Detailed data store.
     * It is static, i.e. it cannot be edited once it has been created.
     * @author bitWolfy
     *
     */
    public class DetailedDroppedItemsEntry implements DetailedData {
        
        private ItemStack stack;
        private Location location;
        private long timestamp;

        /**
         * <b>Default constructor</b><br />
         * Creates a new DetailedDroppedItemsEntry based on the data provided
         * @param location
         * @param itemStack
         */
        public DetailedDroppedItemsEntry(Location location, ItemStack stack) {
            Message.debug(Level.FINE, "Creating a new instance of DetailedDroppedItemsEntry");
            this.stack = stack;
            this.stack.setAmount(1);
            
            this.location = location;
            this.timestamp = Util.getTimestamp();
        }
        
        @Override
        public boolean pushData(int playerId) {
            return Query.table(DroppedItems.TableName)
                    .value(DroppedItems.PlayerId, playerId)
                    .value(DroppedItems.Material, MaterialCache.parse(stack))
                    .value(DroppedItems.World, location.getWorld().getName())
                    .value(DroppedItems.XCoord, location.getBlockX())
                    .value(DroppedItems.YCoord, location.getBlockY())
                    .value(DroppedItems.ZCoord, location.getBlockZ())
                    .value(DroppedItems.Timestamp, timestamp)
                    .insert();
        }

    }
    
    
    /**
     * Represents an entry in the Detailed data store.
     * It is static, i.e. it cannot be edited once it has been created.
     * @author bitWolfy
     *
     */
    public class DetailedPickedupItemsEntry implements DetailedData {
        
        private ItemStack stack;
        private Location location;
        private long timestamp;
        
        /**
         * <b>Default constructor</b><br />
         * Creates a new DetailedPickedupItemsEntry based on the data provided
         * @param location Item location
         * @param stack Item stack
         */
        public DetailedPickedupItemsEntry(Location location, ItemStack stack) {
            Message.debug(Level.FINE, "Creating a new instance of DetailedPickedupItemsEntry");
            this.stack = stack;
            this.stack.setAmount(1);
            
            this.location = location;
            this.timestamp = Util.getTimestamp();
        }
        
        @Override
        public boolean pushData(int playerId) {
            return Query.table(PickedupItems.TableName)
                    .value(PickedupItems.PlayerId, playerId)
                    .value(PickedupItems.Material, MaterialCache.parse(stack))
                    .value(PickedupItems.World, location.getWorld().getName())
                    .value(PickedupItems.XCoord, location.getBlockX())
                    .value(PickedupItems.YCoord, location.getBlockY())
                    .value(PickedupItems.ZCoord, location.getBlockZ())
                    .value(PickedupItems.Timestamp, timestamp)
                    .insert();
        }

    }
    
    
    /**
     * Represents an entry in the Detailed data store.
     * It is static, i.e. it cannot be edited once it has been created.
     * @author bitWolfy
     *
     */
    public class DetailedUsedItemsEntry implements DetailedData {

        private ItemStack stack;
        private Location location;
        private long timestamp;
        
        /**
         * <b>Default constructor</b><br />
         * Creates a new DetailedUsedItemsEntry based on the data provided
         * @param location Item location
         * @param itemStack Item stack
         */
        public DetailedUsedItemsEntry(Location location, ItemStack stack) {
            Message.debug(Level.FINE, "Creating a new instance of DetailedUsedItemsEntry");
            this.stack = stack;
            this.stack.setAmount(1);
            
            this.location = location;
            this.timestamp = Util.getTimestamp();
        }
        
        @Override
        public boolean pushData(int playerId) {
            return Query.table(UsedItems.TableName)
                    .value(UsedItems.PlayerId, playerId)
                    .value(UsedItems.Material, MaterialCache.parse(stack))
                    .value(UsedItems.World, location.getWorld().getName())
                    .value(UsedItems.XCoord, location.getBlockX())
                    .value(UsedItems.YCoord, location.getBlockY())
                    .value(UsedItems.ZCoord, location.getBlockZ())
                    .value(UsedItems.Timestamp, timestamp)
                    .insert();
        }

    }
    
}
