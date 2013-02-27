package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.detailed.DetailedUsedItems;
import com.wolvencraft.yasp.util.Util;

public class DetailedUsedItemsData implements _DetailedData {
	
	public DetailedUsedItemsData(Player player, ItemStack itemStack) {
		this.playerId = DataCollector.getCachedPlayerId(player.getPlayerListName());
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
			DetailedUsedItems.TableName.toString(),
			getValues()
		);
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DetailedUsedItems.PlayerId.toString(), playerId);
		map.put(DetailedUsedItems.MaterialId.toString(), itemStack.getTypeId());
		map.put(DetailedUsedItems.MaterialData.toString(), itemStack.getData().getData());
		map.put(DetailedUsedItems.World.toString(), location.getWorld().getName());
		map.put(DetailedUsedItems.XCoord.toString(), location.getBlockX());
		map.put(DetailedUsedItems.YCoord.toString(), location.getBlockY());
		map.put(DetailedUsedItems.ZCoord.toString(), location.getBlockZ());
		map.put(DetailedUsedItems.Timestamp.toString(), timestamp);
		return map;
	}

}
