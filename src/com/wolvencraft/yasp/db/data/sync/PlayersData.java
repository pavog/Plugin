package com.wolvencraft.yasp.db.data.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Detailed;
import com.wolvencraft.yasp.db.tables.Normal.DistancePlayersTable;
import com.wolvencraft.yasp.db.tables.Normal.MiscInfoPlayersTable;
import com.wolvencraft.yasp.db.tables.Normal.PlayersTable;
import com.wolvencraft.yasp.util.Util;

public class PlayersData implements _DataStore {
	
	private int playerId;
	private Players generalData;
	private DistancePlayers distanceData;
	private MiscInfoPlayers miscData;
	private List<DetailedData> detailedData;
	
	public PlayersData(Player player, int playerId) {
		this.playerId = playerId;
		generalData = new Players(playerId, player);
		distanceData = new DistancePlayers(playerId);
		miscData = new MiscInfoPlayers(playerId, player);
		
		detailedData = new ArrayList<DetailedData>();
	}
	
	@Override
	public List<NormalData> getNormalData() { return null; }
	
	@Override
	public List<DetailedData> getDetailedData() {
		List<DetailedData> temp = new ArrayList<DetailedData>();
		for(DetailedData value : detailedData) temp.add(value);
		return temp;
	}
	
	@Override
	public void sync() {
		generalData.pushData(playerId);
		distanceData.pushData(playerId);
		miscData.pushData(playerId);
		
		for(DetailedData entry : getDetailedData()) {
			if(entry.pushData(playerId)) detailedData.remove(entry);
		}
	}
	
	@Override
	public void dump() {
		for(DetailedData entry : getDetailedData()) {
			detailedData.remove(entry);
		}
	}
	
	public Players general() { return generalData; }
	public DistancePlayers distance() { return distanceData; }
	public MiscInfoPlayers misc() { return miscData; }
	
	/**
	 * Returns the default values to create a placeholder entry when initializing a new user.
	 * @param name Name of the new player
	 * @return Map with placeholder data
	 */
	public static Map<String, Object> getDefaultValues(String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PlayersTable.Name.toString(), name);
		return map;
	}
	
	/**
	 * Registers player logging in with all corresponding statistics trackers.<br />
	 * Player's online status is updated in the database instantly.
	 */
	public void login(Location location) {
		generalData.setOnline(true);
		detailedData.add(new DetailedLogPlayersEntry(location, true));
		QueryUtils.update(
			PlayersTable.TableName.toString(),
			PlayersTable.Online.toString(),
			1 + "",
			new String[] {PlayersTable.PlayerId.toString(), playerId + ""}
		);
	}
	
	/**
	 * Registers player logging out with all corresponding statistics trackers.<br />
	 * Player's online status is updated in the database instantly.
	 */
	public void logout(Location location) {
		generalData.setOnline(false);
		detailedData.add(new DetailedLogPlayersEntry(location, false));
		QueryUtils.update(
				PlayersTable.TableName.toString(),
				PlayersTable.Online.toString(),
				0 + "",
				new String[] {PlayersTable.PlayerId.toString(), playerId + ""}
			);
	}
	
	/**
	 * Represents the Player data that is being tracked.<br />
	 * Each entry must have a unique player name.
	 * @author bitWolfy
	 *
	 */
	public class Players implements NormalData {
		
		public Players (int playerId, Player player) {
			this.playerName = player.getPlayerListName();
			
			this.online = true;
			this.sessionStart = Util.getTimestamp();
			this.firstJoin = Util.getTimestamp();
			this.logins = 0;
			
			fetchData(playerId);
		}
		
		private String playerName;
		
		private boolean online;
		private long sessionStart;
		private long firstJoin;
		private int logins;
		
		@Override
		public void fetchData(int playerId) {
			List<QueryResult> results = QueryUtils.select(
				PlayersTable.TableName.toString(),
				new String[] {PlayersTable.PlayerId.toString(), PlayersTable.Logins.toString()},
				new String[] { PlayersTable.PlayerId.toString(), playerId + ""}
			);
			if(results.isEmpty()) QueryUtils.insert(PlayersTable.TableName.toString(), getValues(playerId));
			else {
				logins = results.get(0).getValueAsInteger(PlayersTable.Logins.toString()) + 1;
			}
		}

		@Override
		public boolean pushData(int playerId) {
			boolean result = QueryUtils.update(
				PlayersTable.TableName.toString(),
				getValues(playerId), 
				new String[] { PlayersTable.PlayerId.toString(), playerId + ""}
			);
			fetchData(playerId);
			return result;
		}

		@Override
		public Map<String, Object> getValues(int playerId) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(PlayersTable.PlayerId.toString(), playerId);
			map.put(PlayersTable.Name.toString(), playerName);
			if(online) map.put(PlayersTable.Online.toString(), 1);
			else map.put(PlayersTable.Online.toString(), 0);
			map.put(PlayersTable.SessionStart.toString(), sessionStart);
			map.put(PlayersTable.FirstLogin.toString(), firstJoin);
			map.put(PlayersTable.Logins.toString(), logins);
			return map;
		}
		
		/**
		 * Returns the player name
		 * @return Player name
		 */
		public String getName() { return playerName; }
		
		/**
		 * Changes the online status of the player
		 * @param online New online status
		 */
		public void setOnline(boolean online) { this.online = online; }
	}
	
	/**
	 * Represents the distances a player traveled.
	 * Only one entry per player is allowed.
	 * @author bitWolfy
	 *
	 */
	public class DistancePlayers implements NormalData {
		
		/**
		 * Default constructor. Takes in the Player object and pulls corresponding values from the remote database.<br />
		 * If no data is found in the database, the default values are inserted.
		 * @param player <b>Player</b> tracked player
		 */
		public DistancePlayers(int playerId) {
			this.foot = 0;
			this.swimmed = 0;
			this.boat = 0;
			this.minecart = 0;
			this.pig = 0;
			
			fetchData(playerId);
		}
		
		private double foot;
		private double swimmed;
		private double boat;
		private double minecart;
		private double pig;
		
		@Override
		public void fetchData(int playerId) {
			List<QueryResult> results = QueryUtils.select(
				DistancePlayersTable.TableName.toString(),
				new String[] {"*"},
				new String[] { DistancePlayersTable.PlayerId.toString(), playerId + ""}
			);
			if(results.isEmpty()) QueryUtils.insert(DistancePlayersTable.TableName.toString(), getValues(playerId));
			else {
				foot = results.get(0).getValueAsInteger(DistancePlayersTable.Foot.toString());
				swimmed = results.get(0).getValueAsInteger(DistancePlayersTable.Swimmed.toString());
				boat = results.get(0).getValueAsInteger(DistancePlayersTable.Boat.toString());
				minecart = results.get(0).getValueAsInteger(DistancePlayersTable.Minecart.toString());
				pig = results.get(0).getValueAsInteger(DistancePlayersTable.Pig.toString());
			}
		}

		@Override
		public boolean pushData(int playerId) {
			boolean result = QueryUtils.update(DistancePlayersTable.TableName.toString(),
				getValues(playerId),
				new String[] { DistancePlayersTable.PlayerId.toString(), playerId + ""}
			);
			fetchData(playerId);
			return result;
		}
		
		@Override
		public Map<String, Object> getValues(int playerId) {
			Map<String, Object> valueMap = new HashMap<String, Object>();
			valueMap.put(DistancePlayersTable.PlayerId.toString(), playerId);
			valueMap.put(DistancePlayersTable.Foot.toString(), foot);
			valueMap.put(DistancePlayersTable.Swimmed.toString(), swimmed);
			valueMap.put(DistancePlayersTable.Boat.toString(), boat);
			valueMap.put(DistancePlayersTable.Minecart.toString(), minecart);
			valueMap.put(DistancePlayersTable.Pig.toString(), pig);
			return valueMap;
		}
		
		/**
		 * Increments the distance traveled by foot.
		 * @param distance Additional distance traveled by foot.
		 */
		public void addDistanceFoot(double distance) { foot += distance; }
		
		/**
		 * Increments the distance swimmed.
		 * @param distance Additional distance swimmed.
		 */
		public void addDistanceSwimmed(double distance) { swimmed += distance; }
		
		/**
		 * Increments the distance traveled by boat.
		 * @param distance Additional distance traveled by boat
		 */
		public void addDistanceBoat(double distance) { boat += distance; }
		
		/**
		 * Increments the distance traveled by minecart.
		 * @param distance Additional distance traveled by minecart
		 */
		public void addDistanceMinecart(double distance) { minecart += distance; }
		
		/**
		 * Increments the distance traveled by pig.
		 * @param distance Additional distance traveled by pig
		 */
		public void addDistancePig(double distance) { pig += distance; }
		
	}
	
	public class MiscInfoPlayers implements NormalData {
		
		public MiscInfoPlayers(int playerId, Player player) {
			this.playerName = player.getPlayerListName();
			
			this.gamemode = 0;
			this.expPercent = player.getExp();
			this.expTotal = player.getTotalExperience();
			this.expLevel = player.getLevel();
			this.foodLevel = player.getFoodLevel();
			this.healthLevel = player.getHealth();
			
			this.fishCaught = 0;
			this.timesKicked = 0;
			this.eggsThrown = 0;
			this.foodEaten = 0;
			this.arrowsShot = 0;
			this.damageTaken = 0;
			this.wordsSaid = 0;
			this.commandsSent = 0;
			
			fetchData(playerId);
		}
		
		private String playerName;
		
		private int gamemode;
		private double expPercent;
		private int expTotal;
		private int expLevel;
		private int foodLevel;
		private int healthLevel;
		
		private int fishCaught;
		private int timesKicked;
		private int eggsThrown;
		private int foodEaten;
		private int arrowsShot;
		private int damageTaken;
		private int bedsEntered;
		private int portalsEntered;
		
		private int wordsSaid;
		private int commandsSent;
		
		@Override
		public void fetchData(int playerId) {
			List<QueryResult> results = QueryUtils.select(
				MiscInfoPlayersTable.TableName.toString(),
				new String[] {"*"},
				new String[] { MiscInfoPlayersTable.PlayerId.toString(), playerId + ""}
			);
			if(results.isEmpty()) QueryUtils.insert(MiscInfoPlayersTable.TableName.toString(), getValues(playerId));
			else {
				fishCaught = results.get(0).getValueAsInteger(MiscInfoPlayersTable.FishCaught.toString());
				timesKicked = results.get(0).getValueAsInteger(MiscInfoPlayersTable.TimesKicked.toString());
				eggsThrown = results.get(0).getValueAsInteger(MiscInfoPlayersTable.EggsThrown.toString());
				foodEaten = results.get(0).getValueAsInteger(MiscInfoPlayersTable.FoodEaten.toString());
				arrowsShot = results.get(0).getValueAsInteger(MiscInfoPlayersTable.ArrowsShot.toString());
				damageTaken = results.get(0).getValueAsInteger(MiscInfoPlayersTable.DamageTaken.toString());
				bedsEntered = results.get(0).getValueAsInteger(MiscInfoPlayersTable.BedsEntered.toString());
				portalsEntered = results.get(0).getValueAsInteger(MiscInfoPlayersTable.PortalsEntered.toString());
				wordsSaid = results.get(0).getValueAsInteger(MiscInfoPlayersTable.WordsSaid.toString());
				commandsSent = results.get(0).getValueAsInteger(MiscInfoPlayersTable.CommandsSent.toString());
			}
		}

		@Override
		public boolean pushData(int playerId) {
			refreshPlayerData();
			boolean result = QueryUtils.update(
				MiscInfoPlayersTable.TableName.toString(),
				getValues(playerId), 
				new String[] { MiscInfoPlayersTable.PlayerId.toString(), playerId + ""}
			);
			fetchData(playerId);
			return result;
		}

		@Override
		public Map<String, Object> getValues(int playerId) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(MiscInfoPlayersTable.PlayerId.toString(), playerId);
			map.put(MiscInfoPlayersTable.ExperiencePercent.toString(), expPercent);
			map.put(MiscInfoPlayersTable.ExperienceTotal.toString(), expTotal);
			map.put(MiscInfoPlayersTable.ExperienceLevel.toString(), expLevel);
			map.put(MiscInfoPlayersTable.FoodLevel.toString(), foodLevel);
			map.put(MiscInfoPlayersTable.HealthLevel.toString(), healthLevel);
			map.put(MiscInfoPlayersTable.Gamemode.toString(), gamemode);
			
			map.put(MiscInfoPlayersTable.FishCaught.toString(), fishCaught);
			map.put(MiscInfoPlayersTable.TimesKicked.toString(), timesKicked);
			map.put(MiscInfoPlayersTable.EggsThrown.toString(), eggsThrown);
			map.put(MiscInfoPlayersTable.FoodEaten.toString(), foodEaten);
			map.put(MiscInfoPlayersTable.ArrowsShot.toString(), arrowsShot);
			map.put(MiscInfoPlayersTable.DamageTaken.toString(), damageTaken);
			map.put(MiscInfoPlayersTable.BedsEntered.toString(), bedsEntered);
			map.put(MiscInfoPlayersTable.PortalsEntered.toString(), portalsEntered);
			
			map.put(MiscInfoPlayersTable.WordsSaid.toString(), wordsSaid);
			map.put(MiscInfoPlayersTable.CommandsSent.toString(), commandsSent);
			
			return map;
		}
		
		/**
		 * Fetches the player data from the player, if he is online
		 */
		public void refreshPlayerData() {
			Player player = null;
			for(Player pl : Bukkit.getServer().getOnlinePlayers()) {
				if(pl.getPlayerListName().equals(playerName)) player = pl;
			}
			if(player == null) return;
			
			this.gamemode = player.getGameMode().getValue();
			this.expPercent = player.getExp();
			this.expTotal = player.getTotalExperience();
			this.expLevel = player.getLevel();
			this.foodLevel = player.getFoodLevel();
			this.healthLevel = player.getHealth();
		}
		
		public void fishCaught() {
			fishCaught++;
		}
		
		public void kicked() {
			timesKicked++;
		}
		
		public void eggThrown() {
			eggsThrown++;
		}
		
		public void foodEaten() {
			foodEaten++;
		}
		
		public void arrowShot() {
			arrowsShot++;
		}
		
		public void damageTaken(int damage) {
			damageTaken += damage;
		}
		
		public void bedEntered() {
			bedsEntered++;
		}
		
		public void portalEntered() {
			portalsEntered++;
		}
		
		public void chatMessageSent(int words) {
			wordsSaid += words;
		}
		
		public void commandSent() {
			commandsSent++;
		}
	}
	
	public class DetailedLogPlayersEntry implements DetailedData {
	     
	    public DetailedLogPlayersEntry(Location location, boolean isLogin) {
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
}
