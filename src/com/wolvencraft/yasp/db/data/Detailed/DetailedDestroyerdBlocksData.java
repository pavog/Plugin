package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.detailed.DetailedDestroyedBlocks;
import com.wolvencraft.yasp.util.Util;

public class DetailedDestroyerdBlocksData implements _DetailedData {
	
	private boolean onHold = false;
	
	public DetailedDestroyerdBlocksData(Player player, MaterialData materialData) {
		this.playerId = DataCollector.getCachedPlayerId(player.getPlayerListName());
		this.materialData = materialData;
		this.timestamp = Util.getCurrentTime().getTime();
	}
	
	private int playerId;
	private MaterialData materialData;
	private long timestamp;

	@Override
	public boolean pushData() {
		return QueryUtils.insert(DetailedDestroyedBlocks.TableName.toString(), getValues());
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DetailedDestroyedBlocks.PlayerId.toString(), playerId);
		map.put(DetailedDestroyedBlocks.MaterialId.toString(), materialData.getItemTypeId());
		map.put(DetailedDestroyedBlocks.Timestamp.toString(), timestamp);
		return map;
	}
	
	@Override
	public boolean isOnHold() { return onHold; }

	@Override
	public void setOnHold(boolean onHold) { this.onHold = onHold; }

	@Override
	public boolean refresh() { return onHold; }

}
