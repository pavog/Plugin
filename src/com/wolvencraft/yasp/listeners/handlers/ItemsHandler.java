/*
 * ItemsHandler.java
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

package com.wolvencraft.yasp.listeners.handlers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

import com.wolvencraft.yasp.db.data.DataStore.DataStoreType;
import com.wolvencraft.yasp.db.data.items.ItemData;
import com.wolvencraft.yasp.db.tables.Normal.PlayerData;
import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.util.cache.OnlineSessionCache;

public class ItemsHandler {
    
    /**
     * Executed when player picks up an item
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class ItemPickup implements Runnable {
        
        private Player player;
        private Location location;
        private ItemStack itemStack;
        
        @Override
        public void run() {
            OnlineSession session = OnlineSessionCache.fetch(player);
            ((ItemData) session.getDataStore(DataStoreType.Items)).itemPickUp(location, itemStack);
        }
    }
    
    /**
     * Executed when player drops an item
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class ItemDrop implements Runnable {
        
        private Player player;
        private Location location;
        private ItemStack itemStack;
        
        @Override
        public void run() {
            OnlineSession session = OnlineSessionCache.fetch(player);
            ((ItemData) session.getDataStore(DataStoreType.Items)).itemDrop(location, itemStack);
        }
    }
    
    /**
     * Executed when player eats a food item
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class FoodConsume implements Runnable {
        
        private Player player;
        
        @Override
        public void run() {
            OnlineSession session = OnlineSessionCache.fetch(player);
            ((ItemData) session.getDataStore(DataStoreType.Items)).itemUse(player.getLocation(), player.getItemInHand());
            session.getPlayerTotals().snacksEaten();
            session.getPlayersData().getMiscData().incrementStat(PlayerData.FoodEaten);
        }
    }
    
    /**
     * Executed when player crafts an item
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class ItemCraft implements Runnable {
        
        private Player player;
        private Location location;
        private ItemStack itemStack;
        
        @Override
        public void run() {
            OnlineSession session = OnlineSessionCache.fetch(player);
            ((ItemData) session.getDataStore(DataStoreType.Items)).itemCraft(location, itemStack);
            session.getPlayerTotals().itemCraft();
        }
    }
    
    /**
     * Executed when player smelts an item in the furnace
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class ItemSmelt implements Runnable {
        
        private Player player;
        private Location location;
        private ItemStack itemStack;
        
        @Override
        public void run() {
            OnlineSession session = OnlineSessionCache.fetch(player);
            ((ItemData) session.getDataStore(DataStoreType.Items)).itemSmelt(location, itemStack);
        }
    }
    
    /**
     * Executed when player breaks a tool
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class ToolBreak implements Runnable {
        
        private Player player;
        private Location location;
        private ItemStack itemStack;
        
        @Override
        public void run() {
            OnlineSession session = OnlineSessionCache.fetch(player);
            ((ItemData) session.getDataStore(DataStoreType.Items)).itemBreak(location, itemStack);
            session.getPlayerTotals().toolBreak();
        }
    }
    
    /**
     * Executed when player enchants an item
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class ItemEnchant implements Runnable {
        
        private Player player;
        private Location location;
        private ItemStack itemStack;
        
        @Override
        public void run() {
            OnlineSession session = OnlineSessionCache.fetch(player);
            ((ItemData) session.getDataStore(DataStoreType.Items)).itemEnchant(location, itemStack);
        }
    }
    
    /**
     * Executed when player repairs an item
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class ItemRepair implements Runnable {
        
        private Player player;
        private InventoryClickEvent event;
        
        @Override
        public void run() {
            if(!(event.getInventory() instanceof AnvilInventory)) return;
            AnvilInventory anvil = (AnvilInventory) event.getInventory();
            InventoryView view = event.getView();
            int rawSlot = event.getRawSlot();
             
            if(rawSlot != view.convertSlot(rawSlot)) return;
            if(rawSlot != 2) return;
            ItemStack[] items = anvil.getContents();
             
            if(items[0] == null || items[1] == null) return;
            int leftSlot = items[0].getTypeId();
            int rightSlot = items[1].getTypeId();
            if(leftSlot == 0 || leftSlot != rightSlot) return;
            
            ItemStack resultSlot = event.getCurrentItem();
            if(resultSlot == null) return;
                
            ItemMeta meta = resultSlot.getItemMeta();
             
            if(meta == null) return;
            if(!(meta instanceof Repairable)) return;
            Repairable repairable = (Repairable) meta;
            int repairCost = repairable.getRepairCost();
            if(player.getLevel() < repairCost) return;

            OnlineSession session = OnlineSessionCache.fetch(player);
            ((ItemData) session.getDataStore(DataStoreType.Items)).itemRepair(player.getLocation(), resultSlot);
        }
    }
    
}
