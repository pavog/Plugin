/*
 * StatsBookListener
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

import net.minecraft.server.v1_5_R2.NBTTagCompound;
import net.minecraft.server.v1_5_R2.NBTTagList;
import net.minecraft.server.v1_5_R2.NBTTagString;

import org.bukkit.craftbukkit.v1_5_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.BookUtil;

/**
 * Handles StatsBook events.<br />
 * Prone to errors on CraftBukkit updates.
 * @author bitWolfy
 *
 */
public class StatsBookListener implements Listener {
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new instance of the Listener and registers it with the PluginManager
     * @param plugin StatsPlugin instance
     */
    public StatsBookListener(Statistics plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        Message.debug("StatsBookListener loads");
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBookOpen(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_AIR)) return;
        ItemStack craftItem = event.getItem();
        if(craftItem == null || (craftItem.getTypeId() != 387 && craftItem.getTypeId() != 340)) return;
        net.minecraft.server.v1_5_R2.ItemStack item = CraftItemStack.asNMSCopy(craftItem);
        
        NBTTagCompound tags = item.getTag();
        if (tags == null) {
            tags = new NBTTagCompound();
            item.setTag(tags);
        }
        
        String author = tags.getString("author");
        String title = tags.getString("title");
        Player player = event.getPlayer();
        if(!(author.equals("Statistics"))) return;
        Message.debug("Player " + player.getPlayerListName() + " read the book '" + title + "' by " + author);
        String playerName = title.split(" ")[0];
        
        NBTTagList pages = new NBTTagList("pages");
        String[] newPages = BookUtil.getBookPages(playerName);
        
        for(int i = 0; i < newPages.length; i++) {
            pages.add(new NBTTagString("" + i + "", newPages[i]));
        }
        tags.set("pages", pages);
        item.setTag(tags);
        
        player.getInventory().remove(player.getItemInHand());
        ItemStack newItem = CraftItemStack.asBukkitCopy(item);
        player.getInventory().setItemInHand(newItem);
        Message.debug("Refreshed the book contents");
    }
    
}
