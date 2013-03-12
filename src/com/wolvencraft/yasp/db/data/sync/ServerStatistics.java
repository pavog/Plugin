package com.wolvencraft.yasp.db.data.sync;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Normal.ServerStatisticsTable;
import com.wolvencraft.yasp.util.TPSTracker;
import com.wolvencraft.yasp.util.Util;

public class ServerStatistics {
	
	public ServerStatistics(StatsPlugin plugin) {
		lastSyncTime = Util.getTimestamp();
		
		firstStartup = Util.getTimestamp();
		lastStartup = Util.getTimestamp();
		currentUptime = 0;
		totalUptime = 0;
		lastShutdown = Util.getTimestamp();
		
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
		
		fetchData();
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
	
	public void fetchData() {
		List<QueryResult> entries = QueryUtils.select(ServerStatisticsTable.TableName.toString(), new String[] {"*"});
		for(QueryResult entry : entries) {
			if(entry.getValue("key").equalsIgnoreCase("first_startup")) firstStartup = entry.getValueAsLong("value");
			else if(entry.getValue("key").equalsIgnoreCase("total_uptime")) totalUptime = entry.getValueAsLong("value");
			else if(entry.getValue("key").equalsIgnoreCase("last_shutdown")) lastShutdown = entry.getValueAsLong("value");
			else if(entry.getValue("key").equalsIgnoreCase("max_players_online")) maxPlayersOnline = entry.getValueAsInteger("value");
			else if(entry.getValue("key").equalsIgnoreCase("max_players_online_time")) maxPlayersOnlineTime = entry.getValueAsLong("value");
		}
		
		if(firstStartup == 0) firstStartup = Util.getTimestamp();
	}

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
		
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", currentUptime + "", new String[] {"key", "current_uptime"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", totalUptime + "", new String[] {"key", "total_uptime"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", maxPlayersOnline + "", new String[] {"key", "max_players_online"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", maxPlayersOnlineTime + "", new String[] {"key", "max_players_online_time"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", totalMemory + "", new String[] {"key", "total_memory"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", freeMemory + "", new String[] {"key", "free_memory"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", ticksPerSecond + "", new String[] {"key", "ticks_per_second"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", serverTime + "", new String[] {"key", "server_time"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", weather + "", new String[] {"key", "weather"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", weatherDuration + "", new String[] {"key", "weather_duration"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", entitiesCount + "", new String[] {"key", "entities_count"} );
		return true;
	}
	
	public void pushStaticData() {
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", firstStartup + "", new String[] {"key", "first_startup"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", lastStartup + "", new String[] {"key", "last_startup"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", plugins + "", new String[] {"key", "plugins"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", bukkitVersion, new String[] {"key", "bukkit_version"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", serverIP, new String[] {"key", "server_ip"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", serverPort + "", new String[] {"key", "server_port"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", serverMOTD, new String[] {"key", "server_motd"} );

		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", maxPlayersAllowed + "", new String[] {"key", "players_allowed"} );
	}
	
	/**
	 * Indicates that the plugin is shutting down and registers the current shutdown time.
	 */
	public void pluginShutdown() {
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", Util.getTimestamp() + "", new String[] {"key", "last_shutdown"} );
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
		
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", playersOnline + "", new String[] {"key", "players_online"} );
	}
	
	public void playerLogout() {
		playersOnline = Bukkit.getOnlinePlayers().length;
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", playersOnline + "", new String[] {"key", "players_online"} );
	}
	
	public void weatherChange(boolean isStorming, int duration) {
		weather = isStorming;
		weatherDuration = duration;
		
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", weather + "", new String[] {"key", "weather"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", weatherDuration + "", new String[] {"key", "weather_duration"} );
	}
	
	public void pluginNumberChange() {
		plugins = Bukkit.getServer().getPluginManager().getPlugins().length;
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", plugins + "", new String[] {"key", "plugins"} );
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
