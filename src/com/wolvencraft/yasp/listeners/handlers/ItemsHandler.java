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

import com.wolvencraft.yasp.db.tables.Normal.MiscInfoPlayersTable;
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
            OnlineSessionCache.fetch(player).itemPickUp(location, itemStack);
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
            OnlineSessionCache.fetch(player).itemDrop(location, itemStack);
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
            session.itemUse(player.getLocation(), player.getItemInHand());
            session.addMiscValue(MiscInfoPlayersTable.FoodEaten);
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
            OnlineSessionCache.fetch(player).itemCraft(location, itemStack);
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
            OnlineSessionCache.fetch(player).itemSmelt(location, itemStack);
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
            OnlineSessionCache.fetch(player).itemBreak(location, itemStack);
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
            OnlineSessionCache.fetch(player).itemEnchant(location, itemStack);
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

            OnlineSessionCache.fetch(player).itemEnchant(player.getLocation(), resultSlot);
        }
    }
    
}
