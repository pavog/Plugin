package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.material.MaterialData;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.detailed.DetailedDestroyedBlocks;
import com.wolvencraft.yasp.util.Util;

public class DetailedDestroyerdBlocksData implements _DetailedData {
	
	public DetailedDestroyerdBlocksData(Location location, MaterialData materialData) {
		this.materialData = materialData;
		this.location = location;
		this.timestamp = Util.getTimestamp();
	}
	
	private MaterialData materialData;
	private Location location;
	private long timestamp;

	@Override
	public boolean pushData(int playerId) {
		return QueryUtils.insert(
			DetailedDestroyedBlocks.TableName.toString(),
			getValues(playerId)
		);
	}

	@Override
	public Map<String, Object> getValues(int playerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DetailedDestroyedBlocks.PlayerId.toString(), playerId);
		map.put(DetailedDestroyedBlocks.MaterialId.toString(), materialData.getItemTypeId());
		map.put(DetailedDestroyedBlocks.MaterialData.toString(), materialData.getData());
		map.put(DetailedDestroyedBlocks.World.toString(), location.getWorld().getName());
		map.put(DetailedDestroyedBlocks.XCoord.toString(), location.getBlockX());
		map.put(DetailedDestroyedBlocks.YCoord.toString(), location.getBlockY());
		map.put(DetailedDestroyedBlocks.ZCoord.toString(), location.getBlockZ());
		map.put(DetailedDestroyedBlocks.Timestamp.toString(), timestamp);
		return map;
	}

}
