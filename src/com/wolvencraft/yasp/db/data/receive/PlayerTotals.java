package com.wolvencraft.yasp.db.data.receive;

import java.util.HashMap;
import java.util.Map;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Normal.DistancePlayersTable;
import com.wolvencraft.yasp.db.tables.Normal.TotalBlocksTable;
import com.wolvencraft.yasp.db.tables.Normal.TotalDeathPlayersTable;
import com.wolvencraft.yasp.db.tables.Normal.TotalItemsTable;
import com.wolvencraft.yasp.db.tables.Normal.TotalPVEKillsTable;
import com.wolvencraft.yasp.db.tables.Normal.TotalPVPKillsTable;

/**
 * Generic Player information used on DisplaySigns and books.
 * @author bitWolfy
 *
 */
public class PlayerTotals {
	
	/**
	 * <b>Default Constructor</b><br />
	 * Sets up the default values for the data holder.
	 */
	public PlayerTotals(int playerId) {
		this.playerId = playerId;
		
		blocksBroken = 0;
		blocksPlaced = 0;
		distance = 0;
		toolsBroken = 0;
		itemsCrafted = 0;
		snacksEaten = 0;
		
		pvpKills = 0;
		pvpDeaths = 0;
		kdr = 1;
		pveKills = 0;
		otherKills = 0;
		
		fetchData();
	}
	
	private int playerId;
	
	private int blocksBroken;
	private int blocksPlaced;
	private double distance;
	private int toolsBroken;
	private int itemsCrafted;
	private int snacksEaten;
	
	private int pvpKills;
	private int pvpDeaths;
	private double kdr;
	private int pveKills;
	private int otherKills;
	
	/**
	 * Fetches the data from the remote database.<br />
	 * Automatically calculates values from the contents of corresponding tables.
	 */
	public void fetchData() {
		blocksBroken = (int) QueryUtils.select(TotalBlocksTable.TableName.toString()).column(TotalBlocksTable.Destroyed.toString()).condition(TotalBlocksTable.PlayerId.toString(), playerId + "").sum();
		blocksPlaced = (int) QueryUtils.select(TotalBlocksTable.TableName.toString()).column(TotalBlocksTable.Placed.toString()).condition(TotalBlocksTable.PlayerId.toString(), playerId + "").sum();
		distance = QueryUtils.select(DistancePlayersTable.TableName.toString()).column(DistancePlayersTable.Foot.toString()).condition(DistancePlayersTable.PlayerId.toString(), playerId + "").sum();
		toolsBroken = (int) QueryUtils.select(TotalItemsTable.TableName.toString()).column(TotalItemsTable.Broken.toString()).condition(TotalItemsTable.PlayerId.toString(), playerId + "").sum();
		itemsCrafted = (int) QueryUtils.select(TotalItemsTable.TableName.toString()).column(TotalItemsTable.Crafted.toString()).condition(TotalItemsTable.PlayerId.toString(), playerId + "").sum();
		snacksEaten = (int) QueryUtils.select(TotalItemsTable.TableName.toString()).column(TotalItemsTable.Used.toString()).condition(TotalItemsTable.PlayerId.toString(), playerId + "").sum();
		
		pvpKills = (int) QueryUtils.select(TotalPVPKillsTable.TableName.toString()).column(TotalPVPKillsTable.Times.toString()).condition(TotalPVPKillsTable.PlayerId.toString(), playerId + "").sum();
		pvpDeaths = (int) QueryUtils.select(TotalPVPKillsTable.TableName.toString()).column(TotalPVPKillsTable.Times.toString()).condition(TotalPVPKillsTable.VictimId.toString(), playerId + "").sum();
		if(pvpDeaths != 0) kdr = (double) Math.round((pvpKills / pvpDeaths) * 100000) / 100000;
		else kdr = pvpKills;
		
		pveKills = (int) QueryUtils.select(TotalPVEKillsTable.TableName.toString()).column(TotalPVEKillsTable.CreatureKilled.toString()).condition(TotalPVEKillsTable.PlayerId.toString(), playerId + "").sum();
		otherKills = (int) QueryUtils.select(TotalDeathPlayersTable.TableName.toString()).column(TotalDeathPlayersTable.Times.toString()).condition(TotalDeathPlayersTable.PlayerId.toString(), playerId + "").sum();
	}
	
	/**
	 * Bundles up the values into one Map for ease of access.
	 * @return Map of values
	 */
	public Map<String, Object> getValues() {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("blocksBroken", blocksBroken);
		values.put("blocksPlaced", blocksPlaced);
		values.put("distance", distance);
		values.put("toolsBroken", toolsBroken);
		values.put("itemsCrafted", itemsCrafted);
		values.put("snacksEaten", snacksEaten);
		values.put("pvpKills", pvpKills);
		values.put("pvpDeaths", pvpDeaths);
		values.put("kdr", kdr);
		values.put("pveKills", pveKills);
		values.put("otherKills", otherKills);
		return values;
	}
}
