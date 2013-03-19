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

package com.wolvencraft.yasp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.data.receive.ServerTotals;
import com.wolvencraft.yasp.db.data.sync.ServerStatistics;
import com.wolvencraft.yasp.db.data.sync.Settings;
import com.wolvencraft.yasp.db.tables.Normal.PlayersTable;
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
		serverTotals = new ServerTotals();
		serverStatistics = new ServerStatistics();
		
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(!Util.isExempt(player)) get(player);
		}
	}
	
	private static List<LocalSession> sessions;
	private static ServerStatistics serverStatistics;
	private static ServerTotals serverTotals;

	@Override
	public void run() {
		if(StatsPlugin.getPaused()) return;
		pushAllData();
		serverTotals.fetchData();
		serverStatistics.pushData();
	}
	
	/**
	 * Pushes all data to the database.<br />
	 * This method is run periodically, as well as on plugin shutdown.
	 */
	public static void pushAllData() {
		Message.debug("Database synchronization in progress");
		for(LocalSession session : get()) {
			session.pushData();
			session.playerTotals().fetchData();
			if(!session.isOnline()) remove(session);
		}
	}
	
	public static void dumpAll() {
		for(LocalSession session : get()) {
			session.dump();
		}
		sessions.clear();
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
		String playerName = player.getPlayerListName();
		for(LocalSession session : sessions) {
			if(session.getPlayerName().equals(playerName)) {
				return session;
			}
		}
		Message.debug("Creating a new user session for " + playerName);
		LocalSession newSession = new LocalSession(player);
		sessions.add(newSession);
		if(Settings.RemoteConfiguration.ShowFirstJoinMessages.asBoolean())
			Message.send(
				player,
				Settings.RemoteConfiguration.FirstJoinMessage.asString().replace("<PLAYER>", player.getPlayerListName())
			);
		return newSession;
	}
	
	public static OfflineSession get(String playerName) {
		Message.debug("Fetching an offline session for " + playerName);
		return new OfflineSession(playerName);
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
	 * Returns the PlayerID corresponding with the specified username.<br />
	 * If the username is not in the database, a dummy entry is created, and an ID is assigned.<br />
	 * Unlike <i>getCachedPlayerId(String username)</i>, does not save the username-id pairs locally.
	 * @param player Player name to look up in the database
	 * @return <b>Integer</b> PlayerID corresponding to the specified username
	 */
	public static Integer getPlayerId(Player player) {
		String username = player.getPlayerListName();
		Message.debug("Retrieving a player ID for " + username);
		int playerId = -1;
		List<QueryResult> results;
		
		results = Query.table(PlayersTable.TableName.toString())
			.column(PlayersTable.PlayerId.toString())
			.column(PlayersTable.Name.toString())
			.condition(PlayersTable.Name.toString(), username)
			.select();
		
		if(results.isEmpty()) {
			Query.table(PlayersTable.TableName.toString())
				.value(PlayersTable.Name.toString(), username)
				.insert();
			
			results = Query
				.table(PlayersTable.TableName.toString())
				.column(PlayersTable.PlayerId.toString())
				.column(PlayersTable.Name.toString())
				.condition(PlayersTable.Name.toString(), username)
				.select();
			playerId = results.get(0).getValueAsInteger(PlayersTable.PlayerId.toString());
		} else {
			playerId = results.get(0).getValueAsInteger(PlayersTable.PlayerId.toString());
		}
		
		Message.debug("User ID found: " + playerId);
		return playerId;
	}
	
	public static ServerStatistics getServerStats() {
		return serverStatistics;
	}
	
	public static ServerTotals getServerTotals() {
		return serverTotals;
	}
}