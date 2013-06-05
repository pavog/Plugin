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
                && VanishHook.isVanished(player.getName())) return false;
        
        if(!check.check(player)) return false;
        
        return true;
    }
    
    public static boolean playerLookup(Player player, StatPerms permission) {
        if(Statistics.isPaused()) return false;
        
        if(Module.Vanish.isActive()
                && RemoteConfiguration.VanishDisablesTracking.asBoolean()
                && VanishHook.isVanished(player.getName())) return false;
        
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
