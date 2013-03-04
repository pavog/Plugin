package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.material.MaterialData;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.detailed.DetailedPlacedBlocks;
import com.wolvencraft.yasp.util.Util;

public class DetailedPlacedBlocksData implements _DetailedData {
	
	public DetailedPlacedBlocksData(Location location, MaterialData materialData) {
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
			DetailedPlacedBlocks.TableName.toString(),
			getValues(playerId)
		);
	}

	@Override
	public Map<String, Object> getValues(int playerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DetailedPlacedBlocks.PlayerId.toString(), playerId);
		map.put(DetailedPlacedBlocks.MaterialId.toString(), materialData.getItemTypeId());
		map.put(DetailedPlacedBlocks.MaterialData.toString(), materialData.getData());
		map.put(DetailedPlacedBlocks.World.toString(), location.getWorld().getName());
		map.put(DetailedPlacedBlocks.XCoord.toString(), location.getBlockX());
		map.put(DetailedPlacedBlocks.YCoord.toString(), location.getBlockY());
		map.put(DetailedPlacedBlocks.ZCoord.toString(), location.getBlockZ());
		map.put(DetailedPlacedBlocks.Timestamp.toString(), timestamp);
		return map;
	}
	
}
