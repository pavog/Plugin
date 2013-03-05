package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal.Players;
import com.wolvencraft.yasp.util.Util;

/**
 * Represents the Player data that is being tracked.<br />
 * Each entry must have a unique player name.
 * @author bitWolfy
 *
 */
public class PlayerData implements _NormalData {
	
	public PlayerData (Player player) {
		this.playerName = player.getPlayerListName();
		
		this.online = true;
		this.sessionStart = Util.getTimestamp();
		this.firstJoin = Util.getTimestamp();
		this.logins = 0;
	}
	
	private String playerName;
	
	private boolean online;
	private long sessionStart;
	private long firstJoin;
	private int logins;
	
	@Override
	public void fetchData(int playerId) {
		List<QueryResult> results = QueryUtils.select(
			Players.TableName.toString(),
			new String[] {Players.PlayerId.toString(), Players.Logins.toString()},
			new String[] { Players.PlayerId.toString(), playerId + ""}
		);
		if(results.isEmpty()) QueryUtils.insert(Players.TableName.toString(), getValues(playerId));
		else {
			logins = results.get(0).getValueAsInteger(Players.Logins.toString());
		}
	}

	@Override
	public boolean pushData(int playerId) {
		return QueryUtils.update(
			Players.TableName.toString(),
			getValues(playerId), 
			new String[] { Players.PlayerId.toString(), playerId + ""}
		);
	}

	@Override
	public Map<String, Object> getValues(int playerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Players.PlayerId.toString(), playerId);
		map.put(Players.Name.toString(), playerName);
		if(online) map.put(Players.Online.toString(), 1);
		else map.put(Players.Online.toString(), 0);
		map.put(Players.SessionStart.toString(), sessionStart);
		map.put(Players.FirstLogin.toString(), firstJoin);
		map.put(Players.Logins.toString(), logins);
		return map;
	}
	
	public static Map<String, Object> getDefaultValues(String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Players.Name.toString(), name);
		return map;
	}
	
	/**
	 * Returns the player name
	 * @return Player name
	 */
	public String getName() { return playerName; }
	
	/**
	 * Changes the online status of the player
	 * @param online New online status
	 */
	public void setOnline(boolean online) { this.online = online; }
}
