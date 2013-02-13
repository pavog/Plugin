package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.wolvencraft.yasp.db.CachedData;
import com.wolvencraft.yasp.db.DBEntry;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal.TotalItemsTable;

public class TotalItems implements DataHolder {
	
	/**
	 * Default constructor. Takes in the Player object and the MaterialData of the block
	 * @param player <b>Player</b> tracked player
	 * @param material <b>MaterialData</b> block data
	 */
	public TotalItems(Player player, MaterialData material) {
		this.playerName = player.getPlayerListName();
		this.playerId = CachedData.getCachedPlayerId(playerName);
		this.material = material;
		this.dropped = 0;
		this.pickedUp = 0;
	}
	
	private String playerName;
	private int playerId;
	private MaterialData material;
	private int dropped;
	private int pickedUp;
	
	@Override
	public void fetchData() {
		List<DBEntry> results = QueryUtils.select(
			TotalItemsTable.TableName.toString(),
			"*",
			TotalItemsTable.PlayerId + " = " + CachedData.getCachedPlayerId(playerName)
		);
		
		if(results.isEmpty()) QueryUtils.insert(TotalItemsTable.TableName.toString(), getValues());
		else {
			dropped = results.get(0).getValueAsInteger(TotalItemsTable.Dropped.toString());
			pickedUp = results.get(0).getValueAsInteger(TotalItemsTable.PickedUp.toString());
		}
	}

	@Override
	public boolean pushData() {
		return QueryUtils.update(
			TotalItemsTable.TableName.toString(),
			getValues(),
			TotalItemsTable.PlayerId + " = " + playerId,
			TotalItemsTable.MaterialId + " = " + material.getItemTypeId()
		);
	}
	
	@Override
	public String getDataLabel() { return DataLabel.TotalItems.getAlias() + ":" + playerName; }
	
	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TotalItemsTable.PlayerId.toString(), playerId);
		map.put(TotalItemsTable.MaterialId.toString(), material.getItemTypeId());
		map.put(TotalItemsTable.Dropped.toString(), dropped);
		map.put(TotalItemsTable.PickedUp.toString(), pickedUp);
		return map;
	}
	
	/**
	 * Adds the specified number of blocks to the total number of blocks destroyed
	 * @param blocks Items to add
	 */
	public void addBroken(int blocks) { dropped += blocks; }
	
	/**
	 * Adds the specified number of blocks to the total number of blocks pickedUp
	 * @param blocks Items to add
	 */
	public void addPickedUp(int blocks) { pickedUp += blocks; }

}
