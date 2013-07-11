/*
 * ItemsDataStore.java
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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.mctrakr.events.player.TrackedItemDropEvent;
import com.mctrakr.events.player.TrackedItemPickupEvent;
import com.mctrakr.events.player.TrackedItemUseEvent;
import com.mctrakr.modules.LargeDataStore;
import com.mctrakr.modules.DataStore.DetailedData;
import com.mctrakr.modules.stats.items.ItemsDetailedStats.ItemConsumeEntry;
import com.mctrakr.modules.stats.items.ItemsDetailedStats.ItemDropEntry;
import com.mctrakr.modules.stats.items.ItemsDetailedStats.ItemPickupEntry;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock;
import com.mctrakr.settings.ConfigLock.PrimaryType;

/**
 * Data store that records all item interactions on the server.
 * @author bitWolfy
 *
 */
public class ItemsDataStore extends LargeDataStore<ItemsTotalStats, DetailedData> {
    
    public static ConfigLock lock = new ConfigLock(PrimaryType.Items);
    
    public ItemsDataStore(OnlineSession session) {
        super(session, PrimaryType.Items);
    }
    
    @Override
    public ConfigLock getLock() {
        return lock;
    }

    /**
     * Returns the specific entry from the data store.<br />
     * If the entry does not exist, it will be created.
     * @param itemStack Item stack
     * @return Corresponding entry
     */
    public ItemsTotalStats getNormalData(ItemStack itemStack) {
        for(ItemsTotalStats entry : getNormalData()) {
            if(entry.equals(itemStack)) return entry;
        }
        ItemsTotalStats entry = new ItemsTotalStats(session, itemStack);
        getNormalData().add(entry);
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
            ItemDropEntry detailedEntry = new ItemDropEntry(session, location, itemStack);
            addDetailedDataEntry(detailedEntry);
            
            Bukkit.getServer().getPluginManager().callEvent(new TrackedItemDropEvent(getSession(), detailedEntry));
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
            ItemPickupEntry detailedEntry = new ItemPickupEntry(session, location, itemStack);
            addDetailedDataEntry(detailedEntry);
            
            Bukkit.getServer().getPluginManager().callEvent(new TrackedItemPickupEvent(getSession(), detailedEntry));
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
            ItemConsumeEntry detailedEntry = new ItemConsumeEntry(session, location, itemStack);
            addDetailedDataEntry(detailedEntry);
            
            Bukkit.getServer().getPluginManager().callEvent(new TrackedItemUseEvent(getSession(), detailedEntry));
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
     * Registers the repaired item in the data stores
     * @param location Location of the event
     * @param itemStack Stack of items in question
     */
    public void itemRepair(Location location, ItemStack itemStack) {
        getNormalData(itemStack).addRepaired(itemStack.getAmount());
    }
    
}
