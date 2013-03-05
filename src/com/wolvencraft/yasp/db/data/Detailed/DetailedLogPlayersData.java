package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Detailed;
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
			Detailed.LogPlayers.TableName.toString(),
			getValues(playerId)
		);
	}

	@Override
	public Map<String, Object> getValues(int playerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Detailed.LogPlayers.PlayerId.toString(), playerId);
		map.put(Detailed.LogPlayers.Timestamp.toString(), time);
		if(isLogin) map.put(Detailed.LogPlayers.IsLogin.toString(), 1);
		else map.put(Detailed.LogPlayers.IsLogin.toString(), 0);
		map.put(Detailed.LogPlayers.World.toString(), location.getWorld().getName());
		map.put(Detailed.LogPlayers.XCoord.toString(), location.getBlockX());
		map.put(Detailed.LogPlayers.YCoord.toString(), location.getBlockY());
		map.put(Detailed.LogPlayers.ZCoord.toString(), location.getBlockZ());
		return map;
	}

}
