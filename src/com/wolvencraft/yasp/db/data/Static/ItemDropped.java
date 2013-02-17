package com.wolvencraft.yasp.db.data.Static;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Static.ItemsDroppedTable;
import com.wolvencraft.yasp.stats.DataCollector;
import com.wolvencraft.yasp.util.Util;

public class ItemDropped implements StaticData {
	
	private boolean onHold = false;
	
	public ItemDropped(Player player, ItemStack itemStack) {
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
		return QueryUtils.insert(ItemsDroppedTable.TableName.toString(), getValues());
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ItemsDroppedTable.PlayerId.toString(), playerId);
		map.put(ItemsDroppedTable.MaterialId.toString(), itemStack.getTypeId());
		map.put(ItemsDroppedTable.World.toString(), location.getWorld().getName());
		map.put(ItemsDroppedTable.XCoord.toString(), location.getBlockX());
		map.put(ItemsDroppedTable.YCoord.toString(), location.getBlockY());
		map.put(ItemsDroppedTable.ZCoord.toString(), location.getBlockZ());
		map.put(ItemsDroppedTable.Timestamp.toString(), timestamp);
		return map;
	}

	@Override
	public boolean isOnHold() { return onHold; }

	@Override
	public void setOnHold(boolean onHold) { this.onHold = onHold; }

	@Override
	public boolean refresh() { return onHold; }

}
