package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
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
	
	public PlayerData (Player player, String playerName, int playerId) {
		this.playerId = playerId;
		this.playerName = playerName;
		
		this.online = true;
		this.expPercent = player.getExp();
		this.expTotal = player.getTotalExperience();
		this.expLevel = player.getLevel();
		this.foodLevel = player.getFoodLevel();
		this.healthLevel = player.getHealth();
		this.firstJoin = Util.getTimestamp();
		this.logins = 0;
	}
	
	private int playerId;
	private String playerName;
	
	private boolean online;
	private double expPercent;
	private int expTotal;
	private int expLevel;
	private int foodLevel;
	private int healthLevel;
	private long firstJoin;
	private int logins;
	
	@Override
	public void fetchData() {
		List<QueryResult> results = QueryUtils.select(
			Players.TableName.toString(),
			new String[] {Players.PlayerId.toString(), Players.Logins.toString()},
			new String[] { Players.PlayerId.toString(), playerId + ""}
		);
		if(results.isEmpty()) QueryUtils.insert(Players.TableName.toString(), getValues());
		else {
			logins = results.get(0).getValueAsInteger(Players.Logins.toString());
		}
	}

	@Override
	public boolean pushData() {
		refreshPlayerData();
		return QueryUtils.update(
			Players.TableName.toString(),
			getValues(), 
			new String[] { Players.PlayerId.toString(), playerId + ""}
		);
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Players.Name.toString(), playerName);
		if(online) map.put(Players.Online.toString(), 1);
		else map.put(Players.Online.toString(), 0);
		map.put(Players.ExperiencePercent.toString(), expPercent);
		map.put(Players.ExperienceTotal.toString(), expTotal);
		map.put(Players.ExperienceLevel.toString(), expLevel);
		map.put(Players.FoodLevel.toString(), foodLevel);
		map.put(Players.HealthLevel.toString(), healthLevel);
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
	 * Fetches the player data from the player, if he is online
	 */
	public void refreshPlayerData() {
		Player player = null;
		for(Player pl : Bukkit.getServer().getOnlinePlayers()) {
			if(pl.getPlayerListName().equals(playerName)) player = pl;
		}
		if(player == null) return;
		
		this.expPercent = player.getExp();
		this.expTotal = player.getTotalExperience();
		this.expLevel = player.getLevel();
		this.foodLevel = player.getFoodLevel();
		this.healthLevel = player.getHealth();
	}
	
	/**
	 * Changes the online status of the player
	 * @param online New online status
	 */
	public void setOnline(boolean online) { this.online = online; }
}
