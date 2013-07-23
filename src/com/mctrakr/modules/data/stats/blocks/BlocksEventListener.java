/*
 * BlocksEventListener.java
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

package com.mctrakr.modules.data.stats.blocks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.mctrakr.Statistics;
import com.mctrakr.managers.HandlerManager;
import com.mctrakr.modules.data.stats.blocks.BlocksEventHandler.BlockBreak;
import com.mctrakr.modules.data.stats.blocks.BlocksEventHandler.BlockPlace;
import com.mctrakr.settings.Constants.StatPerms;

/**
 * Listens to any block changes on the server and reports them to the plugin.
 * @author bitWolfy
 *
 */
public class BlocksEventListener implements Listener {
    
    public BlocksEventListener() {
        Statistics plugin = Statistics.getInstance();
        if(plugin == null) return;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(!HandlerManager.playerLookup(player, StatPerms.BlockBreak)) return;
        
        HandlerManager.runAsyncTask(new BlockBreak(player, event.getBlock().getState()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if(!HandlerManager.playerLookup(player, StatPerms.BlockPlace)) return;

        HandlerManager.runAsyncTask(new BlockPlace(player, event.getBlock().getState()));
    }
}
