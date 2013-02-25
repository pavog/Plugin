package com.wolvencraft.yasp.db.data.Detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Detailed.DetailedDroppedItems;
import com.wolvencraft.yasp.util.Util;

public class DetailedDroppedItemsData implements _DetailedData {
	
	private boolean onHold = false;
	
	public DetailedDroppedItemsData(Player player, ItemStack itemStack) {
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
		return QueryUtils.insert(DetailedDroppedItems.TableName.toString(), getValues());
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DetailedDroppedItems.PlayerId.toString(), playerId);
		map.put(DetailedDroppedItems.MaterialId.toString(), itemStack.getTypeId());
		map.put(DetailedDroppedItems.World.toString(), location.getWorld().getName());
		map.put(DetailedDroppedItems.XCoord.toString(), location.getBlockX());
		map.put(DetailedDroppedItems.YCoord.toString(), location.getBlockY());
		map.put(DetailedDroppedItems.ZCoord.toString(), location.getBlockZ());
		map.put(DetailedDroppedItems.Timestamp.toString(), timestamp);
		return map;
	}

	@Override
	public boolean isOnHold() { return onHold; }

	@Override
	public void setOnHold(boolean onHold) { this.onHold = onHold; }

	@Override
	public boolean refresh() { return onHold; }

}
