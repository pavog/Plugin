package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.wolvencraft.yasp.db.CachedData;
import com.wolvencraft.yasp.db.DBEntry;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal.TotalDeathsTable;

public class TotalDeaths implements DataHolder {
	
	public TotalDeaths(Player player, DamageCause cause) {
		this.playerName = player.getPlayerListName();
		this.playerId = CachedData.getCachedPlayerId(playerName);
		this.cause = cause;
		this.times = 0;
	}
	
	private String playerName;
	private int playerId;
	private DamageCause cause;
	private int times;
	
	@Override
	public void fetchData() {
		List<DBEntry> results = QueryUtils.select(
			TotalDeathsTable.TableName.toString(),
			"*",
			TotalDeathsTable.PlayerId + " = " + CachedData.getCachedPlayerId(playerName),
			TotalDeathsTable.Cause + " = " + cause.name()
		);
		
		if(results.isEmpty()) QueryUtils.insert(TotalDeathsTable.TableName.toString(), getValues());
		else {
			times = results.get(0).getValueAsInteger(TotalDeathsTable.Times.toString());
		}
	}

	@Override
	public boolean pushData() {
		return QueryUtils.update(
			TotalDeathsTable.TableName.toString(),
			getValues(),
			TotalDeathsTable.PlayerId + " = " + playerId,
			TotalDeathsTable.Cause + " = " + cause.name()
		);
	}
	
	@Override
	public String getDataLabel() { return DataLabel.TotalDeaths.getAlias(); }

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TotalDeathsTable.PlayerId.toString(), playerId);
		map.put(TotalDeathsTable.Cause.toString(), cause.name());
		map.put(TotalDeathsTable.Times.toString(), times);
		return map;
	}
	
	/**
	 * Adds the specified number of deaths to the existing value
	 * @param extraTimes Number of deaths to add
	 */
	public void addTimes(int extraTimes) { times += extraTimes; }
	
}
