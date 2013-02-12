package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.wolvencraft.yasp.db.CachedData;
import com.wolvencraft.yasp.db.DBEntry;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal.TotalBlocksTable;

public class TotalBlocks implements DataHolder {
	
	/**
	 * Default constructor. Takes in the Player object and the MaterialData of the block
	 * @param player <b>Player</b> tracked player
	 * @param material <b>MaterialData</b> block data
	 */
	public TotalBlocks(Player player, MaterialData material) {
		this.playerName = player.getPlayerListName();
		this.playerId = CachedData.getCachedPlayerId(playerName);
		this.material = material;
		this.broken = 0;
		this.placed = 0;
	}
	
	private String playerName;
	private int playerId;
	private MaterialData material;
	private int broken;
	private int placed;
	
	@Override
	public void fetchData() {
		List<DBEntry> results = QueryUtils.select(
			TotalBlocksTable.TableName.toString(),
			"*",
			TotalBlocksTable.PlayerId + " = " + CachedData.getCachedPlayerId(playerName)
		);
		
		if(results.isEmpty()) QueryUtils.insert(TotalBlocksTable.TableName.toString(), getValues());
		else {
			broken = results.get(0).getValueAsInteger(TotalBlocksTable.Destroyed.toString());
			placed = results.get(0).getValueAsInteger(TotalBlocksTable.Placed.toString());
		}
	}

	@Override
	public boolean pushData() {
		return QueryUtils.update(
			TotalBlocksTable.TableName.toString(),
			getValues(),
			TotalBlocksTable.PlayerId + " = " + playerId,
			TotalBlocksTable.MaterialId + " = " + material.getItemTypeId()
		);
	}
	
	@Override
	public String getDataLabel() { return DataLabel.TotalBlocks.getAlias(); }
	
	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TotalBlocksTable.PlayerId.toString(), playerId);
		map.put(TotalBlocksTable.MaterialId.toString(), material.getItemTypeId());
		map.put(TotalBlocksTable.Destroyed.toString(), broken);
		map.put(TotalBlocksTable.Placed.toString(), placed);
		return map;
	}
	
	/**
	 * Adds the specified number of blocks to the total number of blocks destroyed
	 * @param blocks Blocks to add
	 */
	public void addBroken(int blocks) { broken += blocks; }
	
	/**
	 * Adds the specified number of blocks to the total number of blocks placed
	 * @param blocks Blocks to add
	 */
	public void addPlaced(int blocks) { placed += blocks; }
	
}
