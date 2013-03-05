package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal.PlayersMisc;

public class PlayerDataMisc implements _NormalData {
	
	public PlayerDataMisc(Player player) {
		this.playerName = player.getPlayerListName();
		
		this.gamemode = 0;
		this.expPercent = player.getExp();
		this.expTotal = player.getTotalExperience();
		this.expLevel = player.getLevel();
		this.foodLevel = player.getFoodLevel();
		this.healthLevel = player.getHealth();
	}
	
	private String playerName;
	
	private int gamemode;
	private double expPercent;
	private int expTotal;
	private int expLevel;
	private int foodLevel;
	private int healthLevel;
	
	@Override
	public void fetchData(int playerId) {
		List<QueryResult> results = QueryUtils.select(
			PlayersMisc.TableName.toString(),
			new String[] {"*"},
			new String[] { PlayersMisc.PlayerId.toString(), playerId + ""}
		);
		if(results.isEmpty()) QueryUtils.insert(PlayersMisc.TableName.toString(), getValues(playerId));
		else {
			// Stuff
		}
	}

	@Override
	public boolean pushData(int playerId) {
		refreshPlayerData();
		return QueryUtils.update(
			PlayersMisc.TableName.toString(),
			getValues(playerId), 
			new String[] { PlayersMisc.PlayerId.toString(), playerId + ""}
		);
	}

	@Override
	public Map<String, Object> getValues(int playerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PlayersMisc.PlayerId.toString(), playerId);
		map.put(PlayersMisc.ExperiencePercent.toString(), expPercent);
		map.put(PlayersMisc.ExperienceTotal.toString(), expTotal);
		map.put(PlayersMisc.ExperienceLevel.toString(), expLevel);
		map.put(PlayersMisc.FoodLevel.toString(), foodLevel);
		map.put(PlayersMisc.HealthLevel.toString(), healthLevel);
		map.put(PlayersMisc.Gamemode.toString(), gamemode);
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
		
		this.gamemode = player.getGameMode().getValue();
		this.expPercent = player.getExp();
		this.expTotal = player.getTotalExperience();
		this.expLevel = player.getLevel();
		this.foodLevel = player.getFoodLevel();
		this.healthLevel = player.getHealth();
	}
}
