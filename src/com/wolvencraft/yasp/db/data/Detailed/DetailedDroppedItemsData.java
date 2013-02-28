package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.detailed.DetailedDroppedItems;
import com.wolvencraft.yasp.util.Util;

public class DetailedDroppedItemsData implements _DetailedData {
	
	public DetailedDroppedItemsData(Player player, int playerId, ItemStack itemStack) {
		this.playerId = playerId;
		this.itemStack = itemStack;
		this.location = player.getLocation();
		this.timestamp = Util.getCurrentTime().getTime();
	}
	
	private int playerId;
	private ItemStack itemStack;
	private Location location;
	private long timestamp;
	
	@Override
	public boolean pushData() {
		return QueryUtils.insert(
			DetailedDroppedItems.TableName.toString(),
			getValues()
		);
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DetailedDroppedItems.PlayerId.toString(), playerId);
		map.put(DetailedDroppedItems.MaterialId.toString(), itemStack.getTypeId());
		map.put(DetailedDroppedItems.MaterialData.toString(), itemStack.getData().getData());
		map.put(DetailedDroppedItems.World.toString(), location.getWorld().getName());
		map.put(DetailedDroppedItems.XCoord.toString(), location.getBlockX());
		map.put(DetailedDroppedItems.YCoord.toString(), location.getBlockY());
		map.put(DetailedDroppedItems.ZCoord.toString(), location.getBlockZ());
		map.put(DetailedDroppedItems.Timestamp.toString(), timestamp);
		return map;
	}

}
