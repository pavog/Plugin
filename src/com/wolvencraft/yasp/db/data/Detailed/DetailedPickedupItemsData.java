package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.detailed.DetailedPickedupItems;
import com.wolvencraft.yasp.util.Util;

public class DetailedPickedupItemsData implements _DetailedData {
	
	public DetailedPickedupItemsData(Player player, int playerId, ItemStack itemStack) {
		this.playerId = playerId;
		this.itemStack = itemStack;
		this.itemStack.setAmount(1);
		this.location = player.getLocation();
		this.timestamp = Util.getTimestamp();
	}
	
	private int playerId;
	private ItemStack itemStack;
	private Location location;
	private long timestamp;
	
	@Override
	public boolean pushData() {
		return QueryUtils.insert(
			DetailedPickedupItems.TableName.toString(),
			getValues()
		);
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DetailedPickedupItems.PlayerId.toString(), playerId);
		map.put(DetailedPickedupItems.MaterialId.toString(), itemStack.getTypeId());
		map.put(DetailedPickedupItems.MaterialData.toString(), itemStack.getData().getData());
		map.put(DetailedPickedupItems.World.toString(), location.getWorld().getName());
		map.put(DetailedPickedupItems.XCoord.toString(), location.getBlockX());
		map.put(DetailedPickedupItems.YCoord.toString(), location.getBlockY());
		map.put(DetailedPickedupItems.ZCoord.toString(), location.getBlockZ());
		map.put(DetailedPickedupItems.Timestamp.toString(), timestamp);
		return map;
	}

}
