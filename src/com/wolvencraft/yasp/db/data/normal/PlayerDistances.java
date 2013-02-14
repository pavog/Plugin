package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.CachedData;
import com.wolvencraft.yasp.db.DBEntry;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal.PlayersDistanceTable;

/**
 * Represents the distances a player travelled.
 * Only one entry per player is allowed.
 * @author bitWolfy
 *
 */
public class PlayerDistances implements DataHolder {
	
	/**
	 * Default constructor. Takes in the Player object and pulls corresponding values from the remote database.<br />
	 * If no data is found in the database, the default values are inserted.
	 * @param player <b>Player</b> tracked player
	 */
	public PlayerDistances(Player player) {
		this.playerName = player.getPlayerListName();
		this.playerId = CachedData.getCachedPlayerId(playerName);
		this.foot = 0;
		this.boat = 0;
		this.minecart = 0;
		this.pig = 0;
		
		fetchData();
	}
	
	private String playerName;
	private int playerId;
	private int foot;
	private int boat;
	private int minecart;
	private int pig;

	@Override
	public String getDataLabel() { return DataLabel.PlayerDistance.getAlias() + ":" + playerName; }
	
	@Override
	public void fetchData() {
		List<DBEntry> results = QueryUtils.select(
			PlayersDistanceTable.TableName.toString(),
			"*",
			PlayersDistanceTable.PlayerId.toString() + " = " + CachedData.getCachedPlayerId(playerName)
		);
		if(results.isEmpty()) QueryUtils.insert(PlayersDistanceTable.TableName.toString(), getValues());
		else {
			foot = results.get(0).getValueAsInteger(PlayersDistanceTable.Foot.toString());
			boat = results.get(0).getValueAsInteger(PlayersDistanceTable.Boat.toString());
			minecart = results.get(0).getValueAsInteger(PlayersDistanceTable.Minecart.toString());
			pig = results.get(0).getValueAsInteger(PlayersDistanceTable.Pig.toString());
		}
	}

	@Override
	public boolean pushData() {
		return QueryUtils.update(PlayersDistanceTable.TableName.toString(),
			getValues(),
			PlayersDistanceTable.PlayerId + " = " + playerId
		);
	}
	
	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> valueMap = new HashMap<String, Object>();
		valueMap.put(PlayersDistanceTable.Foot.toString(), foot);
		valueMap.put(PlayersDistanceTable.Boat.toString(), boat);
		valueMap.put(PlayersDistanceTable.Minecart.toString(), minecart);
		valueMap.put(PlayersDistanceTable.Pig.toString(), pig);
		return valueMap;
	}

	@Override
	public boolean equals(DataHolder holder) {
		return ((holder instanceof PlayerDistances) && (holder.getPlayerName().equals(playerName)));
	}

	@Override
	public boolean equals(String... arguments) {
		return arguments[0].equals(playerName);
	}
	
	@Override
	public String getPlayerName() { return playerName; }
	
	/**
	 * Increments the distance travelled by foot by the specified amount.
	 * @param distance Distance to add to the statistics
	 */
	public void addFootDistance(int distance) { foot += distance; }
	
	/**
	 * Increments the distance travelled by boat by the specified amount.
	 * @param distance Distance to add to the statistics
	 */
	public void addBoatDistance(int distance) { boat += distance; }
	
	/**
	 * Increments the distance travelled by minecart by the specified amount.
	 * @param distance Distance to add to the statistics
	 */
	public void addMinecartDistance(int distance) { minecart += distance; }
	
	/**
	 * Increments the distance travelled by pig by the specified amount.
	 * @param distance Distance to add to the statistics
	 */
	public void addPigDistance (int distance) { pig += distance; }
	
}
