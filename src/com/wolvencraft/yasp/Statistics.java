/*
 * StatsPlugin.java
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
import com.wolvencraft.yasp.db.data.hooks.*;
import com.wolvencraft.yasp.listeners.*;
import com.wolvencraft.yasp.scoreboard.ScoreboardAPI;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.PlayerCache;
import com.wolvencraft.yasp.util.StatsSignFactory;
import com.wolvencraft.yasp.util.TPSTracker;
import com.wolvencraft.yasp.util.StatsSignFactory.StatsSign;

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
    
    private static VaultHookFactory vaultHook;
    private static WGHookFactory worldGuardHook;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new instance of the StatsPlugin.
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
        
        new Query();
        
        try { new Database(); }
        catch (Exception e) {
            crashed = true;
            Message.log(Level.SEVERE, "Cannot establish a database connection!");
            Message.log(Level.SEVERE, "Is the plugin set up correctly?");
            if (Settings.LocalConfiguration.Debug.asBoolean()) e.printStackTrace();
            this.setEnabled(false);
            return;
        }
        
        Message.log("Database connection established.");
        
        if (getServer().getPluginManager().getPlugin("Vault") != null && Settings.Modules.HookVault.getEnabled()) {
            vaultHook = new VaultHookFactory();
            vaultHook.onEnable();
        }
        
        if (getServer().getPluginManager().getPlugin("WorldGuard") != null && Settings.Modules.HookWorldGuard.getEnabled()) {
            worldGuardHook = new WGHookFactory();
            worldGuardHook.onEnable();
        }

        ConfigurationSerialization.registerClass(StatsSign.class, "StatsSign");
        
        new ServerListener(this);
        new PlayerListener(this);
        if(Settings.Modules.Blocks.getEnabled()) new BlockListener(this);
        if(Settings.Modules.Items.getEnabled()) new ItemListener(this);
        if(Settings.Modules.Deaths.getEnabled()) new DeathListener(this);
        new StatsSignListener(this);
        if(isCraftBukkitCompatible()) new StatsBookListener(this);

        long ping = Settings.RemoteConfiguration.Ping.asInteger() * 20;
        Message.debug("ping=" + ping);
        
        try {
            metrics = new PluginMetrics(this);
            if(!metrics.isOptOut()) metrics.start();
        }
        catch (IOException e) { Message.log(Level.SEVERE, "An error occurred while connecting to PluginMetrics"); }

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new DataCollector(), (ping / 2), ping);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new PlayerCache(), 0L, 12000L);
        Bukkit.getScheduler().runTaskTimer(this, new StatsSignFactory(), ping, ping);
        Bukkit.getScheduler().runTaskTimer(this, new TPSTracker(), 0, 1);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new ScoreboardAPI(), 0L, 40L);
    }

    @Override
    public void onDisable() {
        Message.log("Plugin shutting down.");
        if(crashed) { instance = null; return; }
        
        try {
            for(Player player : Bukkit.getOnlinePlayers())
                DataCollector.get(player).logout(player.getLocation());
            DataCollector.pushPlayerData();
            DataCollector.getStats().pluginShutdown();
            DataCollector.dumpPlayerData();
            
            Bukkit.getScheduler().cancelTasks(this);
            
            if(vaultHook != null) { vaultHook.onDisable(); }
            if(worldGuardHook != null) { worldGuardHook.onDisable(); }

            Database.cleanup();
        } catch (Throwable t) { 
            Message.log(Level.SEVERE, t.getMessage());
            if(Settings.LocalConfiguration.Debug.asBoolean()) t.printStackTrace();
        }
        instance = null;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(command.getName().equalsIgnoreCase("stats"))) return false;
        CommandManager.setSender(sender);
        
        if(args.length == 0) {
            CommandManager.Help.run();
            CommandManager.resetSender();
            return true;
        }
        
        for(CommandManager cmd : CommandManager.values()) {
            if(cmd.isCommand(args[0])) {
                if(Settings.LocalConfiguration.Debug.asBoolean()) {
                    String argString = "/stats";
                    for (String arg : args) { argString = argString + " " + arg; }
                    Message.log(sender.getName() + ": " + argString);
                }
                
                boolean result = cmd.run(args);
                CommandManager.resetSender();
                return result;
            }
        }
        
        Message.sendFormattedError(sender, "Unknown command");
        CommandManager.resetSender();
        return false;
    }
    
    /**
     * Returns a static plugin instance
     * @return <b>StatsPlugin</b> instance
     */
    public static Statistics getInstance() {
        return instance;
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
}
