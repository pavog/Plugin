package com.wolvencraft.yasp.db.data.normal;

import java.util.List;
import java.util.Map;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.DBEntry;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal._ServerStatistics;
import com.wolvencraft.yasp.util.Util;

public class ServerStatistics implements _NormalData {
	
	public ServerStatistics(StatsPlugin plugin) {
		firstStartupTime = 0;
		startupTime = Util.getCurrentTime().getTime();
		shutdownTime = 0;
		currentUptime = 0;
		totalUptime = 0;
		maxPlayersOnline = 0;
		
		fetchData();
	}
	
	private long firstStartupTime;
	private long startupTime;
	private long shutdownTime;
	private long currentUptime;
	private long totalUptime;
	private int maxPlayersOnline;
	
	@Override
	public void fetchData() {
		List<DBEntry> entries = QueryUtils.select(_ServerStatistics.TableName.toString(), "*");
		for(DBEntry entry : entries) {
			if(entry.getValue("key").equalsIgnoreCase("first_startup")) firstStartupTime = entry.getValueAsLong("value");
			else if(entry.getValue("key").equalsIgnoreCase("last_startup")) startupTime = entry.getValueAsLong("value");
			else if(entry.getValue("key").equalsIgnoreCase("last_shutdown")) shutdownTime = entry.getValueAsLong("value");
			else if(entry.getValue("key").equalsIgnoreCase("current_uptime")) currentUptime = entry.getValueAsLong("value");
			else if(entry.getValue("key").equalsIgnoreCase("total_uptime")) totalUptime = entry.getValueAsLong("value");
			else if(entry.getValue("key").equalsIgnoreCase("max_players_online")) maxPlayersOnline = entry.getValueAsInteger("value");
		}
		
		if(firstStartupTime == 0) firstStartupTime = Util.getCurrentTime().getTime();
	}

	@Override
	public boolean pushData() {
		currentUptime = Util.getCurrentTime().getTime() - startupTime;
		totalUptime += currentUptime;
		return
			QueryUtils.update(_ServerStatistics.TableName.toString(), "value", firstStartupTime + "", "key = first_startup") &&
			QueryUtils.update(_ServerStatistics.TableName.toString(), "value", startupTime + "", "key = last_startup") &&  
			QueryUtils.update(_ServerStatistics.TableName.toString(), "value", shutdownTime + "", "key = shutdown_time") && 
			QueryUtils.update(_ServerStatistics.TableName.toString(), "value", currentUptime + "", "key = current_uptime") && 
			QueryUtils.update(_ServerStatistics.TableName.toString(), "value", totalUptime + "", "key = total_uptime") && 
			QueryUtils.update(_ServerStatistics.TableName.toString(), "value", maxPlayersOnline + "", "key = max_players_online");
	}

	@Override
	public Map<String, Object> getValues() { return null; }
	
	/**
	 * Indicates that the plugin is shutting down and registers the current shutdown time.
	 */
	public void shutdown() { shutdownTime = Util.getCurrentTime().getTime(); }
	
	/**
	 * Updates the maximum online players count.
	 * @param players Maximum players online
	 */
	public void updateMaxPlayers(int players) { this.maxPlayersOnline = players; }
}
