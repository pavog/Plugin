/*
 * Statistics.java
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

package com.wolvencraft.yasp;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.data.ServerStatistics;
import com.wolvencraft.yasp.db.totals.ServerTotals;
import com.wolvencraft.yasp.listeners.*;
import com.wolvencraft.yasp.settings.*;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.PatchFetcher;
import com.wolvencraft.yasp.util.cache.*;
import com.wolvencraft.yasp.util.hooks.*;
import com.wolvencraft.yasp.util.tasks.DatabaseTask;
import com.wolvencraft.yasp.util.tasks.RefreshTask;
import com.wolvencraft.yasp.util.tasks.SignRefreshTask;
import com.wolvencraft.yasp.util.tasks.TickTask;
import com.wolvencraft.yasp.util.tasks.SignRefreshTask.StatsSign;

/**
 * <b>Main plugin class</b><br />
 * Establishes a database connection and sets up the event listeners
 * @author bitWolfy
 *
 */
public class Statistics extends JavaPlugin {
    private static Statistics instance;
    private static PluginMetrics metrics;
    
    private static boolean paused;
    private static boolean crashed;
    
    private static Gson gson;
    
    private static VaultHook vaultHook;
    private static WorldGuardHook worldGuardHook;

    private static ServerTotals serverTotals;
    private static ServerStatistics serverStatistics;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new instance of the Statistics.
     */
    public Statistics() {
        instance = this;
        paused = true;
        crashed = false;
        
        gson = new Gson();
    }
    
    @Override
    public void onEnable() {
        
        if(!new File(getDataFolder(), "config.yml").exists()) {
            Message.log("Config.yml not found. Creating a one for you.");
            getConfig().options().copyDefaults(true);
            saveConfig();
            crashed = true;
            this.setEnabled(false);
            return;
        }
        
        if(LocalConfiguration.FetchPatches.asBoolean()) new PatchFetcher();
        new Query();
        
        try { new Database(); }
        catch (Exception e) {
            crashed = true;
            Message.log(Level.SEVERE, "Cannot establish a database connection!");
            Message.log(Level.SEVERE, "Is the plugin set up correctly?");
            if (LocalConfiguration.Debug.asBoolean()) e.printStackTrace();
            this.setEnabled(false);
            return;
        }
        
        Message.log("Database connection established.");
        
        if (getServer().getPluginManager().getPlugin("Vault") != null
         && Module.Vault.isEnabled()) {
            vaultHook = new VaultHook();
            vaultHook.onEnable();
        }
        
        if (getServer().getPluginManager().getPlugin("WorldGuard") != null
         && getServer().getPluginManager().getPlugin("WorldEdit") != null
         && Module.WorldGuard.isEnabled()) {
            worldGuardHook = new WorldGuardHook();
            worldGuardHook.onEnable();
        }

        ConfigurationSerialization.registerClass(StatsSign.class, "StatsSign");
        
        new CommandManager();
        
        new ServerListener(this);
        new PlayerListener(this);
        if(Module.Blocks.isEnabled()) new BlockListener(this);
        if(Module.Items.isEnabled()) new ItemListener(this);
        if(Module.Deaths.isEnabled()) new DeathListener(this);
        new StatsSignListener(this);
        if(isCraftBukkitCompatible()) new StatsBookListener(this);
        
        serverStatistics = new ServerStatistics();
        serverTotals = new ServerTotals();
        
        long ping = RemoteConfiguration.Ping.asInteger() * 20;
        
        try {
            metrics = new PluginMetrics(this);
            if(!metrics.isOptOut()) metrics.start();
        }
        catch (IOException e) { Message.log(Level.SEVERE, "An error occurred while connecting to PluginMetrics"); }
        
        CachedData.startAll();
        
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new DatabaseTask(), (ping / 2), ping);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new RefreshTask(), 0L, 20L);
        
        Bukkit.getScheduler().runTaskTimer(this, new SignRefreshTask(), ping, ping);
        Bukkit.getScheduler().runTaskTimer(this, new TickTask(), 0L, 1L);
    }

    @Override
    public void onDisable() {
        if(crashed) { crashed = false; return; }
        
        try {
            for(Player player : Bukkit.getOnlinePlayers()) {
                OnlineSessionCache.fetch(player).logout(player.getLocation());
            }
            DatabaseTask.commit();
            serverStatistics.pluginShutdown();
            OnlineSessionCache.dumpSessions();
            CachedData.stopAll();
            
            Bukkit.getScheduler().cancelTasks(this);
            
            if(vaultHook != null) { vaultHook.onDisable(); }
            if(worldGuardHook != null) { worldGuardHook.onDisable(); }

            Database.close();
        } catch (Throwable t) { 
            Message.log(Level.SEVERE, t.getMessage());
            if(LocalConfiguration.Debug.asBoolean()) t.printStackTrace();
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return CommandManager.run(sender, args);
    }
    
    /**
     * Returns a static plugin instance
     * @return <b>StatsPlugin</b> instance
     */
    public static Statistics getInstance() {
        return instance;
    }
    
    /**
     * Wraps around a BookUtils method to check if the server's bukkit version differs from the one
     * the plugin was compiled with
     * @return <b>true</b> if it is safe to proceed, <b>false</b> otherwise
     */
    public static boolean isCraftBukkitCompatible() {
        try { com.wolvencraft.yasp.util.BookUtil.isBukkitCompatible(); }
        catch (Throwable t) { return false; }
        return true;
    }
    
    /**
     * Returns the static instance of Gson
     * @return Gson instance
     */
    public static Gson getGson() {
        return gson;
    }
    
    /**
     * Checks if the database synchronization is paused or not
     * @return <b>true</b> if the database synchronization is paused, <b>false</b> otherwise
     */
    public static boolean getPaused() {
        return paused;
    }
    
    /**
     * Sets the synchronization status
     * @param paused <b>true</b> to pause the database synchronization, <b>false</b> to unpause it.
     */
    public static void setPaused(boolean paused) {
        Statistics.paused = paused;
    }
    
    /**
     * Returns the server totals for signs and books
     * @return Server totals
     */
    public static ServerTotals getServerTotals() {
        return serverTotals;
    }
    
    /**
     * Returns the generic server statistics
     * @return ServerStatistics
     */
    public static ServerStatistics getServerStatistics() {
        return serverStatistics;
    }
}
