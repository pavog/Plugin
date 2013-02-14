package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.wolvencraft.yasp.db.CachedData;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.detailed.BlocksPlacedTable;
import com.wolvencraft.yasp.util.Util;

public class BlockPlaced implements DetailedDataHolder {
	
	private boolean onHold = false;
	
	public BlockPlaced(Player player, MaterialData materialData) {
		this.playerName = player.getPlayerListName();
		this.playerId = CachedData.getCachedPlayerId(playerName);
		this.materialData = materialData;
		this.timestamp = Util.getCurrentTime().getTime();
	}
	
	private String playerName;
	private int playerId;
	private MaterialData materialData;
	private long timestamp;

	@Override
	public boolean pushData() {
		return QueryUtils.insert(BlocksPlacedTable.TableName.toString(), getValues());
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(BlocksPlacedTable.PlayerId.toString(), playerId);
		map.put(BlocksPlacedTable.MaterialId.toString(), materialData.getItemTypeId());
		map.put(BlocksPlacedTable.Timestamp.toString(), timestamp);
		return map;
	}

	@Override
	public boolean isOnHold() { return onHold; }

	@Override
	public void setOnHold(boolean onHold) { this.onHold = onHold; }

	@Override
	public boolean refresh() { return onHold; }
	
}