package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Detailed;
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
			Detailed.DestroyedBlocks.TableName.toString(),
			getValues(playerId)
		);
	}

	@Override
	public Map<String, Object> getValues(int playerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Detailed.DestroyedBlocks.PlayerId.toString(), playerId);
		map.put(Detailed.DestroyedBlocks.MaterialId.toString(), type);
		map.put(Detailed.DestroyedBlocks.MaterialData.toString(), data);
		map.put(Detailed.DestroyedBlocks.World.toString(), location.getWorld().getName());
		map.put(Detailed.DestroyedBlocks.XCoord.toString(), location.getBlockX());
		map.put(Detailed.DestroyedBlocks.YCoord.toString(), location.getBlockY());
		map.put(Detailed.DestroyedBlocks.ZCoord.toString(), location.getBlockZ());
		map.put(Detailed.DestroyedBlocks.Timestamp.toString(), timestamp);
		return map;
	}

}
