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
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.tables.Detailed;
import com.wolvencraft.yasp.db.tables.Normal.TotalItemsTable;
import com.wolvencraft.yasp.util.Util;

/**
 * Data store that records all item interactions on the server.
 * @author bitWolfy
 *
 */
public class ItemsData implements _DataStore {
	
	private int playerId;
	private List<TotalItemsEntry> normalData;
	private List<DetailedData> detailedData;

	/**
	 * <b>Default constructor</b><br />
	 * Creates an empty data store to save the statistics until database synchronization.
	 */
	public ItemsData(int playerId) {
		this.playerId = playerId;
		normalData = new ArrayList<TotalItemsEntry>();
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
	 * Returns the specific entry from the data store.<br />
	 * If the entry does not exist, it will be created.
	 * @param itemStack
	 * @return Corresponding entry
	 */
	public TotalItemsEntry getNormalData(ItemStack itemStack) {
		itemStack.setAmount(1);
		for(TotalItemsEntry entry : normalData) {
			if(entry.equals(itemStack)) return entry;
		}
		TotalItemsEntry entry = new TotalItemsEntry(playerId, itemStack);
		normalData.add(entry);
		return entry;
	}
	
	/**
	 * Registers the dropped item in the data stores
	 * @param location Location of the event
	 * @param itemStack Stack of items in question
	 */
	public void itemDrop(Location location, ItemStack itemStack) {
		getNormalData(itemStack).addDropped();
		detailedData.add(new DetailedDroppedItemsEntry(location, itemStack));
	}
	
	/**
	 * Registers the picked up item in the data stores
	 * @param location Location of the event
	 * @param itemStack Stack of items in question
	 */
	public void itemPickUp(Location location, ItemStack itemStack) {
		getNormalData(itemStack).addPickedUp();
		detailedData.add(new DetailedPickedupItemsEntry(location, itemStack));
	}
	
	/**
	 * Registers the used item in the data stores
	 * @param location Location of the event
	 * @param itemStack Stack of items in question
	 */
	public void itemUse(Location location, ItemStack itemStack) {
		getNormalData(itemStack).addUsed();
		detailedData.add(new DetailedUsedItemsEntry(location, itemStack));
	}
	
	/**
	 * Registers the crafted item in the data stores
	 * @param location Location of the event
	 * @param itemStack Stack of items in question
	 */
	public void itemCraft(Location location, ItemStack itemStack) {
		getNormalData(itemStack).addCrafted();
	}
	
	/**
	 * Registers the smelted item in the data stores
	 * @param location Location of the event
	 * @param itemStack Stack of items in question
	 */
	public void itemSmelt(Location location, ItemStack itemStack) {
		getNormalData(itemStack).addSmelted();
	}
	
	/**
	 * Registers the broken item in the data stores
	 * @param location Location of the event
	 * @param itemStack Stack of items in question
	 */
	public void itemBreak(Location location, ItemStack itemStack) {
		getNormalData(itemStack).addBroken();
	}
	
	/**
	 * Registers the enchanted item in the data stores
	 * @param location Location of the event
	 * @param itemStack Stack of items in question
	 */
	public void itemEnchant(Location location, ItemStack itemStack) {
		getNormalData(itemStack).addEnchanted();
	}
	
	
	/**
	 * Represents the total number of items player dropped and picked up.<br />
	 * Each entry must have a unique player - material ID combination.
	 * @author bitWolfy
	 *
	 */
	public class TotalItemsEntry implements NormalData {
		
		/**
		 * <b>Default constructor</b><br />
		 * Creates a new TotalItemsEntry based on the data provided
		 * @param itemStack
		 */
		public TotalItemsEntry(int playerId, ItemStack itemStack) {
			this.type = itemStack.getTypeId();
			if(Settings.ItemsWithMetadata.checkAgainst(this.type)) this.data = itemStack.getData().getData();
			else this.data = 0;
			
			this.dropped = 0;
			this.pickedUp = 0;
			this.used = 0;
			this.crafted = 0;
			this.broken = 0;
			this.smelted = 0;
			this.enchanted = 0;
			
			fetchData(playerId);
		}
		
		private int type;
		private byte data;
		private int dropped;
		private int pickedUp;
		private int used;
		private int crafted;
		private int broken;
		private int smelted;
		private int enchanted;
		
		@Override
		public void fetchData(int playerId) {
			List<QueryResult> results = Query.table(TotalItemsTable.TableName.toString())
				.condition(TotalItemsTable.PlayerId.toString(), playerId + "")
				.condition(TotalItemsTable.Material.toString(), type + ":" + data)
				.select();
			
			if(results.isEmpty()) Query.table(TotalItemsTable.TableName.toString()).value(getValues(playerId)).insert();
			else {
				dropped = results.get(0).getValueAsInteger(TotalItemsTable.Dropped.toString());
				pickedUp = results.get(0).getValueAsInteger(TotalItemsTable.PickedUp.toString());
				used = results.get(0).getValueAsInteger(TotalItemsTable.Used.toString());
				crafted = results.get(0).getValueAsInteger(TotalItemsTable.Crafted.toString());
				broken = results.get(0).getValueAsInteger(TotalItemsTable.Broken.toString());
				smelted = results.get(0).getValueAsInteger(TotalItemsTable.Smelted.toString());
				enchanted = results.get(0).getValueAsInteger(TotalItemsTable.Enchanted.toString());
			}
		}

		@Override
		public boolean pushData(int playerId) {
			boolean result = Query.table(TotalItemsTable.TableName.toString())
				.value(getValues(playerId))
				.condition(TotalItemsTable.PlayerId.toString(), playerId + "")
				.condition(TotalItemsTable.Material.toString(), type + ":" + data)
				.update(true);
			fetchData(playerId);
			return result;
		}
		
		@Override
		public Map<String, Object> getValues(int playerId) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(TotalItemsTable.PlayerId.toString(), playerId);
			map.put(TotalItemsTable.Material.toString(), type + ":" + data);
			map.put(TotalItemsTable.Dropped.toString(), dropped);
			map.put(TotalItemsTable.PickedUp.toString(), pickedUp);
			map.put(TotalItemsTable.Used.toString(), used);
			map.put(TotalItemsTable.Crafted.toString(), crafted);
			map.put(TotalItemsTable.Broken.toString(), broken);
			map.put(TotalItemsTable.Smelted.toString(), smelted);
			map.put(TotalItemsTable.Enchanted.toString(), enchanted);
			return map;
		}
		
		/**
		 * Checks if the ItemStack corresponds to this entry 
		 * @param itemStack ItemStack to check
		 * @return b>true</b> if the data matches, <b>false</b> otherwise.
		 */
		public boolean equals(ItemStack itemStack) {
			int type = itemStack.getTypeId();
			int data = itemStack.getData().getData();
			if(!Settings.ItemsWithMetadata.checkAgainst(type)) data = 0;
			
			return this.type == type && this.data == data;
		}
		
		public void addDropped() { dropped++; }
		public void addPickedUp() { pickedUp++; }
		public void addUsed() { used++; }
		public void addCrafted() { crafted++; }
		public void addBroken() { broken++; }
		public void addSmelted() { smelted++; }
		public void addEnchanted() { enchanted++; }
	}
	
	
	/**
	 * Represents an entry in the Detailed data store.
	 * It is static, i.e. it cannot be edited once it has been created.
	 * @author bitWolfy
	 *
	 */
	public class DetailedDroppedItemsEntry implements DetailedData {

		/**
		 * <b>Default constructor</b><br />
		 * Creates a new DetailedDroppedItemsEntry based on the data provided
		 * @param location
		 * @param itemStack
		 */
		public DetailedDroppedItemsEntry(Location location, ItemStack itemStack) {
			this.type = itemStack.getTypeId();
			if(Settings.ItemsWithMetadata.checkAgainst(type)) this.data = itemStack.getData().getData();
			else this.data = 0;
			
			this.location = location;
			this.timestamp = Util.getTimestamp();
		}
		
		private int type;
		private byte data;
		private Location location;
		private long timestamp;
		
		@Override
		public boolean pushData(int playerId) {
			return Query.table(Detailed.DroppedItems.TableName.toString())
				.value(getValues(playerId))
				.insert();
		}

		@Override
		public Map<String, Object> getValues(int playerId) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Detailed.DroppedItems.PlayerId.toString(), playerId);
			map.put(Detailed.DroppedItems.Material.toString(), type + ":" + data);
			map.put(Detailed.DroppedItems.World.toString(), location.getWorld().getName());
			map.put(Detailed.DroppedItems.XCoord.toString(), location.getBlockX());
			map.put(Detailed.DroppedItems.YCoord.toString(), location.getBlockY());
			map.put(Detailed.DroppedItems.ZCoord.toString(), location.getBlockZ());
			map.put(Detailed.DroppedItems.Timestamp.toString(), timestamp);
			return map;
		}

	}
	
	
	/**
	 * Represents an entry in the Detailed data store.
	 * It is static, i.e. it cannot be edited once it has been created.
	 * @author bitWolfy
	 *
	 */
	public class DetailedPickedupItemsEntry implements DetailedData {
		
		/**
		 * <b>Default constructor</b><br />
		 * Creates a new DetailedPickedupItemsEntry based on the data provided
		 * @param location
		 * @param itemStack
		 */
		public DetailedPickedupItemsEntry(Location location, ItemStack itemStack) {
			this.type = itemStack.getTypeId();
			if(Settings.ItemsWithMetadata.checkAgainst(type)) this.data = itemStack.getData().getData();
			else this.data = 0;
			
			this.location = location;
			this.timestamp = Util.getTimestamp();
		}
		
		private int type;
		private int data;
		private Location location;
		private long timestamp;
		
		@Override
		public boolean pushData(int playerId) {
			return Query.table(Detailed.PickedupItems.TableName.toString())
				.value(getValues(playerId))
				.insert();
		}

		@Override
		public Map<String, Object> getValues(int playerId) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Detailed.PickedupItems.PlayerId.toString(), playerId);
			map.put(Detailed.PickedupItems.Material.toString(), type + ":" + data);
			map.put(Detailed.PickedupItems.World.toString(), location.getWorld().getName());
			map.put(Detailed.PickedupItems.XCoord.toString(), location.getBlockX());
			map.put(Detailed.PickedupItems.YCoord.toString(), location.getBlockY());
			map.put(Detailed.PickedupItems.ZCoord.toString(), location.getBlockZ());
			map.put(Detailed.PickedupItems.Timestamp.toString(), timestamp);
			return map;
		}

	}
	
	
	/**
	 * Represents an entry in the Detailed data store.
	 * It is static, i.e. it cannot be edited once it has been created.
	 * @author bitWolfy
	 *
	 */
	public class DetailedUsedItemsEntry implements DetailedData {
		
		/**
		 * <b>Default constructor</b><br />
		 * Creates a new DetailedUsedItemsEntry based on the data provided
		 * @param location
		 * @param itemStack
		 */
		public DetailedUsedItemsEntry(Location location, ItemStack itemStack) {
			this.type = itemStack.getTypeId();
			if(Settings.ItemsWithMetadata.checkAgainst(type)) this.data = itemStack.getData().getData();
			else this.data = 0;
			
			this.location = location;
			this.timestamp = Util.getTimestamp();
		}

		private int type;
		private byte data;
		private Location location;
		private long timestamp;
		
		@Override
		public boolean pushData(int playerId) {
			return Query.table(Detailed.UsedItems.TableName.toString())
				.value(getValues(playerId))
				.insert();
		}

		@Override
		public Map<String, Object> getValues(int playerId) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Detailed.UsedItems.PlayerId.toString(), playerId);
			map.put(Detailed.UsedItems.Material.toString(), type + ":" + data);
			map.put(Detailed.UsedItems.World.toString(), location.getWorld().getName());
			map.put(Detailed.UsedItems.XCoord.toString(), location.getBlockX());
			map.put(Detailed.UsedItems.YCoord.toString(), location.getBlockY());
			map.put(Detailed.UsedItems.ZCoord.toString(), location.getBlockZ());
			map.put(Detailed.UsedItems.Timestamp.toString(), timestamp);
			return map;
		}

	}
	
}
