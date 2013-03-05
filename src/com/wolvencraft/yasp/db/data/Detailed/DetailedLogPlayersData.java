package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.detailed.DetailedLogPlayers;
import com.wolvencraft.yasp.util.Util;

public class DetailedLogPlayersData implements _DetailedData {
	
	public DetailedLogPlayersData(Location location, boolean isLogin) {
		this.time = Util.getTimestamp();
		this.isLogin = isLogin;
		this.location = location;
	}
	
	private long time;
	private boolean isLogin;
	private Location location;
	
	@Override
	public boolean pushData(int playerId) {
		return QueryUtils.insert(
			DetailedLogPlayers.TableName.toString(),
			getValues(playerId)
		);
	}

	@Override
	public Map<String, Object> getValues(int playerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DetailedLogPlayers.PlayerId.toString(), playerId);
		map.put(DetailedLogPlayers.Timestamp.toString(), time);
		if(isLogin) map.put(DetailedLogPlayers.IsLogin.toString(), 1);
		else map.put(DetailedLogPlayers.IsLogin.toString(), 0);
		map.put(DetailedLogPlayers.World.toString(), location.getWorld().getName());
		map.put(DetailedLogPlayers.XCoord.toString(), location.getBlockX());
		map.put(DetailedLogPlayers.YCoord.toString(), location.getBlockY());
		map.put(DetailedLogPlayers.ZCoord.toString(), location.getBlockZ());
		return map;
	}

}
