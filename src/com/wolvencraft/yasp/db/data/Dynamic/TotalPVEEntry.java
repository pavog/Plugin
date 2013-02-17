package com.wolvencraft.yasp.db.data.Dynamic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wolvencraft.yasp.db.DBEntry;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Dynamic.TotalPVETable;

/**
 * Represents a logged event, in which either a player or a creature was killed.<br />
 * Each entry must have a unique player - creature combination.
 * @author bitWolfy
 *
 */
public class TotalPVEEntry implements DynamicData {
	
	/**
	 * <b>Default constructor</b><br />
	 * Creates a new TotalPVE object based on the player and creature in question
	 * @param player Player in question
	 * @param creature Creature in question
	 */
	public TotalPVEEntry(int playerId, String creatureId) {
		this.playerId = playerId;
		this.creatureId = creatureId;
		this.playerDeaths = 0;
		this.creatureDeaths = 0;
	}
	
	private int playerId;
	private String creatureId;
	private int playerDeaths;
	private int creatureDeaths;
	
	@Override
	public void fetchData() {
		List<DBEntry> results = QueryUtils.select(
			TotalPVETable.TableName.toString(),
			"*",
			TotalPVETable.PlayerId.toString() + " = " + playerId,
			TotalPVETable.CreatureId.toString() + " = " + creatureId
		);
		if(results.isEmpty()) QueryUtils.insert(TotalPVETable.TableName.toString(), getValues());
		else {
			playerDeaths = results.get(0).getValueAsInteger(TotalPVETable.PlayerKilled.toString());
			creatureDeaths = results.get(0).getValueAsInteger(TotalPVETable.CreatureKilled.toString());
		}
	}

	@Override
	public boolean pushData() {
		return QueryUtils.update(
			TotalPVETable.TableName.toString(),
			getValues(), 
			TotalPVETable.PlayerId.toString() + " = " + playerId,
			TotalPVETable.CreatureId.toString() + " = " + creatureId
		);
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TotalPVETable.PlayerId.toString(), playerId);
		map.put(TotalPVETable.CreatureId.toString(), creatureId);
		map.put(TotalPVETable.PlayerKilled.toString(), playerDeaths);
		map.put(TotalPVETable.CreatureKilled.toString(), creatureDeaths);
		return map;
	}
	
	public boolean equals(int playerId, String creatureId) {
		return this.playerId == playerId && this.creatureId.equals(creatureId);
	}
	
	/**
	 * Returns the creature type
	 * @return <b>String</b> creature type
	 */
	public String getCreatureId() { return creatureId; }
	
	/**
	 * Increments the number of time a player died
	 */
	public void addPlayerDeaths() { playerDeaths++; }
	
	/**
	 * Increments the number of times a creature died
	 */
	public void addCreatureDeaths() { creatureDeaths++; }
}
