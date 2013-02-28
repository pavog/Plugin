package com.wolvencraft.yasp.db.data.normal;

import java.util.List;
import java.util.Map;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal._ServerStatistics;
import com.wolvencraft.yasp.util.Util;

public class ServerStatistics implements _NormalData {
	
	public ServerStatistics(StatsPlugin plugin) {
		firstStartupTime = 0;
		startupTime = Util.getCurrentTime().getTime();
		currentUptime = 0;
		totalUptime = 0;
		maxPlayersOnline = 0;
		
		fetchData();
	}
	
	private long firstStartupTime;
	private long startupTime;
	private long currentUptime;
	private long totalUptime;
	private int maxPlayersOnline;
	
	@Override
	public void fetchData() {
		List<QueryResult> entries = QueryUtils.select(_ServerStatistics.TableName.toString(), new String[] {"*"});
		for(QueryResult entry : entries) {
			if(entry.getValue("key").equalsIgnoreCase("first_startup")) firstStartupTime = entry.getValueAsLong("value");
			else if(entry.getValue("key").equalsIgnoreCase("total_uptime")) totalUptime = entry.getValueAsLong("value");
			else if(entry.getValue("key").equalsIgnoreCase("max_players_online")) maxPlayersOnline = entry.getValueAsInteger("value");
		}
		
		if(firstStartupTime == 0) firstStartupTime = Util.getCurrentTime().getTime();
	}

	@Override
	public boolean pushData() {
		currentUptime = Util.getCurrentTime().getTime() - startupTime;
		totalUptime += currentUptime;
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
		return true;
	}

	@Override
	public Map<String, Object> getValues() { return null; }
	
	/**
	 * Indicates that the plugin is shutting down and registers the current shutdown time.
	 */
	public void shutdown() {
		QueryUtils.update(
			_ServerStatistics.TableName.toString(),
			"value", Util.getCurrentTime().getTime() + "",
			new String[] {"key", "last_shutdown"}
		);
	}
	
	/**
	 * Updates the maximum online players count.
	 * @param players Maximum players online
	 */
	public void playerLogin(int playersOnline) {
		if(playersOnline > maxPlayersOnline)
		this.maxPlayersOnline = playersOnline;
	}
}
