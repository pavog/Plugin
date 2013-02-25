package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.detailed.DetailedLogPlayers;
import com.wolvencraft.yasp.util.Util;

public class DetailedLogPlayersData implements _DetailedData {
	
	private boolean onHold = true;
	
	public DetailedLogPlayersData(Player player, int playerId) {
		playerName = player.getPlayerListName();
		this.playerId = playerId;
		this.login = Util.getCurrentTime().getTime();
		this.logout = -1;
		this.location = player.getLocation();
	}
	
	private String playerName;
	private int playerId;
	private long login;
	private long logout;
	private Location location;
	
	@Override
	public boolean pushData() {
		if(onHold && refresh()) return false;
		return QueryUtils.insert(DetailedLogPlayers.TableName.toString(), getValues());
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DetailedLogPlayers.PlayerId.toString(), playerId);
		map.put(DetailedLogPlayers.LoggedIn.toString(), login);
		map.put(DetailedLogPlayers.LoggedOut.toString(), logout);
		map.put(DetailedLogPlayers.World.toString(), location.getWorld().getName());
		map.put(DetailedLogPlayers.XCoord.toString(), location.getBlockX());
		map.put(DetailedLogPlayers.YCoord.toString(), location.getBlockY());
		map.put(DetailedLogPlayers.ZCoord.toString(), location.getBlockZ());
		return map;
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
