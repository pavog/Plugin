package com.wolvencraft.yasp.db.data.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.tables.Detailed;
import com.wolvencraft.yasp.db.tables.Normal.TotalPVEKillsTable;
import com.wolvencraft.yasp.util.Util;

/**
 * Data collector that records all PVE statistics on the server for a specific player.
 * @author bitWolfy
 *
 */
public class PVEData implements _DataStore{

	private int playerId;
	private List<TotalPVEEntry> normalData;
	private List<DetailedData> detailedData;
	
	/**
	 * <b>Default constructor</b><br />
	 * Creates an empty data store to save the statistics until database synchronization.
	 */
	public PVEData(int playerId) {
		this.playerId = playerId;
		normalData = new ArrayList<TotalPVEEntry>();
		detailedData = new ArrayList<DetailedData>();
	}
	
	@Override
	public List<NormalData> getNormalData() {
		List<NormalData> temp = new ArrayList<NormalData>();
		for(NormalData value : normalData) temp.add(value);
		return temp;
	}
	
	@Override
	public List<DetailedData> getDetailedData() {
		List<DetailedData> temp = new ArrayList<DetailedData>();
		for(DetailedData value : detailedData) temp.add(value);
		return temp;
	}
	
	@Override
	public void sync() {
		for(NormalData entry : getNormalData()) {
			if(entry.pushData(playerId)) normalData.remove(entry);
		}
		
		for(DetailedData entry : getDetailedData()) {
			if(entry.pushData(playerId)) detailedData.remove(entry);
		}
	}
	
	@Override
	public void dump() {
		for(NormalData entry : getNormalData()) {
			normalData.remove(entry);
		}
		
		for(DetailedData entry : getDetailedData()) {
			detailedData.remove(entry);
		}
	}
	
	/**
	 * Returns a specific entry from the data store.<br />
	 * If an entry does not exist, it will be created.
	 * @param type Entity type of the creature
	 * @param weapon Weapon used in the event
	 * @return Corresponding entry
	 */
	public TotalPVEEntry getNormalData(EntityType type, ItemStack weapon) {
		for(TotalPVEEntry entry : normalData) {
			if(entry.equals(type, weapon)) return entry;
		}
		TotalPVEEntry entry = new TotalPVEEntry(playerId, type, weapon);
		normalData.add(entry);
		return entry;
	}
	
	/**
	 * Registers the creature death in the data store
	 * @param location Location of the event
	 * @param victim Creature killed
	 * @param weapon Weapon used by killer
	 */
	public void playerKilledCreature(Creature victim, ItemStack weapon) {
		getNormalData(victim.getType(), weapon).addCreatureDeaths();
		detailedData.add(new DetailedPVEEntry(victim.getLocation(), victim.getType(), weapon, false));
	}
	
	/**
	 * Registers the player death in the data store
	 * @param location Location of the event
	 * @param killer Creature that killed the player
	 * @param weapon Weapon used by killer
	 */
	public void creatureKilledPlayer(Creature killer, ItemStack weapon) {
		getNormalData(killer.getType(), weapon).addPlayerDeaths();
		detailedData.add(new DetailedPVEEntry(killer.getLocation(), killer.getType(), weapon, true));
	}
	
	
	/**
	 * Represents an entry in the PVE data store.
	 * It is dynamic, i.e. it can be edited once it has been created.
	 * @author bitWolfy
	 *
	 */
	public class TotalPVEEntry implements NormalData {
		
		/**
		 * <b>Default constructor</b><br />
		 * Creates a new TotalPVE object based on the player and creature in question
		 * @param player Player in question
		 * @param creature Creature in question
		 */
		public TotalPVEEntry(int playerId, EntityType creatureType, ItemStack weapon) {
			this.creatureType = creatureType;
			this.weapon = weapon;
			this.playerDeaths = 0;
			this.creatureDeaths = 0;
			
			fetchData(playerId);
		}
		
		private EntityType creatureType;
		private ItemStack weapon;
		private int playerDeaths;
		private int creatureDeaths;
		
		@Override
		public void fetchData(int playerId) {
			List<QueryResult> results = Query.table(TotalPVEKillsTable.TableName.toString())
				.condition(TotalPVEKillsTable.PlayerId.toString(), playerId + "")
				.condition(TotalPVEKillsTable.CreatureId.toString(), creatureType.getTypeId() + "")
				.condition(TotalPVEKillsTable.Material.toString(), weapon.getTypeId() + ":" + weapon.getData().getData())
				.select();
			if(results.isEmpty()) Query.table(TotalPVEKillsTable.TableName.toString()).value(getValues(playerId));
			else {
				playerDeaths = results.get(0).getValueAsInteger(TotalPVEKillsTable.PlayerKilled.toString());
				creatureDeaths = results.get(0).getValueAsInteger(TotalPVEKillsTable.CreatureKilled.toString());
			}
		}

		@Override
		public boolean pushData(int playerId) {
			boolean result = Query.table(TotalPVEKillsTable.TableName.toString())
				.value(getValues(playerId))
				.condition(TotalPVEKillsTable.PlayerId.toString(), playerId + "")
				.condition(TotalPVEKillsTable.CreatureId.toString(), creatureType.getTypeId() + "")
				.condition(TotalPVEKillsTable.Material.toString(), weapon.getTypeId() + ":" + weapon.getData().getData())
				.update();
			fetchData(playerId);
			return result;
		}

		@Override
		public Map<String, Object> getValues(int playerId) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(TotalPVEKillsTable.PlayerId.toString(), playerId);
			map.put(TotalPVEKillsTable.CreatureId.toString(), creatureType.getTypeId());
			map.put(TotalPVEKillsTable.Material.toString(), weapon.getTypeId() + ":" + weapon.getData().getData());
			map.put(TotalPVEKillsTable.PlayerKilled.toString(), playerDeaths);
			map.put(TotalPVEKillsTable.CreatureKilled.toString(), creatureDeaths);
			return map;
		}

		/**
		 * Matches data provided in the arguments with the one in the entry.
		 * @param creatureType Type of the creature
		 * @param weapon Weapon used in the event
		 * @return <b>true</b> if the data matches, <b>false</b> otherwise.
		 */
		public boolean equals(EntityType creatureType, ItemStack weapon) {
			return this.creatureType.equals(creatureType) && this.weapon.equals(weapon);
		}
		
		public void addPlayerDeaths() { playerDeaths++; }
		public void addCreatureDeaths() { creatureDeaths++; }
	}
	
	
	/**
	 * Represents an entry in the Detailed data store.
	 * It is static, i.e. it cannot be edited once it has been created.
	 * @author bitWolfy
	 *
	 */
	public class DetailedPVEEntry implements DetailedData {

		/**
		 * <b>Default constructor</b><br />
		 * Creates a new DetailedPVPEntry based on the data provided
		 * @param location
		 * @param creatureType
		 * @param weapon
		 * @param playerKilled
		 */
		public DetailedPVEEntry (Location location, EntityType creatureType, ItemStack weapon, boolean playerKilled) {
			this.creatureType = creatureType;
			this.weapon = weapon;
			this.location = location;
			this.playerKilled = playerKilled;
			this.timestamp = Util.getTimestamp();
		}
		
		private EntityType creatureType;
		private ItemStack weapon;
		private Location location;
		private boolean playerKilled;
		private long timestamp;
		
		@Override
		public boolean pushData(int playerId) {
			return Query.table(Detailed.PVEKills.TableName.toString())
				.value(getValues(playerId))
				.insert();
		}

		@Override
		public Map<String, Object> getValues(int playerId) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Detailed.PVEKills.PlayerId.toString(), playerId);
			map.put(Detailed.PVEKills.CreatureId.toString(), creatureType.getTypeId());
			if(playerKilled) {
				map.put(Detailed.PVEKills.PlayerKilled.toString(), 1);
				map.put(Detailed.PVEKills.Material.toString(), "-1:0");
			} else {
				map.put(Detailed.PVEKills.PlayerKilled.toString(), 0);
				map.put(Detailed.PVEKills.Material.toString(), weapon.getTypeId() + ":" + weapon.getData().getData());
			}
			map.put(Detailed.PVEKills.World.toString(), location.getWorld().getName());
			map.put(Detailed.PVEKills.XCoord.toString(), location.getBlockX());
			map.put(Detailed.PVEKills.YCoord.toString(), location.getBlockY());
			map.put(Detailed.PVEKills.ZCoord.toString(), location.getBlockZ());
			map.put(Detailed.PVEKills.Timestamp.toString(), timestamp);
			return map;
		}

	}
	
}
