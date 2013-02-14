package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.CachedData;
import com.wolvencraft.yasp.db.DBEntry;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal.TotalPVETable;

/**
 * Represents a logged event, in which either a player or a creature was killed.<br />
 * Each entry must have a unique player - creature combination.
 * @author bitWolfy
 *
 */
public class TotalPVE implements DataHolder {
	
	/**
	 * <b>Default constructor</b><br />
	 * Creates a new TotalPVE object based on the player and creature in question
	 * @param player Player in question
	 * @param creature Creature in question
	 */
	public TotalPVE(Player player, Creature creature) {
		this.playerName = player.getPlayerListName();
		this.playerId = CachedData.getCachedPlayerId(playerName);
		this.creatureId = creature.getType().name();
		this.playerDeaths = 0;
		this.creatureDeaths = 0;
	}
	
	private String playerName;
	private int playerId;
	private String creatureId;
	private int playerDeaths;
	private int creatureDeaths;
	
	@Override
	public String getDataLabel() { return DataLabel.TotalPVE.toParameterizedString(playerName, creatureId); }
	
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

	@Override
	public boolean equals(DataHolder holder) {
		return holder instanceof TotalPVE
				&& holder.getPlayerName().equals(playerName)
				&& ((TotalPVE) holder).getCreatureId().equals(creatureId);
	}

	@Override
	public boolean equals(String... arguments) {
		return arguments[0].equals(playerName)
				&& arguments[1].equals(creatureId);
	}
	
	@Override
	public String getPlayerName() { return playerName; }
	
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
