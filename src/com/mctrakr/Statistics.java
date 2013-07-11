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

package com.mctrakr;

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

import com.mctrakr.cache.SessionCache;
import com.mctrakr.database.Database;
import com.mctrakr.database.PatchManager;
import com.mctrakr.listeners.BlockListener;
import com.mctrakr.listeners.DeathListener;
import com.mctrakr.listeners.ItemListener;
import com.mctrakr.listeners.PlayerListener;
import com.mctrakr.listeners.ServerListener;
import com.mctrakr.listeners.SessionListener;
import com.mctrakr.listeners.StatsBookListener;
import com.mctrakr.listeners.StatsSignListener;
import com.mctrakr.managers.CacheManager;
import com.mctrakr.managers.CommandManager;
import com.mctrakr.managers.HookManager;
import com.mctrakr.managers.ModuleManager;
import com.mctrakr.modules.stats.player.PlayerDataStore;
import com.mctrakr.modules.stats.server.ServerStatistics;
import com.mctrakr.modules.totals.ServerTotals;
import com.mctrakr.settings.LocalConfiguration;
import com.mctrakr.settings.RemoteConfiguration;
import com.mctrakr.settings.ConfigLock.PrimaryType;
import com.mctrakr.util.ExceptionHandler;
import com.mctrakr.util.Message;
import com.mctrakr.util.tasks.DatabaseProcess;
import com.mctrakr.util.tasks.ScoreboardProcess;
import com.mctrakr.util.tasks.SignProcess;
import com.mctrakr.util.tasks.SignProcess.StatsSign;
import com.mctrakr.util.tasks.TickProcess;

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
        
        // Starting up the plugin
        Message.log(
                "+-------------[ Statistics ]-------------+",
                "| [+] Statistics starting up             |"
                );
        
        // Check if the config file is present
        if(!new File(getDataFolder(), "config.yml").exists()) {
            Message.log(
                    "| [+] Could not find a valid config file |",
                    "|     Creating one for you.              |",
                    "+----------------------------------------+"
                    );
            getConfig().options().copyDefaults(true);
            saveConfig();
            crashed = true;
            this.setEnabled(false);
            return;
        }
        
        // Attempt to connect to the database
        try {
            Message.log("| [+] Connecting to the database         |");
            new Database();
        } catch(Throwable t) {
            Message.log(
                    "| [-] Could not establish a connection!  |",
                    "|     Is the plugin configuration valid? |",
                    "+----------------------------------------+",
                    ""
                    );
            ExceptionHandler.handle(t);
            crashed = true;
            this.setEnabled(false);
            return;
        }
        
        // Fetch patches from the jarfile
        Message.log(
                "| [+] Database connection established    |",
                "| [+] Copying database patches           |"
                );
        PatchManager.fetch(PatchManager.PATCH_KEY);
        
        // Attempt to patch the database
        try {
            Database.patchDatabase(false);
            Message.log(
                    "| [+] The database is up to date!        |"
                    );
        } catch (Throwable t) {
            Message.log(
                    "| [-] An error occurred while patching   |",
                    "+----------------------------------------+",
                    ""
                    );
            ExceptionHandler.handle(t);
            crashed = true;
            this.setEnabled(false);
            return;
        }
        
        // Start up the managers
        new HookManager();
        new ModuleManager();
        new CommandManager();
        
        // Starting up some generic server stats
        serverStatistics = new ServerStatistics();
        serverTotals = new ServerTotals();
        
        // Register serializable classes
        ConfigurationSerialization.registerClass(StatsSign.class, "StatsSign");
        
        // Start up event listeners
        new BlockListener(this);
        new DeathListener(this);
        new ItemListener(this);
        new PlayerListener(this);
        new ServerListener(this);
        new SessionListener(this);
        new StatsBookListener(this);
        new StatsSignListener(this);
        
        // XXX Bogus fix - sometimes, ping ends up being 0 for whatever reason
        long ping = RemoteConfiguration.Ping.asInteger() * 20;
        if(ping < 600) ping = 600;
        
        // Start up plugin metrics
        try {
            metrics = new PluginMetrics(this);
            if(!metrics.isOptOut()) metrics.start();
        } catch (IOException e) { }

        // Creating and starting up data caches
        new CacheManager();
        RemoteConfiguration.clearCache();
        CacheManager.startAll();
        
        // Starting up event tasks
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new DatabaseProcess(), (ping / 2), ping);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new ScoreboardProcess(), 20L, 20L);
        
        Bukkit.getScheduler().runTaskTimer(this, new SignProcess(), ping, ping);
        Bukkit.getScheduler().runTaskTimer(this, new TickProcess(), 0L, 1L);
        
        // Unpause the statistics
        Statistics.setPaused(false);
        Message.log(
                "| [+] Statistics is now running          |",
                "+----------------------------------------+"
                );
    }

    @Override
    public void onDisable() {
        if(crashed) { crashed = false; return; }
        
        try {
            for(Player player : Bukkit.getOnlinePlayers()) {
                ((PlayerDataStore) SessionCache.fetch(player).getDataStore(PrimaryType.Player)).addPlayerLog(player.getLocation(), false);
            }
            DatabaseProcess.commit();
            serverStatistics.pluginShutdown();
            SessionCache.dumpSessions();
            
            CacheManager.clearCache();
            CacheManager.stopAll();
            
            Bukkit.getScheduler().cancelTasks(this);
            
            HookManager.onDisable();

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
