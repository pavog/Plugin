package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.CachedData;
import com.wolvencraft.yasp.db.DBEntry;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal.TotalPVPTable;

/**
 * Represents a logged event, in which one player killed another.<br />
 * Each entry must have a unique killer - victim combination.<br />
 * <b>TotalPVP(player1, player2)</b> != <b>TotalPVP(player2, player1)</b>
 * @author bitWolfy
 *
 */
public class TotalPVP implements DataHolder {
	
	/**
	 * <b>Default constructor</b><br />
	 * Creates a new TotalPVP object based on the killer and victim in question
	 * @param killer Player who killed the victim
	 * @param victim Player who was killed
	 */
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
	public String getDataLabel() { return DataLabel.TotalPVP.toParameterizedString(killerName, victimName); }
	
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

	@Override
	public boolean equals(DataHolder holder) {
		return holder instanceof TotalPVP
				&& ((TotalPVP) holder).getKillerName().equals(killerName)
				&& ((TotalPVP) holder).getVictimName().equals(victimName);
	}

	@Override
	public boolean equals(String... arguments) {
		return arguments[0].equals(killerName)
				&& arguments[1].equals(victimName);
	}
	
	@Override
	public String getPlayerName() { return killerName; }
	
	/**
	 * Returns the name of the killer
	 * @return <b>String</b> killer's name
	 */
	public String getKillerName() { return killerName; }
	
	/**
	 * Returns the name of the victim
	 * @return <b>String</b> victim's name
	 */
	public String getVictimName() { return victimName; }
	
	/**
	 * Increments the number of times the victim was killed
	 */
	public void addTimes() { times++; }
	
}
