package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.detailed.DetailedPickedupItems;
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
			DetailedPickedupItems.TableName.toString(),
			getValues(playerId)
		);
	}

	@Override
	public Map<String, Object> getValues(int playerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DetailedPickedupItems.PlayerId.toString(), playerId);
		map.put(DetailedPickedupItems.MaterialId.toString(), type);
		map.put(DetailedPickedupItems.MaterialData.toString(), data);
		map.put(DetailedPickedupItems.World.toString(), location.getWorld().getName());
		map.put(DetailedPickedupItems.XCoord.toString(), location.getBlockX());
		map.put(DetailedPickedupItems.YCoord.toString(), location.getBlockY());
		map.put(DetailedPickedupItems.ZCoord.toString(), location.getBlockZ());
		map.put(DetailedPickedupItems.Timestamp.toString(), timestamp);
		return map;
	}

}
