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

public class TotalPVE implements DataHolder {
	
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
	public String getDataLabel() { return DataLabel.TotalPVE.getAlias() + ":" + playerName; }

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TotalPVETable.PlayerId.toString(), playerId);
		map.put(TotalPVETable.CreatureId.toString(), creatureId);
		map.put(TotalPVETable.PlayerKilled.toString(), playerDeaths);
		map.put(TotalPVETable.CreatureKilled.toString(), creatureDeaths);
		return map;
	}
	
	public void addPlayerDeaths() { playerDeaths++; }
	
	public void addCreatureDeaths() { creatureDeaths++; }
}
