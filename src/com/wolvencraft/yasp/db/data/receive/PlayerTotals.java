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
		blocksBroken = (int) QueryUtils.sum(TotalBlocksTable.TableName.toString(), TotalBlocksTable.Destroyed.toString(), new String[] {TotalBlocksTable.PlayerId.toString(), playerId + ""});
		blocksPlaced = (int) QueryUtils.sum(TotalBlocksTable.TableName.toString(), TotalBlocksTable.Placed.toString(), new String[] {TotalBlocksTable.PlayerId.toString(), playerId + ""});
		distance = QueryUtils.sum(DistancePlayersTable.TableName.toString(), DistancePlayersTable.Foot.toString(), new String[] {DistancePlayersTable.PlayerId.toString(), playerId + ""});
		toolsBroken = (int) QueryUtils.sum(TotalItemsTable.TableName.toString(), TotalItemsTable.Broken.toString(), new String[] {TotalItemsTable.PlayerId.toString(), playerId + ""});
		itemsCrafted = (int) QueryUtils.sum(TotalItemsTable.TableName.toString(), TotalItemsTable.Crafted.toString(), new String[] {TotalItemsTable.PlayerId.toString(), playerId + ""});
		snacksEaten = (int) QueryUtils.sum(TotalItemsTable.TableName.toString(), TotalItemsTable.Used.toString(), new String[] {TotalItemsTable.PlayerId.toString(), playerId + ""});
		
		pvpKills = (int) QueryUtils.sum(TotalPVPKillsTable.TableName.toString(), TotalPVPKillsTable.Times.toString(), new String[] {TotalPVPKillsTable.PlayerId.toString(), playerId + ""});
		pvpDeaths = (int) QueryUtils.sum(TotalPVPKillsTable.TableName.toString(), TotalPVPKillsTable.Times.toString(), new String[] {TotalPVPKillsTable.VictimId.toString(), playerId + ""});
		kdr = (double) Math.round((pvpKills / pvpDeaths) * 100000) / 100000;
		
		pveKills = (int) QueryUtils.sum(TotalPVEKillsTable.TableName.toString(), TotalPVEKillsTable.CreatureKilled.toString(), new String[] {TotalPVEKillsTable.PlayerId.toString(), playerId + ""});
		otherKills = (int) QueryUtils.sum(TotalDeathPlayersTable.TableName.toString(), TotalDeathPlayersTable.Times.toString(), new String[] {TotalDeathPlayersTable.PlayerId.toString(), playerId + ""});
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
