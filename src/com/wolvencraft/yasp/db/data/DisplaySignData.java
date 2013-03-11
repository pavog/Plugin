package com.wolvencraft.yasp.db.data;

import java.util.HashMap;
import java.util.Map;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Normal.*;

/**
 * Stores generic statistical data to be used on display signs.<br />
 * Does not implement NormalData because it does not synch data back to the database.
 * @author bitWolfy
 *
 */
public class DisplaySignData {
	
	/**
	 * <b>Default Constructor</b><br />
	 * Sets up the default values for the data holder.
	 */
	public DisplaySignData() {
		blocksBroken = 0;
		blocksPlaced = 0;
		distance = 0;
		toolsBroken = 0;
		itemsCrafted = 0;
		snacksEaten = 0;
		
		pvpKills = 0;
		pveKills = 0;
		otherKills = 0;
	}
	
	private int blocksBroken;
	private int blocksPlaced;
	private double distance;
	private int toolsBroken;
	private int itemsCrafted;
	private int snacksEaten;
	
	private int pvpKills;
	private int pveKills;
	private int pveDeaths;
	private int otherKills;
	
	/**
	 * Fetches the data from the remote database.<br />
	 * Automatically calculates values from the contents of corresponding tables.
	 */
	public void fetchData() {
		blocksBroken = (int) QueryUtils.sum(TotalBlocksTable.TableName.toString(), TotalBlocksTable.Destroyed.toString());
		blocksPlaced = (int) QueryUtils.sum(TotalBlocksTable.TableName.toString(), TotalBlocksTable.Placed.toString());
		distance = QueryUtils.sum(DistancePlayersTable.TableName.toString(), DistancePlayersTable.Foot.toString());
		toolsBroken = (int) QueryUtils.sum(TotalItemsTable.TableName.toString(), TotalItemsTable.Broken.toString());
		itemsCrafted = (int) QueryUtils.sum(TotalItemsTable.TableName.toString(), TotalItemsTable.Crafted.toString());
		snacksEaten = (int) QueryUtils.sum(TotalItemsTable.TableName.toString(), TotalItemsTable.Used.toString());
		
		pvpKills = (int) QueryUtils.sum(TotalPVPKillsTable.TableName.toString(), TotalPVPKillsTable.Times.toString());
		pveKills = (int) QueryUtils.sum(TotalPVEKillsTable.TableName.toString(), TotalPVEKillsTable.CreatureKilled.toString());
		pveDeaths = (int) QueryUtils.sum(TotalPVEKillsTable.TableName.toString(), TotalPVEKillsTable.PlayerKilled.toString());
		otherKills = (int) QueryUtils.sum(TotalDeathPlayersTable.TableName.toString(), TotalDeathPlayersTable.Times.toString());
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
		values.put("pveKills", pveKills);
		values.put("pveDeaths", pveDeaths);
		values.put("otherKills", otherKills);
		return values;
	}
	
}
