package com.wolvencraft.yasp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.data.normal.PlayerData;
import com.wolvencraft.yasp.db.data.normal.ServerStatistics;
import com.wolvencraft.yasp.db.data.normal.Settings;
import com.wolvencraft.yasp.db.tables.normal.Players;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.Util;

/**
 * Stores collected statistical data until it can be processed and sent to the database
 * @author bitWolfy
 *
 */
public class DataCollector implements Runnable {

	/**
	 * <b>Default constructor.</b><br />
	 * Initializes an empty list of LocalSessions
	 */
	public DataCollector() {
		sessions = new ArrayList<LocalSession>();
		serverStatistics = new ServerStatistics(StatsPlugin.getInstance());
		
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(!Util.isExempt(player)) get(player);
		}
	}
	
	private static List<LocalSession> sessions;
	private static ServerStatistics serverStatistics;
	private static Map<String, Integer> players = new HashMap<String, Integer>();

	@Override
	public void run() {
		pushAllData();
	}
	
	/**
	 * Pushes all data to the database.<br />
	 * This method is run periodically, as well as on plugin shutdown.
	 */
	public static void pushAllData() {
		Message.debug("Data is being sent to the remote database");
		for(LocalSession session : get()) {
			session.pushData();
			if(!session.isOnline()) remove(session);
		}
		
		serverStatistics.pushData();
	}
	
	/**
	 * Returns all stored sessions
	 * @return List of stored player sessions
	 */
	public static List<LocalSession> get() {
		List<LocalSession> tempList = new ArrayList<LocalSession>();
		for(LocalSession session : sessions) tempList.add(session);
		return tempList;
	}
	
	/**
	 * Returns the LocalSession associated with the specified player.<br />
	 * If no session is found, it will be created.
	 * @param player Tracked player
	 * @return LocalSession associated with the player.
	 */
	public static LocalSession get(Player player) {
		for(LocalSession session : sessions) {
			if(session.getPlayerName().equals(player.getPlayerListName())) {
				Message.debug("Retrieving a user session for " + player.getPlayerListName());
				if(Settings.getWelcomeMessage() != null) Message.send(player, Settings.getWelcomeMessage());
				return session;
			}
		}
		Message.debug("Creating a new user session for " + player.getPlayerListName());
		LocalSession newSession = new LocalSession(player);
		sessions.add(newSession);
		if(Settings.getFirstJoinMessage() != null) Message.send(player, Settings.getFirstJoinMessage());
		return newSession;
	}
	
	/**
	 * Purges the stored sessions list of all data
	 */
	public static void clear() {
		sessions.clear();
	}
	
	/**
	 * Removes the specified session
	 * @param session Session to remove
	 */
	public static void remove(LocalSession session) {
		Message.debug("Removing a user session for " + session.getPlayerName());
		sessions.remove(session);
	}
	
	/**
	 * Returns the playerID of the specified player
	 * @deprecated
	 * @param username Player to look up
	 * @return <b>int</b> playerID
	 */
	public static Integer getCachedPlayerId(String username) {
		Message.debug("Retriving a player ID for " + username);
		Iterator<Entry<String, Integer>> it = players.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>) it.next();
			if(pairs.getKey().equals(username)) return pairs.getValue();
			it.remove();
		}
		int playerId = -1;
		List<QueryResult> results = QueryUtils.select(Players.TableName.toString(), new String[] {Players.Name.toString(), Players.PlayerId.toString()}, new String[] {"name", username} );
		if(results.isEmpty()) {
			QueryUtils.insert(Players.TableName.toString(), PlayerData.getDefaultValues(username));
			List<QueryResult> newResults = QueryUtils.select(Players.TableName.toString(), new String[] {Players.Name.toString(), Players.PlayerId.toString()}, new String[] {"name", username} );
			playerId = newResults.get(0).getValueAsInteger(Players.PlayerId.toString());
		} else playerId = results.get(0).getValueAsInteger(Players.PlayerId.toString());
		players.put(username, playerId);
		Message.debug("User ID found: " + playerId);
		return playerId;
	}
	
	public static Integer getPlayerId(Player player) {
		String username = player.getPlayerListName();
		Message.debug("Retrieving a player ID for " + username);
		int playerId = -1;
		List<QueryResult> results;
		
		results = QueryUtils.select(
			Players.TableName.toString(),
			new String[] {Players.PlayerId.toString(), Players.Name.toString()},
			new String[] {Players.Name.toString(), username}
		);
		
		if(results.isEmpty()) {
			QueryUtils.insert(
				Players.TableName.toString(),
				PlayerData.getDefaultValues(username)
			);
			results = QueryUtils.select(
				Players.TableName.toString(),
				new String[] {Players.PlayerId.toString(), Players.Name.toString()},
				new String[] {Players.Name.toString(), username}
			);
			playerId = results.get(0).getValueAsInteger(Players.PlayerId.toString());
		} else {
			playerId = results.get(0).getValueAsInteger(Players.PlayerId.toString());
		}
		
		Message.debug("User ID found: " + playerId);
		return playerId;
	}
	
	/**
	 * Updates the maximum online players count.<br />
	 * Wraps around the corresponding <b>ServerStatistics</b> method.
	 * @param players Maximum players online
	 */
	public static void updateMaxPlayersOnline(int players) {
		serverStatistics.updateMaxPlayers(players);
	}
	
	/**
	 * Indicates that the plugin is shutting down and registers the current shutdown time.<br />
	 * Wraps around the corresponding <b>ServerStatistics</b> method.
	 */
	public static void pluginShutdown() {
		serverStatistics.shutdown();
	}
}