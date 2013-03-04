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
	public TotalPVPEntry(int victimId, ItemStack weapon) {
		this.victimId = victimId;
		this.weapon = weapon;
		this.times = 0;
	}
	
	private int victimId;
	private ItemStack weapon;
	private int times;
	
	@Override
	public void fetchData(int killerId) {
		List<QueryResult> results = QueryUtils.select(
			TotalPVPKills.TableName.toString(),
			new String[] {"*"},
			new String[] { TotalPVPKills.PlayerId.toString(), killerId + ""},
			new String[] { TotalPVPKills.VictimId.toString(), victimId + ""},
			new String[] { TotalPVPKills.MaterialId.toString(), weapon.getTypeId() + ""},
			new String[] { TotalPVPKills.MaterialData.toString(), weapon.getData().getData() + ""}
		);
		if(results.isEmpty()) QueryUtils.insert(TotalPVPKills.TableName.toString(), getValues(killerId));
		else {
			times = results.get(0).getValueAsInteger(TotalPVPKills.Times.toString());
		}
	}

	@Override
	public boolean pushData(int killerId) {
		return QueryUtils.update(
			TotalPVPKills.TableName.toString(),
			getValues(killerId), 
			new String[] { TotalPVPKills.PlayerId.toString(), killerId + ""},
			new String[] { TotalPVPKills.VictimId.toString(), victimId + ""},
			new String[] { TotalPVPKills.MaterialId.toString(), weapon.getTypeId() + ""},
			new String[] { TotalPVPKills.MaterialData.toString(), weapon.getData().getData() + ""}
		);
	}

	@Override
	public Map<String, Object> getValues(int killerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TotalPVPKills.PlayerId.toString(), killerId);
		map.put(TotalPVPKills.VictimId.toString(), victimId);
		map.put(TotalPVPKills.MaterialId.toString(), weapon.getTypeId());
		map.put(TotalPVPKills.MaterialData.toString(), weapon.getData().getData());
		map.put(TotalPVPKills.Times.toString(), times);
		return map;
	}
	
	public boolean equals(int victimId, ItemStack weapon) {
		return this.victimId == victimId && this.weapon.equals(weapon);
	}
	
	/**
	 * Increments the number of times the victim was killed
	 */
	public void addTimes() { times++; }
	
}
