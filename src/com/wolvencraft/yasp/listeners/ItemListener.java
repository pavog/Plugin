/*
 * ItemListener.java
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

package com.wolvencraft.yasp.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.listeners.handlers.HandlerManager;
import com.wolvencraft.yasp.listeners.handlers.ItemsHandler.FoodConsume;
import com.wolvencraft.yasp.listeners.handlers.ItemsHandler.ItemCraft;
import com.wolvencraft.yasp.listeners.handlers.ItemsHandler.ItemDrop;
import com.wolvencraft.yasp.listeners.handlers.ItemsHandler.ItemEnchant;
import com.wolvencraft.yasp.listeners.handlers.ItemsHandler.ItemPickup;
import com.wolvencraft.yasp.listeners.handlers.ItemsHandler.ItemRepair;
import com.wolvencraft.yasp.listeners.handlers.ItemsHandler.ItemSmelt;
import com.wolvencraft.yasp.listeners.handlers.ItemsHandler.ToolBreak;
import com.wolvencraft.yasp.settings.Constants.StatPerms;

/**
 * Listens to any item changes on the server and reports them to the plugin.
 * @author bitWolfy
 *
 */
public class ItemListener implements Listener {
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new instance of the Listener and registers it with the PluginManager
     * @param plugin StatsPlugin instance
     */
    public ItemListener(Statistics plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if(!HandlerManager.playerLookup(player, StatPerms.ItemPickUp)) return;
        
        HandlerManager.runAsyncTask(new ItemPickup(player, player.getLocation(), event.getItem().getItemStack()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if(!HandlerManager.playerLookup(player, StatPerms.ItemDrop)) return;

        HandlerManager.runAsyncTask(new ItemDrop(player, player.getLocation(), event.getItemDrop().getItemStack()));
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFoodConsume(FoodLevelChangeEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        
        Player player = (Player) event.getEntity();
        if(!HandlerManager.playerLookup(player, StatPerms.ItemUse)) return;

        HandlerManager.runAsyncTask(new FoodConsume(player));
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemCraft(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(!HandlerManager.playerLookup(player, StatPerms.ItemCraft)) return;

        HandlerManager.runAsyncTask(new ItemCraft(player, player.getLocation(), event.getCurrentItem()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemSmelt(FurnaceExtractEvent event) {
        Player player = event.getPlayer();
        if(!HandlerManager.playerLookup(player, StatPerms.ItemMisc)) return;

        HandlerManager.runAsyncTask(new ItemSmelt(player, player.getLocation(), new ItemStack(event.getItemType())));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onToolBreak(PlayerItemBreakEvent event) {
        Player player = event.getPlayer();
        if(!HandlerManager.playerLookup(player, StatPerms.ItemBreak)) return;

        HandlerManager.runAsyncTask(new ToolBreak(player, player.getLocation(), event.getBrokenItem()));
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemEnchant(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        if(!HandlerManager.playerLookup(player, StatPerms.ItemMisc)) return;

        HandlerManager.runAsyncTask(new ItemEnchant(player, player.getLocation(), event.getItem()));
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public static void onItemRepair(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if(!HandlerManager.playerLookup(player, StatPerms.ItemAnvil)) return;
        

        HandlerManager.runAsyncTask(new ItemRepair(player, event));
    }
    
}
