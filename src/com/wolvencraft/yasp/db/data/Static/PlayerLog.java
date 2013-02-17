package com.wolvencraft.yasp.db.data.Static;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Static.PlayersLogTable;
import com.wolvencraft.yasp.util.Util;

public class PlayerLog implements StaticData {
	
	private boolean onHold = true;
	
	public PlayerLog(Player player, int playerId) {
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
		return QueryUtils.insert(PlayersLogTable.TableName.toString(), getValues());
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PlayersLogTable.PlayerId.toString(), playerId);
		map.put(PlayersLogTable.LoggedIn.toString(), login);
		map.put(PlayersLogTable.LoggedOut.toString(), logout);
		map.put(PlayersLogTable.World.toString(), location.getWorld().getName());
		map.put(PlayersLogTable.XCoord.toString(), location.getBlockX());
		map.put(PlayersLogTable.YCoord.toString(), location.getBlockY());
		map.put(PlayersLogTable.ZCoord.toString(), location.getBlockZ());
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
