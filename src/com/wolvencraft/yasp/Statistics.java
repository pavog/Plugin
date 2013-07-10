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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.db.PatchManager;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.data.ServerStatistics;
import com.wolvencraft.yasp.db.totals.ServerTotals;
import com.wolvencraft.yasp.listeners.BlockListener;
import com.wolvencraft.yasp.listeners.DeathListener;
import com.wolvencraft.yasp.listeners.ItemListener;
import com.wolvencraft.yasp.listeners.PlayerListener;
import com.wolvencraft.yasp.listeners.ServerListener;
import com.wolvencraft.yasp.listeners.SessionListener;
import com.wolvencraft.yasp.listeners.StatsBookListener;
import com.wolvencraft.yasp.listeners.StatsSignListener;
import com.wolvencraft.yasp.settings.LocalConfiguration;
import com.wolvencraft.yasp.settings.Module;
import com.wolvencraft.yasp.settings.RemoteConfiguration;
import com.wolvencraft.yasp.util.ExceptionHandler;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.cache.CachedData;
import com.wolvencraft.yasp.util.cache.OnlineSessionCache;
import com.wolvencraft.yasp.util.tasks.DatabaseTask;
import com.wolvencraft.yasp.util.tasks.RefreshTask;
import com.wolvencraft.yasp.util.tasks.SignRefreshTask;
import com.wolvencraft.yasp.util.tasks.SignRefreshTask.StatsSign;
import com.wolvencraft.yasp.util.tasks.TickTask;

/**
 * <b>Main plugin class</b><br />
 * Establishes a database connection and sets up the event listeners
 * @author bitWolfy
 *
 */
public class Statistics extends JavaPlugin {
    
    @Getter(AccessLevel.PUBLIC) private static Statistics instance;
    private static PluginMetrics metrics;
    
    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private static boolean paused;
    private static boolean crashed;
    
    @Getter(AccessLevel.PUBLIC) private static Gson gson;
    
    private static HookManager hookManager;

    @Getter(AccessLevel.PUBLIC) private static ServerTotals serverTotals;
    @Getter(AccessLevel.PUBLIC) private static ServerStatistics serverStatistics;
    
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
        
        new PatchManager();
        new Query();
        
        try { new Database(); }
        catch (Exception e) {
            crashed = true;
            Message.log(Level.SEVERE, "Cannot establish a database connection!");
            Message.log(Level.SEVERE, "Is the plugin set up correctly?");
            if (LocalConfiguration.Debug.toBoolean()) e.printStackTrace();
            this.setEnabled(false);
            return;
        }
        
        Message.log("Database connection established.");
        
        hookManager = new HookManager();
        hookManager.onEnable();
        
        serverStatistics = new ServerStatistics();
        serverTotals = new ServerTotals();
        
        ConfigurationSerialization.registerClass(StatsSign.class, "StatsSign");
        
        new CommandManager();

        if(Module.Blocks.isEnabled()) new BlockListener(this);
        if(Module.Deaths.isEnabled()) new DeathListener(this);
        if(Module.Items.isEnabled()) new ItemListener(this);
        new PlayerListener(this);
        new ServerListener(this);
        new SessionListener(this);
        new StatsBookListener(this);
        new StatsSignListener(this);
        
        long ping = RemoteConfiguration.Ping.asInteger() * 20;
        if(ping < (20 * 60)) ping = 20 * 60;
        
        
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
                OnlineSessionCache.fetch(player).getPlayersData().addPlayerLog(player.getLocation(), false);
            }
            DatabaseTask.commit();
            serverStatistics.pluginShutdown();
            OnlineSessionCache.dumpSessions();
            CachedData.stopAll();
            
            Bukkit.getScheduler().cancelTasks(this);
            
            hookManager.onDisable();

            Database.close();
        } catch (Throwable t) { 
            Message.log(Level.SEVERE, t.getMessage());
            if(LocalConfiguration.Debug.toBoolean()) ExceptionHandler.handle(t);
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return CommandManager.run(sender, args);
    }
}
