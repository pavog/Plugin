package com.wolvencraft.yasp.db.data.Static;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Static.ItemsPickedUpTable;
import com.wolvencraft.yasp.stats.DataCollector;
import com.wolvencraft.yasp.util.Util;

public class ItemPickedUp implements StaticData {
	
	private boolean onHold = false;
	
	public ItemPickedUp(Player player, ItemStack itemStack) {
		this.playerId = DataCollector.getCachedPlayerId(player.getPlayerListName());
		this.itemStack = itemStack;
		this.itemStack.setAmount(1);
		this.location = player.getLocation();
		this.timestamp = Util.getCurrentTime().getTime();
	}
	
	private int playerId;
	private ItemStack itemStack;
	private Location location;
	private long timestamp;
	
	@Override
	public boolean pushData() {
		return QueryUtils.insert(ItemsPickedUpTable.TableName.toString(), getValues());
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ItemsPickedUpTable.PlayerId.toString(), playerId);
		map.put(ItemsPickedUpTable.MaterialId.toString(), itemStack.getTypeId());
		map.put(ItemsPickedUpTable.World.toString(), location.getWorld().getName());
		map.put(ItemsPickedUpTable.XCoord.toString(), location.getBlockX());
		map.put(ItemsPickedUpTable.YCoord.toString(), location.getBlockY());
		map.put(ItemsPickedUpTable.ZCoord.toString(), location.getBlockZ());
		map.put(ItemsPickedUpTable.Timestamp.toString(), timestamp);
		return map;
	}

	@Override
	public boolean isOnHold() { return onHold; }

	@Override
	public void setOnHold(boolean onHold) { this.onHold = onHold; }

	@Override
	public boolean refresh() { return onHold; }

}
