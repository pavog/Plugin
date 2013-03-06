package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Normal;

/**
 * Represents the distances a player traveled.
 * Only one entry per player is allowed.
 * @author bitWolfy
 *
 */
public class PlayerDistances implements _NormalData {
	
	/**
	 * Default constructor. Takes in the Player object and pulls corresponding values from the remote database.<br />
	 * If no data is found in the database, the default values are inserted.
	 * @param player <b>Player</b> tracked player
	 */
	public PlayerDistances() {
		this.foot = 0;
		this.swimmed = 0;
		this.boat = 0;
		this.minecart = 0;
		this.pig = 0;
	}
	
	private double foot;
	private double swimmed;
	private double boat;
	private double minecart;
	private double pig;
	
	@Override
	public void fetchData(int playerId) {
		List<QueryResult> results = QueryUtils.select(
			Normal.DistancePlayers.TableName.toString(),
			new String[] {"*"},
			new String[] { Normal.DistancePlayers.PlayerId.toString(), playerId + ""}
		);
		if(results.isEmpty()) QueryUtils.insert(Normal.DistancePlayers.TableName.toString(), getValues(playerId));
		else {
			foot = results.get(0).getValueAsInteger(Normal.DistancePlayers.Foot.toString());
			swimmed = results.get(0).getValueAsInteger(Normal.DistancePlayers.Swimmed.toString());
			boat = results.get(0).getValueAsInteger(Normal.DistancePlayers.Boat.toString());
			minecart = results.get(0).getValueAsInteger(Normal.DistancePlayers.Minecart.toString());
			pig = results.get(0).getValueAsInteger(Normal.DistancePlayers.Pig.toString());
		}
	}

	@Override
	public boolean pushData(int playerId) {
		return QueryUtils.update(Normal.DistancePlayers.TableName.toString(),
			getValues(playerId),
			new String[] { Normal.DistancePlayers.PlayerId.toString(), playerId + ""}
		);
	}
	
	@Override
	public Map<String, Object> getValues(int playerId) {
		Map<String, Object> valueMap = new HashMap<String, Object>();
		valueMap.put(Normal.DistancePlayers.PlayerId.toString(), playerId);
		valueMap.put(Normal.DistancePlayers.Foot.toString(), foot);
		valueMap.put(Normal.DistancePlayers.Swimmed.toString(), swimmed);
		valueMap.put(Normal.DistancePlayers.Boat.toString(), boat);
		valueMap.put(Normal.DistancePlayers.Minecart.toString(), minecart);
		valueMap.put(Normal.DistancePlayers.Pig.toString(), pig);
		return valueMap;
	}
	
	/**
	 * Increments the distance traveled by foot by the specified amount.
	 * @param distance Distance to add to the statistics
	 */
	public void addFootDistance(double distance) { foot += distance; }
	
	/**
	 * Increments the distance swimmed by the specified amount.
	 * @param distance Distance to add to the statistics
	 */
	public void addSwimmedDistance(double distance) { swimmed += distance; }
	
	/**
	 * Increments the distance traveled by boat by the specified amount.
	 * @param distance Distance to add to the statistics
	 */
	public void addBoatDistance(double distance) { boat += distance; }
	
	/**
	 * Increments the distance traveled by minecart by the specified amount.
	 * @param distance Distance to add to the statistics
	 */
	public void addMinecartDistance(double distance) { minecart += distance; }
	
	/**
	 * Increments the distance traveled by pig by the specified amount.
	 * @param distance Distance to add to the statistics
	 */
	public void addPigDistance (double distance) { pig += distance; }
	
}
