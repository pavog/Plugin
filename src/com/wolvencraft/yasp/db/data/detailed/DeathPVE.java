package com.wolvencraft.yasp.db.data.detailed;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.CachedData;
import com.wolvencraft.yasp.db.tables.detailed.PVETable;
import com.wolvencraft.yasp.util.Util;

public class DeathPVE implements DetailedDataHolder {
	
	private boolean onHold = false;
	
	public DeathPVE(Player player, EntityType creatureType, ItemStack weapon, boolean playerKilled) {
		this.playerName = player.getPlayerListName();
		this.creatureType = creatureType;
		this.weapon = weapon;
		this.location = player.getLocation();
		this.playerKilled = playerKilled;
		this.timestamp = Util.getCurrentTime().getTime();
	}
	
	private String playerName;
	private EntityType creatureType;
	private ItemStack weapon;
	private Location location;
	private boolean playerKilled;
	private long timestamp;
	
	@Override
	public String getQuery() {
		if(onHold) return null;
		String query = "INSERT INTO " + PVETable.TableName + " (" + PVETable.PlayerID + ", " + PVETable.CreatureID + ", " + PVETable.MaterialID + ", " + PVETable.Cause + ", " + PVETable.World + ", " + PVETable.XCoord + ", " + PVETable.YCoord + ", " + PVETable.ZCoord + ", " + PVETable.Timestamp + ", " + PVETable.PlayerKilled + ") " +
				"VALUES (" + CachedData.getCachedPlayerId(playerName) + ", " + creatureType.getName() + ", " + weapon.getTypeId() + ", " + "getting hit a bunch of times" + ", " + location.getWorld().getName() + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ", " + timestamp + ", " + playerKilled + ")";;
		return query;
	}

	@Override
	public boolean isOnHold() { return onHold; }

	@Override
	public void setOnHold(boolean onHold) { this.onHold = onHold; }

	@Override
	public boolean refresh() { return onHold; }

}
