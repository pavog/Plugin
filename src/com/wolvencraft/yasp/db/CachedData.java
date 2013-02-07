package com.wolvencraft.yasp.db;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CachedData {
	
	private static Map<String, Integer> players = new HashMap<String, Integer>();
	
	public static Integer getCachedPlayerId(String username) {
		Iterator<Entry<String, Integer>> it = players.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>) it.next();
			if(pairs.getKey().equals(username)) return pairs.getValue();
			it.remove();
		}
		int playerId = -1;
		List<DBEntry> results = QueryUtils.fetchData("SELECT name, player_id FROM players WHERE name = '" + username + "'");
		if(results.isEmpty()) return -1;
		playerId = results.get(0).getValueAsInteger("player_id");
		players.put(username, playerId);
		return playerId;
	}
	
}
