package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal.TotalPVEKills;

/**
 * Represents a logged event, in which either a player or a creature was killed.<br />
 * Each entry must have a unique player - creature combination.
 * @author bitWolfy
 *
 */
public class TotalPVEEntry implements _NormalData {
	
	/**
	 * <b>Default constructor</b><br />
	 * Creates a new TotalPVE object based on the player and creature in question
	 * @param player Player in question
	 * @param creature Creature in question
	 */
	public TotalPVEEntry(int playerId, EntityType creatureType, ItemStack weapon) {
		this.playerId = playerId;
		this.creatureType = creatureType;
		this.weapon = weapon;
		this.playerDeaths = 0;
		this.creatureDeaths = 0;
	}
	
	private int playerId;
	private EntityType creatureType;
	private ItemStack weapon;
	private int playerDeaths;
	private int creatureDeaths;
	
	@Override
	public void fetchData() {
		List<QueryResult> results = QueryUtils.select(
			TotalPVEKills.TableName.toString(),
			"*",
			TotalPVEKills.PlayerId.toString() + " = " + playerId,
			TotalPVEKills.CreatureId.toString() + " = " + creatureType.getTypeId(),
			TotalPVEKills.MaterialId.toString() + " = " + weapon.getTypeId(),
			TotalPVEKills.MaterialData.toString() + " = " + weapon.getData().getData()
		);
		if(results.isEmpty()) QueryUtils.insert(TotalPVEKills.TableName.toString(), getValues());
		else {
			playerDeaths = results.get(0).getValueAsInteger(TotalPVEKills.PlayerKilled.toString());
			creatureDeaths = results.get(0).getValueAsInteger(TotalPVEKills.CreatureKilled.toString());
		}
	}

	@Override
	public boolean pushData() {
		return QueryUtils.update(
			TotalPVEKills.TableName.toString(),
			getValues(), 
			TotalPVEKills.PlayerId.toString() + " = " + playerId,
			TotalPVEKills.CreatureId.toString() + " = " + creatureType.getTypeId(),
			TotalPVEKills.MaterialId.toString() + " = " + weapon.getTypeId(),
			TotalPVEKills.MaterialData.toString() + " = " + weapon.getData().getData()
		);
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TotalPVEKills.PlayerId.toString(), playerId);
		map.put(TotalPVEKills.CreatureId.toString(), creatureType.getTypeId());
		map.put(TotalPVEKills.MaterialId.toString(), weapon.getTypeId());
		map.put(TotalPVEKills.MaterialData.toString(), weapon.getData().getData());
		map.put(TotalPVEKills.PlayerKilled.toString(), playerDeaths);
		map.put(TotalPVEKills.CreatureKilled.toString(), creatureDeaths);
		return map;
	}
	
	public boolean equals(int playerId, EntityType creatureType, ItemStack weapon) {
		return this.playerId == playerId && this.creatureType.equals(creatureType) && this.weapon.equals(weapon);
	}
	
	/**
	 * Returns the creature type
	 * @return <b>String</b> creature type
	 */
	public EntityType getCreatureId() { return creatureType; }
	
	/**
	 * Increments the number of time a player died
	 */
	public void addPlayerDeaths() { playerDeaths++; }
	
	/**
	 * Increments the number of times a creature died
	 */
	public void addCreatureDeaths() { creatureDeaths++; }
}
