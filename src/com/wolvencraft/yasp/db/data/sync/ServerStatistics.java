package com.wolvencraft.yasp.db.data.sync;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.QueryUtils.QueryResult;
import com.wolvencraft.yasp.db.tables.Normal.ServerStatsTable;
import com.wolvencraft.yasp.util.TPSTracker;
import com.wolvencraft.yasp.util.Util;

public class ServerStatistics {
	
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
		playersOnline = Bukkit.getServer().getOnlinePlayers().length;
		entitiesCount = 0;
		for(World world : Bukkit.getServer().getWorlds()) entitiesCount += world.getEntities().size();
		
		List<QueryResult> entries = QueryUtils.table(ServerStatsTable.TableName.toString()).select();
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
	private int playersOnline;
	private int entitiesCount;

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
		
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", currentUptime).condition("key", "current_uptime").update(true);
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", totalUptime).condition("key", "total_uptime").update(true);
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", maxPlayersOnline).condition("key", "max_players_online").update(true);
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", maxPlayersOnlineTime).condition("key", "max_players_online_time").update(true);
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", totalMemory).condition("key", "total_memory").update(true);
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", freeMemory).condition("key", "free_memory").update(true);
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", ticksPerSecond).condition("key", "ticks_per_second").update(true);
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", serverTime).condition("key", "server_time").update(true);
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", weather).condition("key", "weather").update(true);
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", weatherDuration).condition("key", "weather_duration").update(true);
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", entitiesCount).condition("key", "entities_count").update(true);
		return true;
	}
	
	public void pushStaticData() {
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", firstStartup).condition("key", "first_startup").update(true);
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", lastStartup).condition("key", "last_startup").update(true);
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", plugins).condition("key", "plugins").update(true);
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", bukkitVersion).condition("key", "bukkit_version").update(true);
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", serverIP).condition("key", "server_ip").update(true);
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", serverPort).condition("key", "server_port").update(true);
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", serverMOTD).condition("key", "server_motd").update(true);

		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value",  maxPlayersAllowed).condition("key", "players_allowed").update(true);
	}
	
	/**
	 * Indicates that the plugin is shutting down and registers the current shutdown time.
	 */
	public void pluginShutdown() {
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", Util.getTimestamp()).condition("key", "last_shutdown").update(true);
	}
	
	/**
	 * Updates the maximum online players count.
	 * @param players Maximum players online
	 */
	public void playerLogin() {
		playersOnline = Bukkit.getOnlinePlayers().length;
		if(playersOnline > maxPlayersOnline) {
			this.maxPlayersOnline = playersOnline;
			this.maxPlayersOnlineTime = Util.getTimestamp();
		}
		
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", playersOnline).condition("key", "players_online").update(true);
	}
	
	public void playerLogout() {
		playersOnline = Bukkit.getOnlinePlayers().length;
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", playersOnline).condition("key", "players_online").update(true);
	}
	
	public void weatherChange(boolean isStorming, int duration) {
		weather = isStorming;
		weatherDuration = duration;
		
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", weather).condition("key", "weather").update(true);
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", weatherDuration).condition("key", "weather_duration").update(true);
	}
	
	public void pluginNumberChange() {
		plugins = Bukkit.getServer().getPluginManager().getPlugins().length;
		QueryUtils.table(ServerStatsTable.TableName.toString()).value("value", plugins).condition("key", "plugins").update(true);
	}
	
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
		values.put("playersOnline", playersOnline);
		values.put("entitiesCount", entitiesCount);
		return values;
	}
}
