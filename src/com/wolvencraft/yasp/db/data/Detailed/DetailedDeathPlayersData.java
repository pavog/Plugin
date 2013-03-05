package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Detailed;
import com.wolvencraft.yasp.util.Util;

public class DetailedDeathPlayersData implements _DetailedData {
	
	public DetailedDeathPlayersData(Location location, DamageCause deathCause) {
		this.deathCause = deathCause.name();
		this.location = location;
		this.timestamp = Util.getTimestamp();
	}
	
	private String deathCause;
	private Location location;
	private long timestamp;

	@Override
	public boolean pushData(int playerId) {
		return QueryUtils.insert(
			Detailed.DeathPlayers.TableName.toString(),
			getValues(playerId)
		);
	}

	@Override
	public Map<String, Object> getValues(int playerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Detailed.DeathPlayers.PlayerId.toString(), playerId);
		map.put(Detailed.DeathPlayers.Cause.toString(), deathCause);
		map.put(Detailed.DeathPlayers.World.toString(), location.getWorld().getName());
		map.put(Detailed.DeathPlayers.XCoord.toString(), location.getBlockX());
		map.put(Detailed.DeathPlayers.YCoord.toString(), location.getBlockY());
		map.put(Detailed.DeathPlayers.ZCoord.toString(), location.getBlockZ());
		map.put(Detailed.DeathPlayers.Timestamp.toString(), timestamp);
		return map;
	}

}
