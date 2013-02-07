package com.wolvencraft.yasp.db.data.detailed;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.CachedData;
import com.wolvencraft.yasp.db.tables.detailed.PVPTable;
import com.wolvencraft.yasp.util.Util;

public class DeathPVP implements DetailedDataHolder {
	
	private boolean onHold = false;
	
	public DeathPVP(Player player, Player victim, ItemStack weapon) {
		this.playerName = player.getPlayerListName();
		this.victimName = victim.getPlayerListName();
		this.weapon = weapon;
		this.location = victim.getLocation();
		this.timestamp = Util.getCurrentTime().getTime();
	}
	
	private String playerName;
	private String victimName;
	private ItemStack weapon;
	private Location location;
	private long timestamp;
	
	@Override
	public String getQuery() {
		if(onHold) return null;
		String query = "INSERT INTO " + PVPTable.TableName + " VALUES (" + PVPTable.PlayerID + ", " + PVPTable.VictimID + ", " + PVPTable.MaterialID + ", " + PVPTable.Cause + ", " + PVPTable.World + ", " + PVPTable.XCoord + ", " + PVPTable.YCoord + ", " + PVPTable.ZCoord + ", " + PVPTable.Timestamp + ") " + 
				"VALUES (" + CachedData.getCachedPlayerId(playerName) + ", " + CachedData.getCachedPlayerId(victimName) + ", " + weapon + ", " + "getting hit a bunch of times" + ", " + location.getWorld().getName() + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ", " + timestamp + ")";
		return query;
	}

	@Override
	public boolean isOnHold() { return onHold; }

	@Override
	public void setOnHold(boolean onHold) { this.onHold = onHold; }

	@Override
	public boolean refresh() { return onHold; }

}
