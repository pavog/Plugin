/*
 * Statistics Bukkit Plugin
 *
 * V2 Copyright (c) 2016 Paul <pavog> Vogel <http://www.paulvogel.me> and contributors.
 * V1 Copyright (c) 2016 bitWolfy <http://www.wolvencraft.com> and contributors.
 * Contributors are: Mario <MarioG1> Gallaun, Christian <Dazzl> Swan, Cory <Coryf88> Finnestad, Crimsonfoxy
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.wolvencraft.yasp.listeners;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.listeners.handlers.HandlerManager;
import com.wolvencraft.yasp.listeners.handlers.ItemsHandler.*;
import com.wolvencraft.yasp.settings.Constants.StatPerms;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 * Listens to any item changes on the server and reports them to the plugin.
 *
 * @author bitWolfy
 */
public class ItemListener implements Listener {

    /**
     * <b>Default constructor</b><br />
     * Creates a new instance of the Listener and registers it with the PluginManager
     *
     * @param plugin StatsPlugin instance
     */
    public ItemListener(Statistics plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public static void onItemRepair(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!HandlerManager.playerLookup(player, StatPerms.ItemAnvil))
            return;


        HandlerManager.runAsyncTask(new ItemRepair(player, event));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (!HandlerManager.playerLookup(player, StatPerms.ItemPickUp))
            return;
        if (event.getItem().getItemStack().getAmount() == 0)
            return;

        HandlerManager.runAsyncTask(new ItemPickup(player, player.getLocation(), event.getItem().getItemStack(), event.getItem().getItemStack().getAmount()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!HandlerManager.playerLookup(player, StatPerms.ItemDrop))
            return;
        if (event.getItemDrop().getItemStack().getAmount() == 0)
            return;

        HandlerManager.runAsyncTask(new ItemDrop(player, player.getLocation(), event.getItemDrop().getItemStack()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (!HandlerManager.playerLookup(player, StatPerms.ItemUse))
            return;

        HandlerManager.runAsyncTask(new ItemConsume(player, event.getItem()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemCraft(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!HandlerManager.playerLookup(player, StatPerms.ItemCraft))
            return;

        //The metod of tracken the amount of craftet items in bukkit is returns wrong amount of items on shift + klick an workaround for this would be to complicated at the moment
        HandlerManager.runAsyncTask(new ItemCraft(player, player.getLocation(), event.getCurrentItem()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemSmelt(FurnaceExtractEvent event) {
        Player player = event.getPlayer();
        if (!HandlerManager.playerLookup(player, StatPerms.ItemMisc))
            return;

        //Disabled for the mombent because the FurnaceExtractEvent is fired twice on shift + click in the same tick wich causes dublicate database entries
        //HandlerManager.runAsyncTask(new ItemSmelt(player, player.getLocation(), new ItemStack(event.getItemType())));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onToolBreak(PlayerItemBreakEvent event) {
        Player player = event.getPlayer();
        if (!HandlerManager.playerLookup(player, StatPerms.ItemBreak))
            return;

        HandlerManager.runAsyncTask(new ToolBreak(player, player.getLocation(), event.getBrokenItem()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemEnchant(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        if (!HandlerManager.playerLookup(player, StatPerms.ItemMisc))
            return;

        HandlerManager.runAsyncTask(new ItemEnchant(player, player.getLocation(), event.getItem()));
    }

}
