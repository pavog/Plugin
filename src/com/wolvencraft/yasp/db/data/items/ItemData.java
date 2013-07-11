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

package com.wolvencraft.yasp.db.data.items;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.data.DataStore;
import com.wolvencraft.yasp.db.data.DetailedData;
import com.wolvencraft.yasp.db.data.items.DetailedItemStats.ItemConsumeEntry;
import com.wolvencraft.yasp.db.data.items.DetailedItemStats.ItemDropEntry;
import com.wolvencraft.yasp.db.data.items.DetailedItemStats.ItemPickupEntry;
import com.wolvencraft.yasp.events.player.TrackedItemDropEvent;
import com.wolvencraft.yasp.events.player.TrackedItemPickupEvent;
import com.wolvencraft.yasp.events.player.TrackedItemUseEvent;
import com.wolvencraft.yasp.session.OnlineSession;

/**
 * Data store that records all item interactions on the server.
 * @author bitWolfy
 *
 */
public class ItemData extends DataStore<TotalItemStats, DetailedData> {
    
    public ItemData(OnlineSession session) {
        super(session, DataStoreType.Items);
    }

    /**
     * Returns the specific entry from the data store.<br />
     * If the entry does not exist, it will be created.
     * @param itemStack Item stack
     * @return Corresponding entry
     */
    public TotalItemStats getNormalData(ItemStack itemStack) {
        for(TotalItemStats entry : getNormalData()) {
            if(entry.equals(itemStack)) return entry;
        }
        TotalItemStats entry = new TotalItemStats(session.getId(), itemStack);
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
            ItemDropEntry detailedEntry = new ItemDropEntry(location, itemStack);
            detailedData.add(detailedEntry);
            
            Bukkit.getServer().getPluginManager().callEvent(new TrackedItemDropEvent(session, detailedEntry));
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
            ItemPickupEntry detailedEntry = new ItemPickupEntry(location, itemStack);
            detailedData.add(detailedEntry);
            
            Bukkit.getServer().getPluginManager().callEvent(new TrackedItemPickupEvent(session, detailedEntry));
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
            ItemConsumeEntry detailedEntry = new ItemConsumeEntry(location, itemStack);
            detailedData.add(detailedEntry);
            
            Bukkit.getServer().getPluginManager().callEvent(new TrackedItemUseEvent(session, detailedEntry));
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
