package com.wolvencraft.yasp.db.data.detailed;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.CachedData;
import com.wolvencraft.yasp.db.tables.detailed.ItemsPickedUpTable;
import com.wolvencraft.yasp.util.Util;

public class ItemPickedUp implements DetailedDataHolder {
	
	private boolean onHold = false;
	
	public ItemPickedUp(Player player, Item item) {
		this.playerName = player.getPlayerListName();
		this.itemStack = item.getItemStack();
		this.location = player.getLocation();
		this.timestamp = Util.getCurrentTime().getTime();
	}
	
	private String playerName;
	private ItemStack itemStack;
	private Location location;
	private long timestamp;

	@Override
	public String getQuery() {
		if(onHold) return null;
		String query = "INSERT INTO " + ItemsPickedUpTable.TableName + " (" + ItemsPickedUpTable.MaterialId + ", " + ItemsPickedUpTable.PlayerId + ", " + ItemsPickedUpTable.World + ", " + ItemsPickedUpTable.XCoord + ", " + ItemsPickedUpTable.YCoord + ", " + ItemsPickedUpTable.ZCoord + ", " + ItemsPickedUpTable.Timestamp + ") " 
				+ "VALUES (" + itemStack.getTypeId() + ", " + CachedData.getCachedPlayerId(playerName) + ", " + location.getWorld().getName() + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ", " + timestamp + ")";
		return query;
	}

	@Override
	public boolean isOnHold() { return onHold; }

	@Override
	public void setOnHold(boolean onHold) { this.onHold = onHold; }

	@Override
	public boolean refresh() { return onHold; }

}
