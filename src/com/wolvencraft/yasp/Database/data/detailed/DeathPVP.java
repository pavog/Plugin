package com.wolvencraft.yasp.Database.data.detailed;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.Database.CachedData;
import com.wolvencraft.yasp.Database.tables.detailed.PVPTable;
import com.wolvencraft.yasp.Utils.Util;

public class DeathPVP implements DetailedData {
	
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
