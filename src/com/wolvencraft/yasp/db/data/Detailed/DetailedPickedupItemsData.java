package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Detailed;
import com.wolvencraft.yasp.util.Util;

public class DetailedPickedupItemsData implements _DetailedData {
	
	public DetailedPickedupItemsData(Location location, ItemStack itemStack) {
		this.type = itemStack.getTypeId();
		this.data = itemStack.getData().getData();
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
			Detailed.PickedupItems.TableName.toString(),
			getValues(playerId)
		);
	}

	@Override
	public Map<String, Object> getValues(int playerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Detailed.PickedupItems.PlayerId.toString(), playerId);
		map.put(Detailed.PickedupItems.MaterialId.toString(), type);
		map.put(Detailed.PickedupItems.MaterialData.toString(), data);
		map.put(Detailed.PickedupItems.World.toString(), location.getWorld().getName());
		map.put(Detailed.PickedupItems.XCoord.toString(), location.getBlockX());
		map.put(Detailed.PickedupItems.YCoord.toString(), location.getBlockY());
		map.put(Detailed.PickedupItems.ZCoord.toString(), location.getBlockZ());
		map.put(Detailed.PickedupItems.Timestamp.toString(), timestamp);
		return map;
	}

}
