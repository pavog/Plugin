/*
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

package com.wolvencraft.yasp.db.data.sync;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.tables.Normal.ServerStatsTable;
import com.wolvencraft.yasp.util.TPSTracker;
import com.wolvencraft.yasp.util.Util;

/**
 * Data collector that stores server-specific information.
 * @author bitWolfy
 *
 */
public class ServerStatistics {
	
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
		ticksPerSecond = TPSTracker.getTicksPerSecond();

		serverIP = Bukkit.getIp();
		serverPort = Bukkit.getPort();
		serverMOTD = Bukkit.getMotd();
		bukkitVersion = Bukkit.getBukkitVersion();
		plugins = Bukkit.getServer().getPluginManager().getPlugins().length;

		World mainWorld = Bukkit.getWorlds().get(0);
		serverTime = mainWorld.getFullTime();
		weather = mainWorld.hasStorm();
		weatherDuration = mainWorld.getWeatherDuration();
		
		maxPlayersOnline = 0;
		maxPlayersOnlineTime = 0;
		maxPlayersAllowed = Bukkit.getMaxPlayers();
		entitiesCount = 0;
		for(World world : Bukkit.getServer().getWorlds()) entitiesCount += world.getEntities().size();
		
		List<QueryResult> entries = Query.table(ServerStatsTable.TableName.toString()).selectAll();
		for(QueryResult entry : entries) {
			if(entry.getValue("key").equalsIgnoreCase("first_startup")) firstStartup = entry.getValueAsLong("value");
			else if(entry.getValue("key").equalsIgnoreCase("total_uptime")) totalUptime = entry.getValueAsLong("value");
			else if(entry.getValue("key").equalsIgnoreCase("last_shutdown")) lastShutdown = entry.getValueAsLong("value");
			else if(entry.getValue("key").equalsIgnoreCase("max_players_online")) maxPlayersOnline = entry.getValueAsInteger("value");
			else if(entry.getValue("key").equalsIgnoreCase("max_players_online_time")) maxPlayersOnlineTime = entry.getValueAsLong("value");
		}
		if(firstStartup == 0) firstStartup = curTime;
		if(lastShutdown == 0) lastShutdown = curTime;
				
		pushStaticData();
	}
	
	private long lastSyncTime;
	
	private long firstStartup;
	private long lastStartup;
	private long currentUptime;
	private long totalUptime;
	private long lastShutdown;

	private long totalMemory;
	private long freeMemory;
	private int ticksPerSecond;

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
	private int entitiesCount;
	
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
		entitiesCount = 0;
		for(World world : Bukkit.getServer().getWorlds()) entitiesCount += world.getEntities().size();
		Runtime runtime = Runtime.getRuntime();
		totalMemory = runtime.totalMemory();
		freeMemory = runtime.freeMemory();
		ticksPerSecond = TPSTracker.getTicksPerSecond();
		
		Query.table(ServerStatsTable.TableName.toString()).value("value", currentUptime).condition("key", "current_uptime").update();
		Query.table(ServerStatsTable.TableName.toString()).value("value", totalUptime).condition("key", "total_uptime").update();
		Query.table(ServerStatsTable.TableName.toString()).value("value", maxPlayersOnline).condition("key", "max_players_online").update();
		Query.table(ServerStatsTable.TableName.toString()).value("value", maxPlayersOnlineTime).condition("key", "max_players_online_time").update();
		Query.table(ServerStatsTable.TableName.toString()).value("value", totalMemory).condition("key", "total_memory").update();
		Query.table(ServerStatsTable.TableName.toString()).value("value", freeMemory).condition("key", "free_memory").update();
		Query.table(ServerStatsTable.TableName.toString()).value("value", ticksPerSecond).condition("key", "ticks_per_second").update();
		Query.table(ServerStatsTable.TableName.toString()).value("value", serverTime).condition("key", "server_time").update();
		Query.table(ServerStatsTable.TableName.toString()).value("value", weather).condition("key", "weather").update();
		Query.table(ServerStatsTable.TableName.toString()).value("value", weatherDuration).condition("key", "weather_duration").update();
		Query.table(ServerStatsTable.TableName.toString()).value("value", entitiesCount).condition("key", "entities_count").update();
		return true;
	}
	
	/**
	 * Performs a one-time database operation to push the local data to the remote database.<br /.
	 * Only performed on plugin startup.
	 */
	public void pushStaticData() {
		Query.table(ServerStatsTable.TableName.toString()).value("value", firstStartup).condition("key", "first_startup").update();
		Query.table(ServerStatsTable.TableName.toString()).value("value", lastStartup).condition("key", "last_startup").update();
		Query.table(ServerStatsTable.TableName.toString()).value("value", plugins).condition("key", "plugins").update();
		Query.table(ServerStatsTable.TableName.toString()).value("value", bukkitVersion).condition("key", "bukkit_version").update();
		Query.table(ServerStatsTable.TableName.toString()).value("value", serverIP).condition("key", "server_ip").update();
		Query.table(ServerStatsTable.TableName.toString()).value("value", serverPort).condition("key", "server_port").update();
		Query.table(ServerStatsTable.TableName.toString()).value("value", serverMOTD).condition("key", "server_motd").update();
		Query.table(ServerStatsTable.TableName.toString()).value("value",  maxPlayersAllowed).condition("key", "players_allowed").update();
	}
	
	/**
	 * Indicates that the plugin is shutting down and registers the current shutdown time.
	 */
	public void pluginShutdown() {
		Query.table(ServerStatsTable.TableName.toString()).value("value", Util.getTimestamp()).condition("key", "last_shutdown").update();
		Query.table(ServerStatsTable.TableName.toString()).value("value", 0).condition("key", "current_uptime").update();
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
		
		Query.table(ServerStatsTable.TableName.toString()).value("value", weather).condition("key", "weather").update();
		Query.table(ServerStatsTable.TableName.toString()).value("value", weatherDuration).condition("key", "weather_duration").update();
	}
	
	/**
	 * Registers the change in number of plugins in the server statistics
	 */
	public void pluginNumberChange() {
		plugins = Bukkit.getServer().getPluginManager().getPlugins().length;
		Query.table(ServerStatsTable.TableName.toString()).value("value", plugins).condition("key", "plugins").update();
	}
	
	/**
	 * Returns a map of variables and their values to replace variables on signs.
	 * @return Map of statistical variables
	 */
	public Map<String, Object> getValueMap() {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("lastSyncTime", lastSyncTime);
		
		values.put("firstStartup", firstStartup);
		values.put("lastStartup", lastStartup);
		values.put("currentUptime", currentUptime);
		values.put("totalUptime", totalUptime);
		values.put("lastShutdown", lastShutdown);

		values.put("totalMemory", totalMemory);
		values.put("freeMemory", freeMemory);
		values.put("ticksPerSecond", ticksPerSecond);

		values.put("serverIP", serverIP);
		values.put("serverPort", serverPort);
		values.put("serverMOTD", serverMOTD);
		values.put("bukkitVersion", bukkitVersion);
		values.put("plugins", plugins);

		values.put("serverTime", serverTime);
		values.put("weather", weather);
		values.put("weatherDuration", weatherDuration);
		
		values.put("maxPlayersOnline", maxPlayersOnline);
		values.put("maxPlayersOnlineTime", maxPlayersOnlineTime);
		
		values.put("maxPlayersAllowed", maxPlayersAllowed);
		values.put("entitiesCount", entitiesCount);
		return values;
	}
}
