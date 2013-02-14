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

/**
 * Represents the total number of times a player died of a particular cause.<br />
 * Each entry must have a unique player and a unique death cause.
 * @author bitWolfy
 *
 */
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
	public String getDataLabel() { return DataLabel.TotalDeaths.toParameterizedString(playerName, cause.name()); }
	
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
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TotalDeathsTable.PlayerId.toString(), playerId);
		map.put(TotalDeathsTable.Cause.toString(), cause.name());
		map.put(TotalDeathsTable.Times.toString(), times);
		return map;
	}

	@Override
	public boolean equals(DataHolder holder) {
		return holder instanceof TotalDeaths
				&& holder.getPlayerName().equals(playerName)
				&& ((TotalDeaths) holder).getCause().equals(cause);
	}

	@Override
	public boolean equals(String... arguments) {
		return arguments[0].equals(playerName)
				&& arguments[1].equals(cause.name());
	}
	
	@Override
	public String getPlayerName() { return playerName; }
	
	/**
	 * Returns the death cause
	 * @return <b>DamageCause</b> death cause
	 */
	public DamageCause getCause() { return cause; }
	
	/**
	 * Increments the number of times a player died from the specified cause.
	 */
	public void addTimes() { times++; }
	
}
