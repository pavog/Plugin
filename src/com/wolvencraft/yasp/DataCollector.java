package com.wolvencraft.yasp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.DBEntry;
import com.wolvencraft.yasp.db.QueryUtils;

public class DataCollector {

	private static List<LocalSession> sessions;
	private static Map<String, Integer> players = new HashMap<String, Integer>();
	
	public DataCollector() {
		sessions = new ArrayList<LocalSession>();
	}
	
	public static void add(LocalSession newSession) {
		sessions.add(newSession);
	}
	
	public static void add(Player player) {
		try { sessions.add(new LocalSession(player)); }
		catch (Exception e) { e.printStackTrace(); }
	}
	
	public static List<LocalSession> get() {
		List<LocalSession> tempList = new ArrayList<LocalSession>();
		for(LocalSession session : sessions) tempList.add(session);
		return tempList;
	}
	
	public static LocalSession get(int playerId) {
		for(LocalSession session : sessions) {
			if(session.getPlayerId() == playerId) return session;
		}
		return null;
	}
	
	public static LocalSession get(Player player) {
		return get(getCachedPlayerId(player.getPlayerListName()));
	}
	
	public static LocalSession get(String playerName) {
		return get(getCachedPlayerId(playerName));
	}
	
	public static void clear() {
		sessions.clear();
	}
	
	public static void remove(int playerId) {
		sessions.remove(get(playerId));
	}
	
	public static void remove(String playerName) {
		sessions.remove(get(playerName));
	}
	
	public static void remove(Player player) {
		sessions.remove(get(player));
	}
	
	public static void remove(LocalSession session) {
		sessions.remove(session);
	}
	
	public static Integer getCachedPlayerId(String username) {
		Iterator<Entry<String, Integer>> it = players.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>) it.next();
			if(pairs.getKey().equals(username)) return pairs.getValue();
			it.remove();
		}
		int playerId = -1;
		List<DBEntry> results = QueryUtils.fetchData("SELECT name, player_id FROM players WHERE name = '" + username + "'");
		if(results.isEmpty()) {
			QueryUtils.pushData("INSERT name INTO players");
			List<DBEntry> newResults = QueryUtils.fetchData("SELECT name, player_id FROM players WHERE name = '" + username + "'");
			playerId = newResults.get(0).getValueAsInteger("player_id");
		} else playerId = results.get(0).getValueAsInteger("player_id");
		players.put(username, playerId);
		return playerId;
	}
}