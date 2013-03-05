package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Normal;

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
			Normal.TotalPVPKills.TableName.toString(),
			new String[] {"*"},
			new String[] { Normal.TotalPVPKills.PlayerId.toString(), killerId + ""},
			new String[] { Normal.TotalPVPKills.VictimId.toString(), victimId + ""},
			new String[] { Normal.TotalPVPKills.MaterialId.toString(), weapon.getTypeId() + ""},
			new String[] { Normal.TotalPVPKills.MaterialData.toString(), weapon.getData().getData() + ""}
		);
		if(results.isEmpty()) QueryUtils.insert(Normal.TotalPVPKills.TableName.toString(), getValues(killerId));
		else {
			times = results.get(0).getValueAsInteger(Normal.TotalPVPKills.Times.toString());
		}
	}

	@Override
	public boolean pushData(int killerId) {
		return QueryUtils.update(
			Normal.TotalPVPKills.TableName.toString(),
			getValues(killerId), 
			new String[] { Normal.TotalPVPKills.PlayerId.toString(), killerId + ""},
			new String[] { Normal.TotalPVPKills.VictimId.toString(), victimId + ""},
			new String[] { Normal.TotalPVPKills.MaterialId.toString(), weapon.getTypeId() + ""},
			new String[] { Normal.TotalPVPKills.MaterialData.toString(), weapon.getData().getData() + ""}
		);
	}

	@Override
	public Map<String, Object> getValues(int killerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Normal.TotalPVPKills.PlayerId.toString(), killerId);
		map.put(Normal.TotalPVPKills.VictimId.toString(), victimId);
		map.put(Normal.TotalPVPKills.MaterialId.toString(), weapon.getTypeId());
		map.put(Normal.TotalPVPKills.MaterialData.toString(), weapon.getData().getData());
		map.put(Normal.TotalPVPKills.Times.toString(), times);
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
