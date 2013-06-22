/*
 * ServerStatistics.java
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

package com.wolvencraft.yasp.db.data;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.tables.Miscellaneous.ServerStatsTable;
import com.wolvencraft.yasp.util.Util;
import com.wolvencraft.yasp.util.VariableManager.ServerVariable;
import com.wolvencraft.yasp.util.tasks.TickTask;

/**
 * Data collector that stores server-specific information.
 * @author bitWolfy
 *
 */
public class ServerStatistics {
    
    private long lastSyncTime;
    
    private long firstStartup;
    private long lastStartup;
    private long currentUptime;
    private long totalUptime;
    private long lastShutdown;

    private long totalMemory;
    private long freeMemory;
    private int ticksPerSecond;
    private int availableProcessors;

    private String serverIP;
    private int serverPort;
    private String serverMOTD;
    private String bukkitVersion;
    private int plugins;

    private long serverTime;
    private boolean weather;
    private int weatherDuration;
    
    private int maxPlayersOnline;
    private long maxPlayersOnlineTime;
    
    private int maxPlayersAllowed;
    
    /**
     * <b>Default constructor</b><br />
     * Creates an empty data store to save the statistics until database synchronization.
     */
    public ServerStatistics() {
        long curTime = Util.getTimestamp();
        
        lastSyncTime = curTime;
        
        firstStartup = 0;
        lastStartup = curTime;
        currentUptime = 0;
        totalUptime = 0;
        lastShutdown = curTime;
        
        Runtime runtime = Runtime.getRuntime();
        totalMemory = runtime.totalMemory();
        freeMemory = runtime.freeMemory();
        ticksPerSecond = TickTask.getTicksPerSecond();
        availableProcessors = runtime.availableProcessors();

        serverIP = Bukkit.getIp();
        if(serverIP.equals("")) {
            try {
                InetAddress in = InetAddress.getLocalHost();
                InetAddress[] all = InetAddress.getAllByName(in.getHostName());
                serverIP = all[0].getHostAddress();
            } catch (Throwable t) { serverIP = ""; }
        }
        serverPort = Bukkit.getPort();
        serverMOTD = Bukkit.getMotd();
        bukkitVersion = Bukkit.getBukkitVersion();
        plugins = Bukkit.getServer().getPluginManager().getPlugins().length;

        World mainWorld = Bukkit.getWorlds().get(0);
        serverTime = mainWorld.getTime();
        weather = mainWorld.hasStorm();
        weatherDuration = mainWorld.getWeatherDuration();
        
        maxPlayersOnline = 0;
        maxPlayersOnlineTime = 0;
        maxPlayersAllowed = Bukkit.getMaxPlayers();
        
        List<QueryResult> entries = Query.table(ServerStatsTable.TableName).selectAll();
        for(QueryResult entry : entries) {
            if(entry.asString("key").equalsIgnoreCase("first_startup")) firstStartup = entry.asLong("value");
            else if(entry.asString("key").equalsIgnoreCase("total_uptime")) totalUptime = entry.asLong("value");
            else if(entry.asString("key").equalsIgnoreCase("last_shutdown")) lastShutdown = entry.asLong("value");
            else if(entry.asString("key").equalsIgnoreCase("max_players_online")) maxPlayersOnline = entry.asInt("value");
            else if(entry.asString("key").equalsIgnoreCase("max_players_online_time")) maxPlayersOnlineTime = entry.asLong("value");
        }
        if(firstStartup == 0 || firstStartup == -1) firstStartup = curTime;
        if(lastShutdown == 0 || lastShutdown == -1) lastShutdown = curTime;
                
        pushStaticData();
    }
    
    /**
     * Performs a database operation to push the local data to the remote database.
     * @return <b>true</b> if the insertion was successful, <b>false</b> otherwise
     */
    public boolean pushData() {
        long curTime = Util.getTimestamp();
        currentUptime = curTime - lastStartup;
        totalUptime += (curTime - lastSyncTime);
        lastSyncTime = curTime;
        serverTime = Bukkit.getWorlds().get(0).getFullTime();
        Runtime runtime = Runtime.getRuntime();
        totalMemory = runtime.totalMemory();
        freeMemory = runtime.freeMemory();
        ticksPerSecond = TickTask.getTicksPerSecond();
        
        Query.table(ServerStatsTable.TableName).value("value", currentUptime).condition("key", "current_uptime").update();
        Query.table(ServerStatsTable.TableName).value("value", totalUptime).condition("key", "total_uptime").update();
        Query.table(ServerStatsTable.TableName).value("value", maxPlayersOnline).condition("key", "max_players_online").update();
        Query.table(ServerStatsTable.TableName).value("value", maxPlayersOnlineTime).condition("key", "max_players_online_time").update();
        Query.table(ServerStatsTable.TableName).value("value", freeMemory).condition("key", "free_memory").update();
        Query.table(ServerStatsTable.TableName).value("value", ticksPerSecond).condition("key", "ticks_per_second").update();
        Query.table(ServerStatsTable.TableName).value("value", serverTime).condition("key", "server_time").update();
        Query.table(ServerStatsTable.TableName).value("value", weather).condition("key", "weather").update();
        Query.table(ServerStatsTable.TableName).value("value", weatherDuration).condition("key", "weather_duration").update();
        return true;
    }
    
    /**
     * Performs a one-time database operation to push the local data to the remote database.<br /.
     * Only performed on plugin startup.
     */
    public void pushStaticData() {
        Query.table(ServerStatsTable.TableName).value("value", firstStartup).condition("key", "first_startup").update();
        Query.table(ServerStatsTable.TableName).value("value", lastStartup).condition("key", "last_startup").update();
        Query.table(ServerStatsTable.TableName).value("value", plugins).condition("key", "plugins").update();
        Query.table(ServerStatsTable.TableName).value("value", bukkitVersion).condition("key", "bukkit_version").update();
        Query.table(ServerStatsTable.TableName).value("value", serverIP).condition("key", "server_ip").update();
        Query.table(ServerStatsTable.TableName).value("value", serverPort).condition("key", "server_port").update();
        Query.table(ServerStatsTable.TableName).value("value", serverMOTD).condition("key", "server_motd").update();
        Query.table(ServerStatsTable.TableName).value("value", maxPlayersAllowed).condition("key", "players_allowed").update();
        
        Query.table(ServerStatsTable.TableName).value("value", totalMemory).condition("key", "total_memory").update();
        Query.table(ServerStatsTable.TableName).value("value", availableProcessors).condition("key", "available_processors").update();
        
        Query.table(ServerStatsTable.TableName).value("value", System.getProperty("os.name")).condition("key", "os.name").update();
        Query.table(ServerStatsTable.TableName).value("value", System.getProperty("os.version")).condition("key", "os.version").update();
        Query.table(ServerStatsTable.TableName).value("value", System.getProperty("os.arch")).condition("key", "os.arch").update();

        Query.table(ServerStatsTable.TableName).value("value", System.getProperty("java.version")).condition("key", "java.version").update();
        Query.table(ServerStatsTable.TableName).value("value", System.getProperty("java.vendor")).condition("key", "java.vendor").update();
        Query.table(ServerStatsTable.TableName).value("value", System.getProperty("java.vendor.url")).condition("key", "java.vendor.url").update();

        Query.table(ServerStatsTable.TableName).value("value", System.getProperty("java.vm.vendor")).condition("key", "java.vm.vendor").update();
        Query.table(ServerStatsTable.TableName).value("value", System.getProperty("java.vm.name")).condition("key", "java.vm.name").update();
        Query.table(ServerStatsTable.TableName).value("value", System.getProperty("java.vm.version")).condition("key", "java.vm.version").update();
    }
    
    /**
     * Indicates that the plugin is shutting down and registers the current shutdown time.
     */
    public void pluginShutdown() {
        Query.table(ServerStatsTable.TableName).value("value", Util.getTimestamp()).condition("key", "last_shutdown").update();
        Query.table(ServerStatsTable.TableName).value("value", 0).condition("key", "current_uptime").update();
    }
    
    /**
     * Registers the player login in the server statistics
     */
    public void playerLogin() {
        int playersOnline = Bukkit.getOnlinePlayers().length;
        if(playersOnline > maxPlayersOnline) {
            this.maxPlayersOnline = playersOnline;
            this.maxPlayersOnlineTime = Util.getTimestamp();
        }
    }
    
    /**
     * Registers the weather change in the server statistics
     */
    public void weatherChange(boolean isStorming, int duration) {
        weather = isStorming;
        weatherDuration = duration;
        
        Query.table(ServerStatsTable.TableName).value("value", weather).condition("key", "weather").update();
        Query.table(ServerStatsTable.TableName).value("value", weatherDuration).condition("key", "weather_duration").update();
    }
    
    /**
     * Registers the change in number of plugins in the server statistics
     */
    public void pluginNumberChange() {
        plugins = Bukkit.getServer().getPluginManager().getPlugins().length;
        Query.table(ServerStatsTable.TableName).value("value", plugins).condition("key", "plugins").update();
    }
    
    /**
     * Returns a map of variables and their values to replace variables on signs.
     * @return Map of statistical variables
     */
    public Map<ServerVariable, Object> getValueMap() {
        @SuppressWarnings("serial")
        Map<ServerVariable, Object> values = new HashMap<ServerVariable, Object>()
        {{
            put(ServerVariable.LAST_SYNC, lastSyncTime);
            
            put(ServerVariable.FIRST_STARTUP, firstStartup);
            put(ServerVariable.LAST_STARTUP, lastStartup);
            put(ServerVariable.CURRENT_UPTIME, currentUptime);
            put(ServerVariable.TOTAL_UPTIME, totalUptime);
            put(ServerVariable.LAST_SHUTDOWN, lastShutdown);

            put(ServerVariable.TOTAL_MEMORY, totalMemory);
            put(ServerVariable.FREE_MEMORY, freeMemory);
            put(ServerVariable.TICKS_PER_SECOND, ticksPerSecond);

            put(ServerVariable.SERVER_IP, serverIP);
            put(ServerVariable.SERVER_PORT, serverPort);
            put(ServerVariable.SERVER_MOTD, serverMOTD);
            put(ServerVariable.BUKKIT_VERSION, bukkitVersion);
            put(ServerVariable.PLUGINS_COUNT, plugins);

            put(ServerVariable.SERVER_TIME, serverTime);
            put(ServerVariable.WEATHER, weather);
            put(ServerVariable.WEATHER_DURATION, weatherDuration);
            
            put(ServerVariable.MAX_PLAYERS_ONLINE, maxPlayersOnline);
            put(ServerVariable.MAX_PLAYERS_TIME, maxPlayersOnlineTime);
            
            put(ServerVariable.MAX_PLAYERS_ALLOWED, maxPlayersAllowed);
        }};
        return values;
    }
    
    /**
     * Updates the tickrate to the one specified
     * @param ticksPerSecond Tickrate
     */
    public void updateTPS(int ticksPerSecond) {
        this.ticksPerSecond = ticksPerSecond;
    }
}
