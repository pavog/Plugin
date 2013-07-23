/*
 * HandlerManager.java
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

package com.mctrakr.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.mctrakr.Statistics;
import com.mctrakr.modules.data.hooks.vanish.VanishHook;
import com.mctrakr.settings.ConfigLock.HookType;
import com.mctrakr.settings.Constants.StatPerms;
import com.mctrakr.settings.RemoteConfiguration;

/**
 * Handles the handlers - har har har.
 * In actuality, this is nothing but a bunch of static methods
 * used by the handlers.
 * @author bitWolfy
 *
 */
public class HandlerManager {
    
    /**
     * Checks if the player should be tracked or not.
     * Performs the plugin and hook checkes.
     * @param player Player to look up
     * @return <b>true</b> if the player should be tracked, <b>false</b> otherwise
     */
    public static boolean playerLookup(Player player) {
        if(Statistics.isPaused()) return false;
        
        if(RemoteConfiguration.VanishDisablesTracking.asBoolean()) {
            VanishHook hook = (VanishHook) HookManager.getHook(HookType.Vanish);
            if(hook != null && hook.isVanished(player)) return false;
        }
        
        // Citizens fix - ensures that the spawned NPCs are not tracked
        if(player.hasMetadata("NPC") && player.getMetadata("NPC").get(0).asBoolean()) return false;
        
        return true;
    }
    
    /**
     * Checks if the player should be tracked or not.
     * Identical in functionality to {@link #playerLookup(Player)}, but also checks the permission
     * @param player Player to look up
     * @param StatPerms permission Permission to check for
     * @return <b>true</b> if the player should be tracked, <b>false</b> otherwise
     */
    public static boolean playerLookup(Player player, StatPerms permission) {
        return playerLookup(player) && permission.has(player);
    }
    
    public static BukkitTask runTask(Runnable task) {
        return Bukkit.getScheduler().runTask(Statistics.getInstance(), task);
    }
    
    public static BukkitTask runAsyncTask(Runnable task) {
        return Bukkit.getScheduler().runTaskAsynchronously(Statistics.getInstance(), task);
    }
    
    public static BukkitTask runTaskLater(Runnable task, long delay) {
        return Bukkit.getScheduler().runTaskLater(Statistics.getInstance(), task, delay);
    }
    
    public static BukkitTask runAsyncTaskLater(Runnable task, long delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(Statistics.getInstance(), task, delay);
    }
    
}
