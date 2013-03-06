package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Normal;

public class PlayerDataMisc implements _NormalData {
	
	public PlayerDataMisc(Player player) {
		this.playerName = player.getPlayerListName();
		
		this.gamemode = 0;
		this.expPercent = player.getExp();
		this.expTotal = player.getTotalExperience();
		this.expLevel = player.getLevel();
		this.foodLevel = player.getFoodLevel();
		this.healthLevel = player.getHealth();
		
		this.fishCaught = 0;
		this.timesKicked = 0;
		this.eggsThrown = 0;
		this.foodEaten = 0;
		this.arrowsShot = 0;
		this.damageTaken = 0;
		this.wordsSaid = 0;
		this.commandsSent = 0;
	}
	
	private String playerName;
	
	private int gamemode;
	private double expPercent;
	private int expTotal;
	private int expLevel;
	private int foodLevel;
	private int healthLevel;
	
	private int fishCaught;
	private int timesKicked;
	private int eggsThrown;
	private int foodEaten;
	private int arrowsShot;
	private int damageTaken;
	private int bedsEntered;
	private int portalsEntered;
	
	private int wordsSaid;
	private int commandsSent;
	
	@Override
	public void fetchData(int playerId) {
		List<QueryResult> results = QueryUtils.select(
			Normal.PlayersMisc.TableName.toString(),
			new String[] {"*"},
			new String[] { Normal.PlayersMisc.PlayerId.toString(), playerId + ""}
		);
		if(results.isEmpty()) QueryUtils.insert(Normal.PlayersMisc.TableName.toString(), getValues(playerId));
		else {
			fishCaught = results.get(0).getValueAsInteger(Normal.PlayersMisc.FishCaught.toString());
			timesKicked = results.get(0).getValueAsInteger(Normal.PlayersMisc.TimesKicked.toString());
			eggsThrown = results.get(0).getValueAsInteger(Normal.PlayersMisc.EggsThrown.toString());
			foodEaten = results.get(0).getValueAsInteger(Normal.PlayersMisc.FoodEaten.toString());
			arrowsShot = results.get(0).getValueAsInteger(Normal.PlayersMisc.ArrowsShot.toString());
			damageTaken = results.get(0).getValueAsInteger(Normal.PlayersMisc.DamageTaken.toString());
			bedsEntered = results.get(0).getValueAsInteger(Normal.PlayersMisc.BedsEntered.toString());
			portalsEntered = results.get(0).getValueAsInteger(Normal.PlayersMisc.PortalsEntered.toString());
			wordsSaid = results.get(0).getValueAsInteger(Normal.PlayersMisc.WordsSaid.toString());
			commandsSent = results.get(0).getValueAsInteger(Normal.PlayersMisc.CommandsSent.toString());
		}
	}

	@Override
	public boolean pushData(int playerId) {
		refreshPlayerData();
		return QueryUtils.update(
			Normal.PlayersMisc.TableName.toString(),
			getValues(playerId), 
			new String[] { Normal.PlayersMisc.PlayerId.toString(), playerId + ""}
		);
	}

	@Override
	public Map<String, Object> getValues(int playerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Normal.PlayersMisc.PlayerId.toString(), playerId);
		map.put(Normal.PlayersMisc.ExperiencePercent.toString(), expPercent);
		map.put(Normal.PlayersMisc.ExperienceTotal.toString(), expTotal);
		map.put(Normal.PlayersMisc.ExperienceLevel.toString(), expLevel);
		map.put(Normal.PlayersMisc.FoodLevel.toString(), foodLevel);
		map.put(Normal.PlayersMisc.HealthLevel.toString(), healthLevel);
		map.put(Normal.PlayersMisc.Gamemode.toString(), gamemode);
		
		map.put(Normal.PlayersMisc.FishCaught.toString(), fishCaught);
		map.put(Normal.PlayersMisc.TimesKicked.toString(), timesKicked);
		map.put(Normal.PlayersMisc.EggsThrown.toString(), eggsThrown);
		map.put(Normal.PlayersMisc.FoodEaten.toString(), foodEaten);
		map.put(Normal.PlayersMisc.ArrowsShot.toString(), arrowsShot);
		map.put(Normal.PlayersMisc.DamageTaken.toString(), damageTaken);
		map.put(Normal.PlayersMisc.BedsEntered.toString(), bedsEntered);
		map.put(Normal.PlayersMisc.PortalsEntered.toString(), portalsEntered);
		
		map.put(Normal.PlayersMisc.WordsSaid.toString(), wordsSaid);
		map.put(Normal.PlayersMisc.CommandsSent.toString(), commandsSent);
		
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
	
	public void fishCaught() {
		fishCaught++;
	}
	
	public void kicked() {
		timesKicked++;
	}
	
	public void eggThrown() {
		eggsThrown++;
	}
	
	public void foodEaten() {
		foodEaten++;
	}
	
	public void arrowShot() {
		arrowsShot++;
	}
	
	public void damageTaken(int damage) {
		damageTaken += damage;
	}
	
	public void bedEntered() {
		bedsEntered++;
	}
	
	public void portalEntered() {
		portalsEntered++;
	}
	
	public void chatMessageSent(int words) {
		wordsSaid += words;
	}
	
	public void commandSent() {
		commandsSent++;
	}
}
