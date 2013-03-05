package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Normal;

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
	public TotalPVEEntry(EntityType creatureType, ItemStack weapon) {
		this.creatureType = creatureType;
		this.weapon = weapon;
		this.playerDeaths = 0;
		this.creatureDeaths = 0;
	}
	
	private EntityType creatureType;
	private ItemStack weapon;
	private int playerDeaths;
	private int creatureDeaths;
	
	@Override
	public void fetchData(int playerId) {
		List<QueryResult> results = QueryUtils.select(
			Normal.TotalPVEKills.TableName.toString(),
			new String[] {"*"},
			new String[] { Normal.TotalPVEKills.PlayerId.toString(), playerId + ""},
			new String[] { Normal.TotalPVEKills.CreatureId.toString(), creatureType.getTypeId() + ""},
			new String[] { Normal.TotalPVEKills.MaterialId.toString(), weapon.getTypeId() + ""},
			new String[] { Normal.TotalPVEKills.MaterialData.toString(), weapon.getData().getData() + ""}
		);
		if(results.isEmpty()) QueryUtils.insert(Normal.TotalPVEKills.TableName.toString(), getValues(playerId));
		else {
			playerDeaths = results.get(0).getValueAsInteger(Normal.TotalPVEKills.PlayerKilled.toString());
			creatureDeaths = results.get(0).getValueAsInteger(Normal.TotalPVEKills.CreatureKilled.toString());
		}
	}

	@Override
	public boolean pushData(int playerId) {
		return QueryUtils.update(
			Normal.TotalPVEKills.TableName.toString(),
			getValues(playerId),
			new String[] { Normal.TotalPVEKills.PlayerId.toString(), playerId + ""},
			new String[] { Normal.TotalPVEKills.CreatureId.toString(), creatureType.getTypeId() + ""},
			new String[] { Normal.TotalPVEKills.MaterialId.toString(), weapon.getTypeId() + ""},
			new String[] { Normal.TotalPVEKills.MaterialData.toString(), weapon.getData().getData() + ""}
		);
	}

	@Override
	public Map<String, Object> getValues(int playerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Normal.TotalPVEKills.PlayerId.toString(), playerId);
		map.put(Normal.TotalPVEKills.CreatureId.toString(), creatureType.getTypeId());
		map.put(Normal.TotalPVEKills.MaterialId.toString(), weapon.getTypeId());
		map.put(Normal.TotalPVEKills.MaterialData.toString(), weapon.getData().getData());
		map.put(Normal.TotalPVEKills.PlayerKilled.toString(), playerDeaths);
		map.put(Normal.TotalPVEKills.CreatureKilled.toString(), creatureDeaths);
		return map;
	}
	
	public boolean equals(EntityType creatureType, ItemStack weapon) {
		return this.creatureType.equals(creatureType) && this.weapon.equals(weapon);
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
