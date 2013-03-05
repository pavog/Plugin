package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Detailed;
import com.wolvencraft.yasp.util.Util;

public class DetailedPlacedBlocksData implements _DetailedData {
	
	public DetailedPlacedBlocksData(Location location, Material materialType, byte data) {
		this.type = materialType.getId();
		this.data = data;
		this.location = location;
		this.timestamp = Util.getTimestamp();
	}
	
	private int type;
	private int data;
	private Location location;
	private long timestamp;

	@Override
	public boolean pushData(int playerId) {
		return QueryUtils.insert(
			Detailed.PlacedBlocks.TableName.toString(),
			getValues(playerId)
		);
	}

	@Override
	public Map<String, Object> getValues(int playerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Detailed.PlacedBlocks.PlayerId.toString(), playerId);
		map.put(Detailed.PlacedBlocks.MaterialId.toString(), type);
		map.put(Detailed.PlacedBlocks.MaterialData.toString(), data);
		map.put(Detailed.PlacedBlocks.World.toString(), location.getWorld().getName());
		map.put(Detailed.PlacedBlocks.XCoord.toString(), location.getBlockX());
		map.put(Detailed.PlacedBlocks.YCoord.toString(), location.getBlockY());
		map.put(Detailed.PlacedBlocks.ZCoord.toString(), location.getBlockZ());
		map.put(Detailed.PlacedBlocks.Timestamp.toString(), timestamp);
		return map;
	}
	
}
