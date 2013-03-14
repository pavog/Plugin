package com.wolvencraft.yasp.db.data.receive;

import java.util.HashMap;
import java.util.Map;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Normal.*;

/**
 * Generic Server information used on DisplaySigns and books.
 * @author bitWolfy
 *
 */
public class ServerTotals {
	
	/**
	 * <b>Default Constructor</b><br />
	 * Sets up the default values for the data holder.
	 */
	public ServerTotals() {
		blocksBroken = 0;
		blocksPlaced = 0;
		distance = 0;
		toolsBroken = 0;
		itemsCrafted = 0;
		snacksEaten = 0;
		
		pvpKills = 0;
		pveKills = 0;
		otherKills = 0;
		
		fetchData();
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
		blocksBroken = (int) QueryUtils.select(TotalBlocksTable.TableName.toString()).column(TotalBlocksTable.Destroyed.toString()).sum();
		blocksPlaced = (int) QueryUtils.select(TotalBlocksTable.TableName.toString()).column(TotalBlocksTable.Placed.toString()).sum();
		distance = QueryUtils.select(DistancePlayersTable.TableName.toString()).column(DistancePlayersTable.Foot.toString()).sum();
		toolsBroken = (int) QueryUtils.select(TotalItemsTable.TableName.toString()).column(TotalItemsTable.Broken.toString()).sum();
		itemsCrafted = (int) QueryUtils.select(TotalItemsTable.TableName.toString()).column(TotalItemsTable.Crafted.toString()).sum();
		snacksEaten = (int) QueryUtils.select(TotalItemsTable.TableName.toString()).column(TotalItemsTable.Used.toString()).sum();
		
		pvpKills = (int) QueryUtils.select(TotalPVPKillsTable.TableName.toString()).column(TotalPVPKillsTable.Times.toString()).sum();
		pveKills = (int) QueryUtils.select(TotalPVEKillsTable.TableName.toString()).column(TotalPVEKillsTable.CreatureKilled.toString()).sum();
		pveDeaths = (int) QueryUtils.select(TotalPVEKillsTable.TableName.toString()).column(TotalPVEKillsTable.PlayerKilled.toString()).sum();
		otherKills = (int) QueryUtils.select(TotalDeathPlayersTable.TableName.toString()).column(TotalDeathPlayersTable.Times.toString()).sum();
	}
	
	/**
	 * Bundles up the values into one Map for ease of access.
	 * @return Map of values
	 */
	public Map<String, Object> getValues() {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("blBroken", blocksBroken);
		values.put("blPlaced", blocksPlaced);
		values.put("distance", distance);
		values.put("itBroken", toolsBroken);
		values.put("itCrafted", itemsCrafted);
		values.put("itEaten", snacksEaten);
		values.put("pvpKills", pvpKills);
		values.put("pveKills", pveKills);
		values.put("pveDeaths", pveDeaths);
		values.put("othKills", otherKills);
		values.putAll(DataCollector.global().getValueMap());
		return values;
	}
	
}
