package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.detailed.DetailedLogPlayers;
import com.wolvencraft.yasp.util.Util;

public class DetailedLogPlayersData implements _DetailedData {
	
	public DetailedLogPlayersData(Player player, int playerId, boolean isLogin) {
		this.playerId = playerId;
		this.time = Util.getTimestamp();
		this.isLogin = isLogin;
		this.location = player.getLocation();
	}
	
	private int playerId;
	private long time;
	private boolean isLogin;
	private Location location;
	
	@Override
	public boolean pushData() {
		return QueryUtils.insert(
			DetailedLogPlayers.TableName.toString(),
			getValues()
		);
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DetailedLogPlayers.PlayerId.toString(), playerId);
		map.put(DetailedLogPlayers.Time.toString(), time);
		if(isLogin) map.put(DetailedLogPlayers.IsLogin.toString(), 1);
		else map.put(DetailedLogPlayers.IsLogin.toString(), 0);
		map.put(DetailedLogPlayers.World.toString(), location.getWorld().getName());
		map.put(DetailedLogPlayers.XCoord.toString(), location.getBlockX());
		map.put(DetailedLogPlayers.YCoord.toString(), location.getBlockY());
		map.put(DetailedLogPlayers.ZCoord.toString(), location.getBlockZ());
		return map;
	}

}
