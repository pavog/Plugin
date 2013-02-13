package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.CachedData;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.detailed.ItemsDroppedTable;
import com.wolvencraft.yasp.util.Util;

public class ItemDropped implements DetailedDataHolder {
	
	private boolean onHold = false;
	
	public ItemDropped(Player player, Item item) {
		this.playerName = player.getPlayerListName();
		this.playerId = CachedData.getCachedPlayerId(playerName);
		this.itemStack = item.getItemStack();
		this.location = player.getLocation();
		this.timestamp = Util.getCurrentTime().getTime();
	}
	
	private String playerName;
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
