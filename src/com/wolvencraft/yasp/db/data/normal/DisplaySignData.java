package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.Map;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Normal.DistancePlayers;
import com.wolvencraft.yasp.db.tables.Normal.TotalBlocks;
import com.wolvencraft.yasp.db.tables.Normal.TotalDeathPlayers;
import com.wolvencraft.yasp.db.tables.Normal.TotalItems;
import com.wolvencraft.yasp.db.tables.Normal.TotalPVEKills;
import com.wolvencraft.yasp.db.tables.Normal.TotalPVPKills;

public class DisplaySignData {
	
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
	
	public void fetchData() {
		blocksBroken = (int) QueryUtils.sum(TotalBlocks.TableName.toString(), TotalBlocks.Destroyed.toString());
		blocksPlaced = (int) QueryUtils.sum(TotalBlocks.TableName.toString(), TotalBlocks.Placed.toString());
		distance = QueryUtils.sum(DistancePlayers.TableName.toString(), DistancePlayers.Foot.toString());
		toolsBroken = (int) QueryUtils.sum(TotalItems.TableName.toString(), TotalItems.Broken.toString());
		itemsCrafted = (int) QueryUtils.sum(TotalItems.TableName.toString(), TotalItems.Crafted.toString());
		snacksEaten = (int) QueryUtils.sum(TotalItems.TableName.toString(), TotalItems.Used.toString());
		
		pvpKills = (int) QueryUtils.sum(TotalPVPKills.TableName.toString(), TotalPVPKills.Times.toString());
		pveKills = (int) QueryUtils.sum(TotalPVEKills.TableName.toString(), TotalPVEKills.CreatureKilled.toString());
		pveDeaths = (int) QueryUtils.sum(TotalPVEKills.TableName.toString(), TotalPVEKills.PlayerKilled.toString());
		otherKills = (int) QueryUtils.sum(TotalDeathPlayers.TableName.toString(), TotalDeathPlayers.Times.toString());
	}
	
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
