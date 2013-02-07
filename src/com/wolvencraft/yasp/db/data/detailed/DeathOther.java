package com.wolvencraft.yasp.db.data.detailed;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.CachedData;
import com.wolvencraft.yasp.db.tables.detailed.PlayersDeathTable;
import com.wolvencraft.yasp.util.Util;

public class DeathOther implements DetailedDataHolder {
	
	private boolean onHold = false;
	
	public DeathOther(Player player, String deathCause) {
		this.playerName = player.getPlayerListName();
		this.deathCause = deathCause;
		this.location = player.getLocation();
		this.timestamp = Util.getCurrentTime().getTime();
	}
	
	private String playerName;
	private String deathCause;
	private Location location;
	private long timestamp;
	
	@Override
	public String getQuery() {
		if(onHold) return null;
		String query = "INSERT INTO " + PlayersDeathTable.TableName + " (" + PlayersDeathTable.PlayerId + ", " + PlayersDeathTable.Cause + ", " + PlayersDeathTable.World + ", " + PlayersDeathTable.XCoord + ", " + PlayersDeathTable.YCoord + ", " + PlayersDeathTable.ZCoord + ", " + PlayersDeathTable.Timestamp + ") " +
				"VALUES (" + CachedData.getCachedPlayerId(playerName) + ", " + deathCause + ", " + location.getWorld().getName() + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ", " + timestamp + ")";
		return query;
	}

	@Override
	public boolean isOnHold() { return onHold; }

	@Override
	public void setOnHold(boolean onHold) { this.onHold = onHold; }

	@Override
	public boolean refresh() { return onHold; }

}
