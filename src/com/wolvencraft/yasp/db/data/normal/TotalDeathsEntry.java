package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal.TotalDeathPlayers;

/**
 * Represents the total number of times a player died of a particular cause.<br />
 * Each entry must have a unique player and a unique death cause.
 * @author bitWolfy
 *
 */
public class TotalDeathsEntry implements _NormalData {
	
	public TotalDeathsEntry(DamageCause cause) {
		this.cause = cause;
		this.times = 0;
	}
	
	private DamageCause cause;
	private int times;
	
	@Override
	public void fetchData(int playerId) {
		List<QueryResult> results = QueryUtils.select(
			TotalDeathPlayers.TableName.toString(),
			new String[] {"*"},
			new String[] { TotalDeathPlayers.PlayerId.toString(), playerId + ""},
			new String[] { TotalDeathPlayers.Cause.toString(), cause.name()}
		);
		
		if(results.isEmpty()) QueryUtils.insert(TotalDeathPlayers.TableName.toString(), getValues(playerId));
		else {
			times = results.get(0).getValueAsInteger(TotalDeathPlayers.Times.toString());
		}
	}

	@Override
	public boolean pushData(int playerId) {
		return QueryUtils.update(
			TotalDeathPlayers.TableName.toString(),
			getValues(playerId),
			new String[] { TotalDeathPlayers.PlayerId.toString(), playerId + ""},
			new String[] { TotalDeathPlayers.Cause.toString(), cause.name()}
		);
	}

	@Override
	public Map<String, Object> getValues(int playerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TotalDeathPlayers.PlayerId.toString(), playerId);
		map.put(TotalDeathPlayers.Cause.toString(), cause.name());
		map.put(TotalDeathPlayers.Times.toString(), times);
		return map;
	}
	
	public boolean equals(DamageCause cause) {
		return this.cause.equals(cause);
	}
	
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
