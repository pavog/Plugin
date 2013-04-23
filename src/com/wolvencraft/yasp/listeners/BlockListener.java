/*
 * BlockListener.java
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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.settings.Constants.StatPerms;
import com.wolvencraft.yasp.util.tasks.DatabaseTask;

/**
 * Listens to any block changes on the server and reports them to the plugin.
 * @author bitWolfy
 *
 */
public class BlockListener implements Listener {
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new instance of the Listener and registers it with the PluginManager
     * @param plugin StatsPlugin instance
     */
    public BlockListener(Statistics plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if(Statistics.getPaused()) return;
        Player player = event.getPlayer();
        if(!StatPerms.BlockBreak.has(player)) return;
        DatabaseTask.getSession(player).blockBreak(event.getBlock().getLocation(), event.getBlock().getState());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if(Statistics.getPaused()) return;
        Player player = event.getPlayer();
        if(!StatPerms.BlockPlace.has(player)) return;
        DatabaseTask.getSession(player).blockPlace(event.getBlock().getLocation(), event.getBlock().getState());
    }
}
