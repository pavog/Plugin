/*
 * Statistics
 * Copyright (C) 2013 bitWolfy <http://www.wolvencraft.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.wolvencraft.yasp.db.data.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.tables.Detailed;
import com.wolvencraft.yasp.db.tables.Normal.TotalBlocksTable;
import com.wolvencraft.yasp.util.Util;

/**
 * Data store that records all block interactions on the server.
 * @author bitWolfy
 *
 */
public class BlocksData implements _DataStore {
	
	private int playerId;
	private List<TotalBlocksEntry> normalData;
	private List<DetailedData> detailedData;
	
	/**
	 * <b>Default constructor</b><br />
	 * Creates an empty data store to save the statistics until database synchronization.
	 */
	public BlocksData(int playerId) {
		this.playerId = playerId;
		normalData = new ArrayList<TotalBlocksEntry>();
		detailedData = new ArrayList<DetailedData>();
	}
	
	@Override
	public List<NormalData> getNormalData() {
		List<NormalData> temp = new ArrayList<NormalData>();
		for(TotalBlocksEntry value : normalData) temp.add(value);
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
	 * Returns the specific entry from the data store.<br />
	 * If the entry does not exist, it will be created.
	 * @param type Material type
	 * @param blockData Damage value of the item
	 * @return Corresponding entry
	 */
	public TotalBlocksEntry getNormalData(Material type, byte blockData) {
		for(TotalBlocksEntry entry : normalData) {
			if(entry.equals(type, blockData)) return entry;
		}
		TotalBlocksEntry entry = new TotalBlocksEntry(playerId, type, blockData);
		normalData.add(entry);
		return entry;
	}
	
	/**
	 * Registers the broken block in the data stores
	 * @param location Location of the block
	 * @param type Material of the block
	 * @param data Damage value of the material
	 */
	public void blockBreak(Location location, Material type, byte data) {
		getNormalData(type, data).addBroken();
		detailedData.add(new DetailedDestroyerdBlocksEntry(location, type, data));
	}
	
	/**
	 * Registers the placed block in the data stores
	 * @param location Location of the block
	 * @param type Material of the block
	 * @param data Damage value of the material
	 */
	public void blockPlace(Location location, Material type, byte data) {
		getNormalData(type, data).addPlaced();
		detailedData.add(new DetailedPlacedBlocksEntry(location, type, data));
	}
	
	
	/**
	 * Represents an entry in the PVP data store.
	 * It is dynamic, i.e. it can be edited once it has been created.
	 * @author bitWolfy
	 *
	 */
	public class TotalBlocksEntry implements NormalData {

		/**
		 * <b>Default constructor</b><br />
		 * Creates a new TotalItemsEntry based on the data provided
		 * @param materialType
		 * @param data
		 */
		public TotalBlocksEntry(int playerId, Material materialType, byte data) {
			this.type = materialType.getId();
			this.data = data;
			this.broken = 0;
			this.placed = 0;
			
			fetchData(playerId);
		}
		
		private int type;
		private int data;
		private int broken;
		private int placed;
		
		@Override
		public void fetchData(int playerId) {
			List<QueryResult> results = Query.table(TotalBlocksTable.TableName.toString())
				.condition(TotalBlocksTable.PlayerId.toString(), playerId + "")
				.condition(TotalBlocksTable.Material.toString(), type + ":" + data)
				.select();
			
			if(results.isEmpty()) Query.table(TotalBlocksTable.TableName.toString()).value(getValues(playerId)).insert();
			else {
				broken = results.get(0).getValueAsInteger(TotalBlocksTable.Destroyed.toString());
				placed = results.get(0).getValueAsInteger(TotalBlocksTable.Placed.toString());
			}
		}

		@Override
		public boolean pushData(int playerId) {
			boolean result = Query.table(TotalBlocksTable.TableName.toString())
				.value(getValues(playerId))
				.condition(TotalBlocksTable.PlayerId.toString(), playerId + "")
				.condition(TotalBlocksTable.Material.toString(), type + ":" + data)
				.update(true);
			fetchData(playerId);
			return result;
		}
		
		@Override
		public Map<String, Object> getValues(int playerId) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(TotalBlocksTable.PlayerId.toString(), playerId);
			map.put(TotalBlocksTable.Material.toString(), type + ":" + data);
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
	public class DetailedDestroyerdBlocksEntry implements DetailedData {
		
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
			return Query.table(Detailed.DestroyedBlocks.TableName.toString())
				.value(getValues(playerId))
				.insert();
		}

		@Override
		public Map<String, Object> getValues(int playerId) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Detailed.DestroyedBlocks.PlayerId.toString(), playerId);
			map.put(Detailed.DestroyedBlocks.Material.toString(), type + ":" + data);
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
	public class DetailedPlacedBlocksEntry implements DetailedData {

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
			return Query.table(Detailed.PlacedBlocks.TableName.toString())
				.value(getValues(playerId))
				.insert();
		}

		@Override
		public Map<String, Object> getValues(int playerId) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Detailed.PlacedBlocks.PlayerId.toString(), playerId);
			map.put(Detailed.PlacedBlocks.Material.toString(), type + ":" + data);
			map.put(Detailed.PlacedBlocks.World.toString(), location.getWorld().getName());
			map.put(Detailed.PlacedBlocks.XCoord.toString(), location.getBlockX());
			map.put(Detailed.PlacedBlocks.YCoord.toString(), location.getBlockY());
			map.put(Detailed.PlacedBlocks.ZCoord.toString(), location.getBlockZ());
			map.put(Detailed.PlacedBlocks.Timestamp.toString(), timestamp);
			return map;
		}
		
	}
	
}
