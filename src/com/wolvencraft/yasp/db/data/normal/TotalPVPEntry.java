package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wolvencraft.yasp.db.DBEntry;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal.TotalPVPKills;

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
			TotalPVPKills.TableName.toString(),
			"*",
			TotalPVPKills.PlayerId.toString() + " = " + killerId,
			TotalPVPKills.VictimId + " = " + victimId
		);
		if(results.isEmpty()) QueryUtils.insert(TotalPVPKills.TableName.toString(), getValues());
		else {
			times = results.get(0).getValueAsInteger(TotalPVPKills.Times.toString());
		}
	}

	@Override
	public boolean pushData() {
		return QueryUtils.update(
			TotalPVPKills.TableName.toString(),
			getValues(), 
			TotalPVPKills.PlayerId.toString() + " = " + killerId,
			TotalPVPKills.VictimId + " = " + victimId
		);
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TotalPVPKills.PlayerId.toString(), killerId);
		map.put(TotalPVPKills.VictimId.toString(), victimId);
		map.put(TotalPVPKills.Times.toString(), times);
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