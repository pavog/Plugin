package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Detailed;
import com.wolvencraft.yasp.util.Util;

public class DetailedUsedItemsData implements _DetailedData {
	
	public DetailedUsedItemsData(Location location, ItemStack itemStack) {
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
			Detailed.UsedItems.TableName.toString(),
			getValues(playerId)
		);
	}

	@Override
	public Map<String, Object> getValues(int playerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Detailed.UsedItems.PlayerId.toString(), playerId);
		map.put(Detailed.UsedItems.MaterialId.toString(), type);
		map.put(Detailed.UsedItems.MaterialData.toString(), data);
		map.put(Detailed.UsedItems.World.toString(), location.getWorld().getName());
		map.put(Detailed.UsedItems.XCoord.toString(), location.getBlockX());
		map.put(Detailed.UsedItems.YCoord.toString(), location.getBlockY());
		map.put(Detailed.UsedItems.ZCoord.toString(), location.getBlockZ());
		map.put(Detailed.UsedItems.Timestamp.toString(), timestamp);
		return map;
	}

}
