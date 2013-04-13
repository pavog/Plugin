/*
 * StatsSignListener.java
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

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.tasks.SignRefreshTask;

/**
 * Handles StatsSign events
 * @author bitWolfy
 *
 */
public class StatsSignListener implements Listener {
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new instance of the Listener and registers it with the PluginManager
     * @param plugin StatsPlugin instance
     */
    public StatsSignListener(Statistics plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onStatsSignInit(PlayerInteractEvent event) {
        if(!event.getPlayer().isOp()) return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        
        Block block = event.getClickedBlock();
        if(!(block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST)) return;
        if(!(block.getState() instanceof Sign)) return;
        
        Sign sign = (Sign) block.getState();
        if(SignRefreshTask.isValid(sign)) {
            Message.debug("Stats sign found at the location!");
            return;
        }
        
        if(!sign.getLines()[0].startsWith("<Y>")) return;
        SignRefreshTask.add(sign);
        Message.sendFormattedSuccess(CommandManager.getSender(), "A new StatsSign has been added");
        SignRefreshTask.updateAll();
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onStatsSignBreak(BlockBreakEvent event) {
        BlockState blockState = event.getBlock().getState();
        if(!(blockState instanceof Sign)) return;
        Sign sign = (Sign) blockState;
        if(!SignRefreshTask.isValid(sign)) return;
        Message.debug("Stats sign found at the location!");
        SignRefreshTask.remove(sign);
        Message.sendFormattedSuccess(event.getPlayer(), "Sign successfully removed");
    }
    
}
