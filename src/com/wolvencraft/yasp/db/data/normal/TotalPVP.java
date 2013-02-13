package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.CachedData;
import com.wolvencraft.yasp.db.DBEntry;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal.TotalPVPTable;

public class TotalPVP implements DataHolder {
	
	public TotalPVP(Player killer, Player victim) {
		this.killerName = killer.getPlayerListName();
		this.killerId = CachedData.getCachedPlayerId(killerName);
		this.victimName = victim.getPlayerListName();
		this.victimId = CachedData.getCachedPlayerId(victimName);
		this.times = 0;
	}
	
	private String killerName;
	private int killerId;
	private String victimName;
	private int victimId;
	private int times;
	
	@Override
	public void fetchData() {
		List<DBEntry> results = QueryUtils.select(
			TotalPVPTable.TableName.toString(),
			"*",
			TotalPVPTable.PlayerId.toString() + " = " + killerId,
			TotalPVPTable.VictimId + " = " + victimId
		);
		if(results.isEmpty()) QueryUtils.insert(TotalPVPTable.TableName.toString(), getValues());
		else {
			times = results.get(0).getValueAsInteger(TotalPVPTable.Times.toString());
		}
	}

	@Override
	public boolean pushData() {
		return QueryUtils.update(
			TotalPVPTable.TableName.toString(),
			getValues(), 
			TotalPVPTable.PlayerId.toString() + " = " + killerId,
			TotalPVPTable.VictimId + " = " + victimId
		);
	}
	
	@Override
	public String getDataLabel() { return DataLabel.TotalPVP.getAlias() + ":" + killerName; }

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TotalPVPTable.PlayerId.toString(), killerId);
		map.put(TotalPVPTable.VictimId.toString(), victimId);
		map.put(TotalPVPTable.Times.toString(), times);
		return map;
	}
	
	/**
	 * Increments the number of times the victim was killed
	 */
	public void addTimes() { times++; }
	
}
