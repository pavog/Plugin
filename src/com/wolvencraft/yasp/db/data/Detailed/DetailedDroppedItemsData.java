package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Detailed;
import com.wolvencraft.yasp.util.Util;

public class DetailedDroppedItemsData implements _DetailedData {
	
	public DetailedDroppedItemsData(Location location, ItemStack itemStack) {
		this.type = itemStack.getTypeId();
		this.data = itemStack.getData().getData();
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
			Detailed.DroppedItems.TableName.toString(),
			getValues(playerId)
		);
	}

	@Override
	public Map<String, Object> getValues(int playerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Detailed.DroppedItems.PlayerId.toString(), playerId);
		map.put(Detailed.DroppedItems.MaterialId.toString(), type);
		map.put(Detailed.DroppedItems.MaterialData.toString(), data);
		map.put(Detailed.DroppedItems.World.toString(), location.getWorld().getName());
		map.put(Detailed.DroppedItems.XCoord.toString(), location.getBlockX());
		map.put(Detailed.DroppedItems.YCoord.toString(), location.getBlockY());
		map.put(Detailed.DroppedItems.ZCoord.toString(), location.getBlockZ());
		map.put(Detailed.DroppedItems.Timestamp.toString(), timestamp);
		return map;
	}

}
