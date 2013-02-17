package com.wolvencraft.yasp.db.data.Static;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Static.BlocksDestroyedTable;
import com.wolvencraft.yasp.stats.DataCollector;
import com.wolvencraft.yasp.util.Util;

public class BlockDestroyed implements StaticData {
	
	private boolean onHold = false;
	
	public BlockDestroyed(Player player, MaterialData materialData) {
		this.playerId = DataCollector.getCachedPlayerId(player.getPlayerListName());
		this.materialData = materialData;
		this.timestamp = Util.getCurrentTime().getTime();
	}
	
	private int playerId;
	private MaterialData materialData;
	private long timestamp;

	@Override
	public boolean pushData() {
		return QueryUtils.insert(BlocksDestroyedTable.TableName.toString(), getValues());
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(BlocksDestroyedTable.PlayerId.toString(), playerId);
		map.put(BlocksDestroyedTable.MaterialId.toString(), materialData.getItemTypeId());
		map.put(BlocksDestroyedTable.Timestamp.toString(), timestamp);
		return map;
	}
	
	@Override
	public boolean isOnHold() { return onHold; }

	@Override
	public void setOnHold(boolean onHold) { this.onHold = onHold; }

	@Override
	public boolean refresh() { return onHold; }

}
