package com.wolvencraft.yasp.db.data;

import java.util.List;

import org.bukkit.Bukkit;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal._ServerStatistics;
import com.wolvencraft.yasp.util.Util;

public class ServerStatistics {
	
	public ServerStatistics(StatsPlugin plugin) {
		lastSyncTime = Util.getTimestamp();
		
		firstStartupTime = 0;
		startupTime = Util.getTimestamp();
		currentUptime = 0;
		totalUptime = 0;
		maxPlayersOnline = 0;
		plugins = Bukkit.getServer().getPluginManager().getPlugins().length;
		maxPlayersAllowed = Bukkit.getMaxPlayers();
		bukkitVersion = Bukkit.getBukkitVersion();
		serverIP = Bukkit.getIp();
		serverPort = Bukkit.getPort();
		serverMOTD = Bukkit.getMotd();
		
		fetchData();
		pushStaticData();
	}
	
	private long lastSyncTime;
	
	private long firstStartupTime;
	private long startupTime;
	private long currentUptime;
	private long totalUptime;
	private int maxPlayersOnline;
	private long maxPlayersOnlineTime;
	private int plugins;
	private String bukkitVersion;
	
	private String serverIP;
	private int serverPort;
	private String serverMOTD;
	
	private int maxPlayersAllowed;
	
	public void fetchData() {
		List<QueryResult> entries = QueryUtils.select(_ServerStatistics.TableName.toString(), new String[] {"*"});
		for(QueryResult entry : entries) {
			if(entry.getValue("key").equalsIgnoreCase("first_startup")) firstStartupTime = entry.getValueAsLong("value");
			else if(entry.getValue("key").equalsIgnoreCase("total_uptime")) totalUptime = entry.getValueAsLong("value");
			else if(entry.getValue("key").equalsIgnoreCase("max_players_online")) maxPlayersOnline = entry.getValueAsInteger("value");
		}
		
		if(firstStartupTime == 0) firstStartupTime = Util.getTimestamp();
	}

	public boolean pushData() {
		long curTime = Util.getTimestamp();
		currentUptime = curTime - startupTime;
		totalUptime += (curTime - lastSyncTime);
		lastSyncTime = curTime;
		QueryUtils.update(
			_ServerStatistics.TableName.toString(),
			"value", currentUptime + "",
			new String[] {"key", "current_uptime"}
		);
		QueryUtils.update(
			_ServerStatistics.TableName.toString(),
			"value", totalUptime + "",
			new String[] {"key", "total_uptime"}
		);
		QueryUtils.update(
			_ServerStatistics.TableName.toString(),
			"value", maxPlayersOnline + "",
			new String[] {"key", "max_players_online"}
		);
		QueryUtils.update(
			_ServerStatistics.TableName.toString(),
			"value", maxPlayersOnlineTime + "",
			new String[] {"key", "max_players_online_time"}
		);
		return true;
	}
	
	public void pushStaticData() {
		QueryUtils.update(
			_ServerStatistics.TableName.toString(),
			"value", firstStartupTime + "",
			new String[] {"key", "first_startup"}
		);
		QueryUtils.update(
			_ServerStatistics.TableName.toString(),
			"value", startupTime + "",
			new String[] {"key", "last_startup"}
		);
		QueryUtils.update(
			_ServerStatistics.TableName.toString(),
			"value", plugins + "",
			new String[] {"key", "plugins"}
		);
		QueryUtils.update(
			_ServerStatistics.TableName.toString(),
			"value", bukkitVersion,
			new String[] {"key", "bukkit_version"}
		);
		QueryUtils.update(
			_ServerStatistics.TableName.toString(),
			"value", serverIP,
			new String[] {"key", "server_ip"}
		);
		QueryUtils.update(
			_ServerStatistics.TableName.toString(),
			"value", serverPort + "",
			new String[] {"key", "server_port"}
		);
		QueryUtils.update(
			_ServerStatistics.TableName.toString(),
			"value", serverMOTD,
			new String[] {"key", "server_motd"}
		);
	}
	
	/**
	 * Indicates that the plugin is shutting down and registers the current shutdown time.
	 */
	public void shutdown() {
		QueryUtils.update(
			_ServerStatistics.TableName.toString(),
			"value", Util.getTimestamp() + "",
			new String[] {"key", "last_shutdown"}
		);
	}
	
	/**
	 * Updates the maximum online players count.
	 * @param players Maximum players online
	 */
	public void playerLogin(int playersOnline) {
		if(playersOnline > maxPlayersOnline) {
			this.maxPlayersOnline = playersOnline;
			this.maxPlayersOnlineTime = Util.getTimestamp();
		}
		
		QueryUtils.update(
			_ServerStatistics.TableName.toString(),
			"value", maxPlayersAllowed + "",
			new String[] {"key", "players_allowed"}
		);
		
		QueryUtils.update(
			_ServerStatistics.TableName.toString(),
			"value", playersOnline + "",
			new String[] {"key", "players_online"}
		);
	}
}
