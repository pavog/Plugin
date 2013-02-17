package com.wolvencraft.yasp.db.data.Dynamic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wolvencraft.yasp.db.DBEntry;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Dynamic.TotalPVPTable;

/**
 * Represents a logged event, in which one player killed another.<br />
 * Each entry must have a unique killer - victim combination.<br />
 * <b>TotalPVP(player1, player2)</b> != <b>TotalPVP(player2, player1)</b>
 * @author bitWolfy
 *
 */
public class TotalPVPEntry implements DynamicData {
	
	/**
	 * <b>Default constructor</b><br />
	 * Creates a new TotalPVP object based on the killer and victim in question
	 * @param killer Player who killed the victim
	 * @param victim Player who was killed
	 */
	public TotalPVPEntry(int killerId, int victimId) {
		this.killerId = killerId;
		this.victimId = victimId;
		this.times = 0;
	}
	
	private int killerId;
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
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TotalPVPTable.PlayerId.toString(), killerId);
		map.put(TotalPVPTable.VictimId.toString(), victimId);
		map.put(TotalPVPTable.Times.toString(), times);
		return map;
	}
	
	public boolean equals(int killerId, int victimId) {
		return this.killerId == killerId && this.victimId == victimId;
	}
	
	/**
	 * Increments the number of times the victim was killed
	 */
	public void addTimes() { times++; }
	
}
