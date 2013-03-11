package com.wolvencraft.yasp.db.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;

import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Detailed;
import com.wolvencraft.yasp.db.tables.Normal.TotalBlocksTable;
import com.wolvencraft.yasp.util.Util;

/**
 * Data collector that records all block statistics on the server for a specific player.
 * @author bitWolfy
 *
 */
public class BlocksData {
	
	/**
	 * <b>Default constructor</b><br />
	 * Creates an empty data store to save the statistics until database synchronization.
	 */
	public BlocksData(int playerId) {
		this.playerId = playerId;
		dynamicData = new ArrayList<TotalBlocksEntry>();
	}
	
	private int playerId;
	private List<TotalBlocksEntry> dynamicData;
	
	/**
	 * Returns the contents of the data store.<br />
	 * Asynchronous method; changes to the returned List will not affect the data store.
	 * @return Contents of the data store
	 */
	public List<TotalBlocksEntry> get() {
		List<TotalBlocksEntry> temp = new ArrayList<TotalBlocksEntry>();
		for(TotalBlocksEntry value : dynamicData) temp.add(value);
		return temp;
	}

	/**
	 * Returns the specific entry from the data store.<br />
	 * If the entry does not exist, it will be created.
	 * @param type Material type
	 * @param blockData Damage value of the item
	 * @return Corresponding entry
	 */
	public TotalBlocksEntry get(Material type, byte blockData) {
		for(TotalBlocksEntry entry : dynamicData) {
			if(entry.equals(type, blockData)) return entry;
		}
		TotalBlocksEntry entry = new TotalBlocksEntry(type, blockData);
		dynamicData.add(entry);
		return entry;
	}
	
	/**
	 * Synchronizes the data from the data store to the database, then removes it.<br />
	 * If an entry was not synchronized, it will not be removed.
	 */
	public void sync() {
		for(TotalBlocksEntry entry : get()) {
			if(entry.pushData(playerId)) dynamicData.remove(entry);
		}
	}
	
	/**
	 * Represents an entry in the PVP data store.
	 * It is dynamic, i.e. it can be edited once it has been created.
	 * @author bitWolfy
	 *
	 */
	public class TotalBlocksEntry implements _NormalData {

		/**
		 * <b>Default constructor</b><br />
		 * Creates a new TotalItemsEntry based on the data provided
		 * @param materialType
		 * @param data
		 */
		public TotalBlocksEntry(Material materialType, byte data) {
			this.type = materialType.getId();
			this.data = data;
			this.broken = 0;
			this.placed = 0;
		}
		
		private int type;
		private int data;
		private int broken;
		private int placed;
		
		@Override
		public void fetchData(int playerId) {
			List<QueryResult> results = QueryUtils.select(
				TotalBlocksTable.TableName.toString(),
				new String[] {"*"},
				new String[] { TotalBlocksTable.PlayerId.toString(), playerId + ""},
				new String[] { TotalBlocksTable.MaterialId.toString(), type + ""},
				new String[] { TotalBlocksTable.MaterialData.toString(), data + ""}
			);
			
			if(results.isEmpty()) QueryUtils.insert(TotalBlocksTable.TableName.toString(), getValues(playerId));
			else {
				broken = results.get(0).getValueAsInteger(TotalBlocksTable.Destroyed.toString());
				placed = results.get(0).getValueAsInteger(TotalBlocksTable.Placed.toString());
			}
		}

		@Override
		public boolean pushData(int playerId) {
			return QueryUtils.update(
				TotalBlocksTable.TableName.toString(),
				getValues(playerId),
				new String[] { TotalBlocksTable.PlayerId.toString(), playerId + ""},
				new String[] { TotalBlocksTable.MaterialId.toString(), type + ""},
				new String[] { TotalBlocksTable.MaterialData.toString(), data + ""}
			);
		}
		
		@Override
		public Map<String, Object> getValues(int playerId) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(TotalBlocksTable.PlayerId.toString(), playerId);
			map.put(TotalBlocksTable.MaterialId.toString(), type);
			map.put(TotalBlocksTable.MaterialData.toString(), data);
			map.put(TotalBlocksTable.Destroyed.toString(), broken);
			map.put(TotalBlocksTable.Placed.toString(), placed);
			return map;
		}
		
		/**
		 * Checks if the object corresponds to provided parameters
		 * @param materialType
		 * @param data
		 * @return <b>true</b> if the conditions are met, <b>false</b> otherwise
		 */
		public boolean equals(Material materialType, byte data) {
			return this.type == materialType.getId() && this.data == data;
		}
		
		/**
		 * Adds the specified number of blocks to the total number of blocks destroyed
		 * @param blocks Blocks to add
		 */
		public void addBroken() { broken ++; }
		
		/**
		 * Adds the specified number of blocks to the total number of blocks placed
		 * @param blocks Blocks to add
		 */
		public void addPlaced() { placed ++; }
	}
	
	/**
	 * Represents an entry in the Detailed data store.
	 * It is static, i.e. it cannot be edited once it has been created.
	 * @author bitWolfy
	 *
	 */
	public class DetailedDestroyerdBlocksEntry implements _DetailedData {
		
		/**
		 * <b>Default constructor</b><br />
		 * Creates a new DetailedDestroyedBlocksEntry based on the data provided
		 * @param location
		 * @param materialType
		 * @param data
		 */
		public DetailedDestroyerdBlocksEntry(Location location, Material materialType, byte data) {
			this.type = materialType.getId();
			this.data = data;
			this.location = location;
			this.timestamp = Util.getTimestamp();
		}
		
		private int type;
		private byte data;
		private Location location;
		private long timestamp;

		@Override
		public boolean pushData(int playerId) {
			return QueryUtils.insert(
				Detailed.DestroyedBlocks.TableName.toString(),
				getValues(playerId)
			);
		}

		@Override
		public Map<String, Object> getValues(int playerId) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Detailed.DestroyedBlocks.PlayerId.toString(), playerId);
			map.put(Detailed.DestroyedBlocks.MaterialId.toString(), type);
			map.put(Detailed.DestroyedBlocks.MaterialData.toString(), data);
			map.put(Detailed.DestroyedBlocks.World.toString(), location.getWorld().getName());
			map.put(Detailed.DestroyedBlocks.XCoord.toString(), location.getBlockX());
			map.put(Detailed.DestroyedBlocks.YCoord.toString(), location.getBlockY());
			map.put(Detailed.DestroyedBlocks.ZCoord.toString(), location.getBlockZ());
			map.put(Detailed.DestroyedBlocks.Timestamp.toString(), timestamp);
			return map;
		}

	}
	
	/**
	 * Represents an entry in the Detailed data store.
	 * It is static, i.e. it cannot be edited once it has been created.
	 * @author bitWolfy
	 *
	 */
	public class DetailedPlacedBlocksEntry implements _DetailedData {

		/**
		 * <b>Default constructor</b><br />
		 * Creates a new DetailedPlacedBlocksEntry based on the data provided
		 * @param location
		 * @param materialType
		 * @param data
		 */
		public DetailedPlacedBlocksEntry(Location location, Material materialType, byte data) {
			this.type = materialType.getId();
			this.data = data;
			this.location = location;
			this.timestamp = Util.getTimestamp();
		}
		
		private int type;
		private int data;
		private Location location;
		private long timestamp;

		@Override
		public boolean pushData(int playerId) {
			return QueryUtils.insert(
				Detailed.PlacedBlocks.TableName.toString(),
				getValues(playerId)
			);
		}

		@Override
		public Map<String, Object> getValues(int playerId) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Detailed.PlacedBlocks.PlayerId.toString(), playerId);
			map.put(Detailed.PlacedBlocks.MaterialId.toString(), type);
			map.put(Detailed.PlacedBlocks.MaterialData.toString(), data);
			map.put(Detailed.PlacedBlocks.World.toString(), location.getWorld().getName());
			map.put(Detailed.PlacedBlocks.XCoord.toString(), location.getBlockX());
			map.put(Detailed.PlacedBlocks.YCoord.toString(), location.getBlockY());
			map.put(Detailed.PlacedBlocks.ZCoord.toString(), location.getBlockZ());
			map.put(Detailed.PlacedBlocks.Timestamp.toString(), timestamp);
			return map;
		}
		
	}
	
}
