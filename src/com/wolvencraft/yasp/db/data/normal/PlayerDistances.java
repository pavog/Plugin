package com.wolvencraft.yasp.db.data.normal;

import java.util.List;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.CachedData;
import com.wolvencraft.yasp.db.DBEntry;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal.PlayersDistanceTable;

public class PlayerDistances implements DataHolder {
	
	public PlayerDistances(Player player) {
		this.playerName = player.getPlayerListName();
		foot = 0;
		boat = 0;
		minecart = 0;
		pig = 0;
	}
	
	private String playerName;
	private int playerId;
	private int foot;
	private int boat;
	private int minecart;
	private int pig;
	
	@Override
	public void fetchData() {
		List<DBEntry> results = QueryUtils.fetchData("SELECT * FROM " + PlayersDistanceTable.TableName + " WHERE " + PlayersDistanceTable.PlayerId + " = " + CachedData.getCachedPlayerId(playerName));
		if(results.isEmpty()) return;
		foot = results.get(0).getValueAsInteger(PlayersDistanceTable.Foot.getColumnName());
		boat = results.get(0).getValueAsInteger(PlayersDistanceTable.Boat.getColumnName());
		minecart = results.get(0).getValueAsInteger(PlayersDistanceTable.Minecart.getColumnName());
		pig = results.get(0).getValueAsInteger(PlayersDistanceTable.Pig.getColumnName());
	}

	@Override
	public void pushData() {
		String query = "INSERT";
		
	}
	
	public void addFootDistance(int distance) { foot += distance; }
	public void addBoatDistance(int distance) { boat += distance; }
	public void addMinecartDistance(int distance) { minecart += distance; }
	public void addPigDistance (int distance) { pig += distance; }
	
	@Override
	public String getDataLabel() { return DataLabel.PlayerDistance.getAlias() + ":" + playerName; }
}
