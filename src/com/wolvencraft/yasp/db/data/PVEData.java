package com.wolvencraft.yasp.db.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Detailed;
import com.wolvencraft.yasp.db.tables.Normal.TotalPVEKillsTable;
import com.wolvencraft.yasp.util.Util;

/**
 * Data collector that records all PVE statistics on the server for a specific player.
 * @author bitWolfy
 *
 */
public class PVEData {
	
	/**
	 * <b>Default constructor</b><br />
	 * Creates an empty data store to save the statistics until database synchronization.
	 */
	public PVEData(int playerId) {
		this.playerId = playerId;
		dynamicData = new ArrayList<TotalPVEEntry>();
	}

	private int playerId;
	private List<TotalPVEEntry> dynamicData;
	
	/**
	 * Returns the contents of the data store.<br />
	 * Asynchronous method; changes to the returned List will not affect the data store.
	 * @return Contents of the data store
	 */
	public List<TotalPVEEntry> get() {
		List<TotalPVEEntry> temp = new ArrayList<TotalPVEEntry>();
		for(TotalPVEEntry value : dynamicData) temp.add(value);
		return temp;
	}
	
	public TotalPVEEntry get(EntityType creatureType, ItemStack weapon) {
		for(TotalPVEEntry entry : dynamicData) {
			if(entry.equals(creatureType, weapon)) return entry;
		}
		TotalPVEEntry entry = new TotalPVEEntry(creatureType, weapon);
		dynamicData.add(entry);
		return entry;
	}
	
	/**
	 * Synchronizes the data from the data store to the database, then removes it.<br />
	 * If an entry was not synchronized, it will not be removed.
	 */
	public void sync() {
		for(TotalPVEEntry entry : get()) {
			if(entry.pushData(playerId)) dynamicData.remove(entry);
		}
	}
	
	/**
	 * Represents an entry in the PVE data store.
	 * It is dynamic, i.e. it can be edited once it has been created.
	 * @author bitWolfy
	 *
	 */
	public class TotalPVEEntry implements _NormalData {
		
		/**
		 * <b>Default constructor</b><br />
		 * Creates a new TotalPVE object based on the player and creature in question
		 * @param player Player in question
		 * @param creature Creature in question
		 */
		public TotalPVEEntry(EntityType creatureType, ItemStack weapon) {
			this.creatureType = creatureType;
			this.weapon = weapon;
			this.playerDeaths = 0;
			this.creatureDeaths = 0;
		}
		
		private EntityType creatureType;
		private ItemStack weapon;
		private int playerDeaths;
		private int creatureDeaths;
		
		@Override
		public void fetchData(int playerId) {
			List<QueryResult> results = QueryUtils.select(
				TotalPVEKillsTable.TableName.toString(),
				new String[] {"*"},
				new String[] { TotalPVEKillsTable.PlayerId.toString(), playerId + ""},
				new String[] { TotalPVEKillsTable.CreatureId.toString(), creatureType.getTypeId() + ""},
				new String[] { TotalPVEKillsTable.Material.toString(), weapon.getTypeId() + ":" + weapon.getData().getData()}
			);
			if(results.isEmpty()) QueryUtils.insert(TotalPVEKillsTable.TableName.toString(), getValues(playerId));
			else {
				playerDeaths = results.get(0).getValueAsInteger(TotalPVEKillsTable.PlayerKilled.toString());
				creatureDeaths = results.get(0).getValueAsInteger(TotalPVEKillsTable.CreatureKilled.toString());
			}
		}

		@Override
		public boolean pushData(int playerId) {
			return QueryUtils.update(
				TotalPVEKillsTable.TableName.toString(),
				getValues(playerId),
				new String[] { TotalPVEKillsTable.PlayerId.toString(), playerId + ""},
				new String[] { TotalPVEKillsTable.CreatureId.toString(), creatureType.getTypeId() + ""},
				new String[] { TotalPVEKillsTable.Material.toString(), weapon.getTypeId() + ":" + weapon.getData().getData()}
			);
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
		
		/**
		 * Increments the number of time a player died
		 */
		public void addPlayerDeaths() { playerDeaths++; }
		
		/**
		 * Increments the number of times a creature died
		 */
		public void addCreatureDeaths() { creatureDeaths++; }
	}
	
	/**
	 * Represents an entry in the Detailed data store.
	 * It is static, i.e. it cannot be edited once it has been created.
	 * @author bitWolfy
	 *
	 */
	public class DetailedPVEEntry implements _DetailedData {

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
			return QueryUtils.insert(
				Detailed.PVEKills.TableName.toString(),
				getValues(playerId)
			);
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
