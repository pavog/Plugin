package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.DBEntry;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal.PlayersTable;

public class TrackedPlayer implements DataHolder {
	
	public TrackedPlayer (Player player) {
		this.playerName = player.getPlayerListName();
		this.online = true;
		this.expPercent = player.getExp();
		this.expTotal = player.getTotalExperience();
		this.expLevel = player.getLevel();
		this.foodLevel = player.getFoodLevel();
		this.healthLevel = player.getHealth();
		this.logins = 0;
		this.deaths = 0;
	}
	
	private String playerName;
	private boolean online;
	private double expPercent;
	private int expTotal;
	private int expLevel;
	private int foodLevel;
	private int healthLevel;
	private long firstJoin;
	private int logins;
	private int deaths;
	
	@Override
	public void fetchData() {
		List<DBEntry> results = QueryUtils.select(
			PlayersTable.TableName.toString(),
			"*",
			PlayersTable.Name.toString() + " = " + playerName
		);
		if(results.isEmpty()) QueryUtils.insert(PlayersTable.TableName.toString(), getValues());
		else {
			logins = results.get(0).getValueAsInteger(PlayersTable.Logins.toString());
			deaths = results.get(0).getValueAsInteger(PlayersTable.Deaths.toString());
		}
	}

	@Override
	public boolean pushData() {
		return QueryUtils.update(
			PlayersTable.TableName.toString(),
			getValues(), 
			PlayersTable.Name.toString() + " = " + playerName
		);
	}
	
	@Override
	public String getDataLabel() { return DataLabel.Player.getAlias() + ":" + playerName; }

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PlayersTable.Name.toString(), playerName);
		if(online) map.put(PlayersTable.Online.toString(), "Y");
		else map.put(PlayersTable.Online.toString(), "N");
		map.put(PlayersTable.ExperiencePercent.toString(), expPercent);
		map.put(PlayersTable.ExperienceTotal.toString(), expTotal);
		map.put(PlayersTable.ExperienceLevel.toString(), expLevel);
		map.put(PlayersTable.FoodLevel.toString(), foodLevel);
		map.put(PlayersTable.HealthLevel.toString(), healthLevel);
		map.put(PlayersTable.FirstLogin.toString(), firstJoin);
		map.put(PlayersTable.Logins.toString(), logins);
		map.put(PlayersTable.Deaths.toString(), deaths);
		return map;
	}
	
}
