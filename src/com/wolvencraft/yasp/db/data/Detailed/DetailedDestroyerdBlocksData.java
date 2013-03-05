package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.detailed.DetailedDestroyedBlocks;
import com.wolvencraft.yasp.util.Util;

public class DetailedDestroyerdBlocksData implements _DetailedData {
	
	public DetailedDestroyerdBlocksData(Location location, Material materialType, byte data) {
		this.type = materialType.getId();
		this.data = data;
		this.location = location;
		this.timestamp = Util.getTimestamp();
	}
	
	private int type;
	private byte data;
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
		map.put(DetailedDestroyedBlocks.MaterialId.toString(), type);
		map.put(DetailedDestroyedBlocks.MaterialData.toString(), data);
		map.put(DetailedDestroyedBlocks.World.toString(), location.getWorld().getName());
		map.put(DetailedDestroyedBlocks.XCoord.toString(), location.getBlockX());
		map.put(DetailedDestroyedBlocks.YCoord.toString(), location.getBlockY());
		map.put(DetailedDestroyedBlocks.ZCoord.toString(), location.getBlockZ());
		map.put(DetailedDestroyedBlocks.Timestamp.toString(), timestamp);
		return map;
	}

}
