package com.wolvencraft.yasp.db.data.Detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Detailed.DetailedDeathPlayers;
import com.wolvencraft.yasp.util.Util;

public class DetailedDeathPlayersData implements _DetailedData {
	
	private boolean onHold = false;
	
	public DetailedDeathPlayersData(Player player, DamageCause deathCause) {
		this.playerId = DataCollector.getCachedPlayerId(player.getPlayerListName());
		this.deathCause = deathCause.name();
		this.location = player.getLocation();
		this.timestamp = Util.getCurrentTime().getTime();
	}
	
	private int playerId;
	private String deathCause;
	private Location location;
	private long timestamp;

	@Override
	public boolean pushData() {
		return QueryUtils.insert(DetailedDeathPlayers.TableName.toString(), getValues());
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DetailedDeathPlayers.PlayerId.toString(), playerId);
		map.put(DetailedDeathPlayers.Cause.toString(), deathCause);
		map.put(DetailedDeathPlayers.World.toString(), location.getWorld().getName());
		map.put(DetailedDeathPlayers.XCoord.toString(), location.getBlockX());
		map.put(DetailedDeathPlayers.YCoord.toString(), location.getBlockY());
		map.put(DetailedDeathPlayers.ZCoord.toString(), location.getBlockZ());
		map.put(DetailedDeathPlayers.Timestamp.toString(), timestamp);
		return map;
	}

	@Override
	public boolean isOnHold() { return onHold; }

	@Override
	public void setOnHold(boolean onHold) { this.onHold = onHold; }

	@Override
	public boolean refresh() { return onHold; }

}
