package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal.TotalPVPKills;

/**
 * Represents a logged event, in which one player killed another.<br />
 * Each entry must have a unique killer - victim combination.<br />
 * <b>TotalPVP(player1, player2)</b> != <b>TotalPVP(player2, player1)</b>
 * @author bitWolfy
 *
 */
public class TotalPVPEntry implements _NormalData {
	
	/**
	 * <b>Default constructor</b><br />
	 * Creates a new TotalPVP object based on the killer and victim in question
	 * @param killer Player who killed the victim
	 * @param victim Player who was killed
	 */
	public TotalPVPEntry(int killerId, int victimId, ItemStack weapon) {
		this.killerId = killerId;
		this.victimId = victimId;
		this.weapon = weapon;
		this.times = 0;
	}
	
	private int killerId;
	private int victimId;
	private ItemStack weapon;
	private int times;
	
	@Override
	public void fetchData() {
		List<QueryResult> results = QueryUtils.select(
			TotalPVPKills.TableName.toString(),
			"*",
			TotalPVPKills.PlayerId.toString() + " = " + killerId,
			TotalPVPKills.VictimId.toString() + " = " + victimId,
			TotalPVPKills.MaterialId.toString() + " = " + weapon.getTypeId(),
			TotalPVPKills.MaterialData.toString() + " = " + weapon.getData().getData()
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
			TotalPVPKills.VictimId + " = " + victimId,
			TotalPVPKills.MaterialId.toString() + " = " + weapon.getTypeId(),
			TotalPVPKills.MaterialData.toString() + " = " + weapon.getData().getData()
		);
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TotalPVPKills.PlayerId.toString(), killerId);
		map.put(TotalPVPKills.VictimId.toString(), victimId);
		map.put(TotalPVPKills.MaterialId.toString(), weapon.getTypeId());
		map.put(TotalPVPKills.MaterialData.toString(), weapon.getData().getData());
		map.put(TotalPVPKills.Times.toString(), times);
		return map;
	}
	
	public boolean equals(int killerId, int victimId, ItemStack weapon) {
		return this.killerId == killerId && this.victimId == victimId && this.weapon.equals(weapon);
	}
	
	/**
	 * Increments the number of times the victim was killed
	 */
	public void addTimes() { times++; }
	
}
