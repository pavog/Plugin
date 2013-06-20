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

package com.wolvencraft.yasp.listeners.handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.settings.Constants.StatPerms;
import com.wolvencraft.yasp.settings.Module;
import com.wolvencraft.yasp.settings.RemoteConfiguration;
import com.wolvencraft.yasp.util.hooks.VanishHook;

public class HandlerManager {
    
    public static boolean playerLookup(Player player, ExtraChecks check) {
        if(Statistics.isPaused()) return false;
        
        if(Module.Vanish.isActive()
                && RemoteConfiguration.VanishDisablesTracking.asBoolean()
                && VanishHook.isVanished(player)) return false;
        
        if(player.hasMetadata("NPC")    // XXX Citizens fix
                && player.getMetadata("NPC").get(0).asBoolean()) return false;
        
        if(!check.check(player)) return false;
        
        return true;
    }
    
    public static boolean playerLookup(Player player, StatPerms permission) {
        if(Statistics.isPaused()) return false;
        
        if(Module.Vanish.isActive()
                && RemoteConfiguration.VanishDisablesTracking.asBoolean()
                && VanishHook.isVanished(player)) return false;
        
        if(player.hasMetadata("NPC")    // XXX Citizens fix
                && player.getMetadata("NPC").get(0).asBoolean()) return false;
        
        return permission.has(player);
    }
    
    public static interface ExtraChecks {
        
        public boolean check(Player player);
        
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
