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
