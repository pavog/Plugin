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
import com.wolvencraft.yasp.util.BookUtil;
import com.wolvencraft.yasp.util.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

/**
 * Handles StatsBook events.<br />
 * Prone to errors on CraftBukkit updates.
 *
 * @author bitWolfy
 */
public class StatsBookListener implements Listener {

    /**
     * <b>Default constructor</b><br />
     * Creates a new instance of the Listener and registers it with the PluginManager
     *
     * @param plugin StatsPlugin instance
     */
    public StatsBookListener(Statistics plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        Message.debug("StatsBookListener loads");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBookOpen(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_AIR)) return;
        ItemStack bookStack = event.getItem();
        if (bookStack == null || bookStack.getTypeId() != 387) return;
        BookMeta book = (BookMeta) bookStack.getItemMeta();

        Player player = event.getPlayer();

        if (!book.hasAuthor()) return;
        if (!book.getAuthor().equals("Statistics")) return;
        book.setPages(BookUtil.getBookPages(player.getUniqueId()));

        bookStack.setItemMeta(book);
        player.getInventory().setItemInHand(bookStack);
        Message.debug("Refreshed the book contents");
    }

}
