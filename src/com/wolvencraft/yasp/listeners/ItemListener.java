/*
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
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.util.Util;

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
	public ItemListener(StatsPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onItemPickup(PlayerPickupItemEvent event) {
		if(StatsPlugin.getPaused()) return;
		Player player = event.getPlayer();
		if(Util.isExempt(player, "item.pickup")) return;
		DataCollector.get(player).items().itemPickUp(player.getLocation(), event.getItem().getItemStack());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onItemDrop(PlayerDropItemEvent event) {
		if(StatsPlugin.getPaused()) return;
		Player player = event.getPlayer();
		if(Util.isExempt(player, "item.drop")) return;
		DataCollector.get(player).items().itemDrop(player.getLocation(), event.getItemDrop().getItemStack());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onFoodConsume(FoodLevelChangeEvent event) {
		if(StatsPlugin.getPaused()) return;
		if(!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		if(Util.isExempt(player, "item.use")) return;
		DataCollector.get(player).items().itemUse(player.getLocation(), player.getItemInHand());
		DataCollector.get(player).player().misc().foodEaten();
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onItemCraft(CraftItemEvent event) {
		if(StatsPlugin.getPaused()) return;
		Player player = (Player) event.getWhoClicked();
		if(Util.isExempt(player, "item.craft")) return;
		DataCollector.get(player).items().itemCraft(player.getLocation(), event.getCurrentItem());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onItemSmelt(FurnaceExtractEvent event) {
		if(StatsPlugin.getPaused()) return;
		Player player = event.getPlayer();
		if(Util.isExempt(player, "item.smelt")) return;
		DataCollector.get(player).items().itemSmelt(player.getLocation(), new ItemStack(event.getItemType()));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onToolBreak(PlayerItemBreakEvent event) {
		if(StatsPlugin.getPaused()) return;
		Player player = event.getPlayer();
		if(Util.isExempt(player, "item.break")) return;
		DataCollector.get(player).items().itemBreak(player.getLocation(), event.getBrokenItem());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onItemEnchant(EnchantItemEvent event) {
		if(StatsPlugin.getPaused()) return;
		Player player = event.getEnchanter();
		if(Util.isExempt(player, "item.enchant")) return;
		DataCollector.get(player).items().itemEnchant(player.getLocation(), new ItemStack(event.getItem().getType()));
	}
}
