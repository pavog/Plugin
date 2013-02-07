package com.wolvencraft.yasp.db.data.detailed;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.CachedData;
import com.wolvencraft.yasp.db.tables.detailed.PlayersLogTable;
import com.wolvencraft.yasp.util.Util;

public class PlayerLog implements DetailedDataHolder {
	
	private boolean onHold = true;
	
	public PlayerLog(Player player) {
		this.playerName = player.getPlayerListName();
		this.login = Util.getCurrentTime().getTime();
		this.logout = -1;
		this.location = player.getLocation();
	}
	
	private String playerName;
	private long login;
	private long logout;
	private Location location;
	
	@Override
	public String getQuery() {
		String query = "INSERT INTO " + PlayersLogTable.TableName + " (" + PlayersLogTable.PlayerId + ", " + PlayersLogTable.LoggedIn + ", " + PlayersLogTable.LoggedOut + ", " + PlayersLogTable.World + ", " + PlayersLogTable.XCoord + ", " + PlayersLogTable.YCoord + ", " + PlayersLogTable.ZCoord + ") " + 
				"VALUES (" + CachedData.getCachedPlayerId(playerName) + ", " + login + ", " + logout + ", " + location.getWorld().getName() + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ")";
		return query;
	}

	@Override
	public boolean isOnHold() { return onHold; }

	@Override
	public void setOnHold(boolean onHold) { this.onHold = onHold; }

	@Override
	public boolean refresh() {
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(!player.getPlayerListName().equals(playerName)) continue;
			return onHold;
		}
		this.logout = Util.getCurrentTime().getTime();
		onHold = false;
		return onHold;
	}

}
