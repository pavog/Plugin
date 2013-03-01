package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.detailed.DetailedDestroyedBlocks;
import com.wolvencraft.yasp.util.Util;

public class DetailedDestroyerdBlocksData implements _DetailedData {
	
	public DetailedDestroyerdBlocksData(Player player, int playerId, MaterialData materialData) {
		this.playerId = playerId;
		this.materialData = materialData;
		this.timestamp = Util.getTimestamp();
	}
	
	private int playerId;
	private MaterialData materialData;
	private long timestamp;

	@Override
	public boolean pushData() {
		return QueryUtils.insert(
			DetailedDestroyedBlocks.TableName.toString(),
			getValues()
		);
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DetailedDestroyedBlocks.PlayerId.toString(), playerId);
		map.put(DetailedDestroyedBlocks.MaterialId.toString(), materialData.getItemTypeId());
		map.put(DetailedDestroyedBlocks.MaterialData.toString(), materialData.getData());
		map.put(DetailedDestroyedBlocks.Timestamp.toString(), timestamp);
		return map;
	}

}
