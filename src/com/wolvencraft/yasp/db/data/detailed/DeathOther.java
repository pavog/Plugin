package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.wolvencraft.yasp.db.CachedData;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.detailed.PlayersDeathTable;
import com.wolvencraft.yasp.util.Util;

public class DeathOther implements DetailedDataHolder {
	
	private boolean onHold = false;
	
	public DeathOther(Player player, DamageCause deathCause) {
		this.playerName = player.getPlayerListName();
		this.playerId = CachedData.getCachedPlayerId(playerName);
		this.deathCause = deathCause.name();
		this.location = player.getLocation();
		this.timestamp = Util.getCurrentTime().getTime();
	}
	
	private String playerName;
	private int playerId;
	private String deathCause;
	private Location location;
	private long timestamp;

	@Override
	public boolean pushData() {
		return QueryUtils.insert(PlayersDeathTable.TableName.toString(), getValues());
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PlayersDeathTable.PlayerId.toString(), playerId);
		map.put(PlayersDeathTable.Cause.toString(), deathCause);
		map.put(PlayersDeathTable.World.toString(), location.getWorld().getName());
		map.put(PlayersDeathTable.XCoord.toString(), location.getBlockX());
		map.put(PlayersDeathTable.YCoord.toString(), location.getBlockY());
		map.put(PlayersDeathTable.ZCoord.toString(), location.getBlockZ());
		map.put(PlayersDeathTable.Timestamp.toString(), timestamp);
		return map;
	}

	@Override
	public boolean isOnHold() { return onHold; }

	@Override
	public void setOnHold(boolean onHold) { this.onHold = onHold; }

	@Override
	public boolean refresh() { return onHold; }

}
