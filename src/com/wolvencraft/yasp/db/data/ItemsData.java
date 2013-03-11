package com.wolvencraft.yasp.db.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Detailed;
import com.wolvencraft.yasp.db.tables.Normal.TotalItemsTable;
import com.wolvencraft.yasp.util.Util;

/**
 * Data collector that records all item statistics on the server for a specific player.
 * @author bitWolfy
 *
 */
public class ItemsData {

	/**
	 * <b>Default constructor</b><br />
	 * Creates an empty data store to save the statistics until database synchronization.
	 */
	public ItemsData(int playerId) {
		this.playerId = playerId;
		dynamicData = new ArrayList<TotalItemsEntry>();
	}
	
	private int playerId;
	private List<TotalItemsEntry> dynamicData;

	/**
	 * Returns the contents of the data store.<br />
	 * Asynchronous method; changes to the returned List will not affect the data store.
	 * @return Contents of the data store
	 */
	public List<TotalItemsEntry> get() {
		List<TotalItemsEntry> temp = new ArrayList<TotalItemsEntry>();
		for(TotalItemsEntry value : dynamicData) temp.add(value);
		return temp;
	}

	/**
	 * Returns the specific entry from the data store.<br />
	 * If the entry does not exist, it will be created.
	 * @param itemStack
	 * @return Corresponding entry
	 */
	public TotalItemsEntry get(ItemStack itemStack) {
		itemStack.setAmount(1);
		for(TotalItemsEntry entry : dynamicData) {
			if(entry.equals(itemStack)) return entry;
		}
		TotalItemsEntry entry = new TotalItemsEntry(itemStack);
		dynamicData.add(entry);
		return entry;
	}
	
	/**
	 * Synchronizes the data from the data store to the database, then removes it.<br />
	 * If an entry was not synchronized, it will not be removed.
	 */
	public void sync() {
		for(TotalItemsEntry entry : get()) {
			if(entry.pushData(playerId)) dynamicData.remove(entry);
		}
	}
	
	/**
	 * Represents the total number of items player dropped and picked up.<br />
	 * Each entry must have a unique player - material ID combination.
	 * @author bitWolfy
	 *
	 */
	public class TotalItemsEntry implements _NormalData {
		
		/**
		 * <b>Default constructor</b><br />
		 * Creates a new TotalItemsEntry based on the data provided
		 * @param itemStack
		 */
		public TotalItemsEntry(ItemStack itemStack) {
			this.type = itemStack.getTypeId();
			this.data = itemStack.getData().getData();
			this.dropped = 0;
			this.pickedUp = 0;
			this.used = 0;
			this.crafted = 0;
			this.broken = 0;
			this.smelted = 0;
			this.enchanted = 0;
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
			List<QueryResult> results = QueryUtils.select(
				TotalItemsTable.TableName.toString(),
				new String[] {"*"},
				new String[] { TotalItemsTable.PlayerId.toString(), playerId + ""},
				new String[] { TotalItemsTable.MaterialId.toString(), type + ""},
				new String[] { TotalItemsTable.MaterialData.toString(), data + ""}
			);
			
			if(results.isEmpty()) QueryUtils.insert(TotalItemsTable.TableName.toString(), getValues(playerId));
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
			return QueryUtils.update(
				TotalItemsTable.TableName.toString(),
				getValues(playerId),
				new String[] { TotalItemsTable.PlayerId.toString(), playerId + ""},
				new String[] { TotalItemsTable.MaterialId.toString(), type + ""},
				new String[] { TotalItemsTable.MaterialData.toString(), data + ""}
			);
		}
		
		@Override
		public Map<String, Object> getValues(int playerId) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(TotalItemsTable.PlayerId.toString(), playerId);
			map.put(TotalItemsTable.MaterialId.toString(), type);
			map.put(TotalItemsTable.MaterialData.toString(), data);
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
			return this.type == itemStack.getTypeId() && this.data == itemStack.getData().getData();
		}
		
		/**
		 * Adds the specified number of blocks to the total number of items dropped
		 * @param blocks Items to add
		 */
		public void addDropped() { dropped++; }
		
		/**
		 * Adds the specified number of blocks to the total number of items picked up
		 * @param blocks Items to add
		 */
		public void addPickedUp() { pickedUp++; }
		
		/**
		 * Adds the specified number of blocks to the total number of items used
		 * @param blocks Items to add
		 */
		public void addUsed() { used++; }

		/**
		 * Adds the specified number of blocks to the total number of items crafted
		 * @param blocks Items to add
		 */
		public void addCrafted() { crafted++; }
		
		/**
		 * Adds the specified number of blocks to the total number of items broken
		 * @param blocks Items to add
		 */
		public void addBroken() { broken++; }
		
		/**
		 * Adds the specified number of blocks to the total number of items smelted
		 * @param blocks Items to add
		 */
		public void addSmelted() { smelted++; }
		
		/**
		 * Adds the specified number of blocks to the total number of items enchanted
		 * @param blocks Items to add
		 */
		public void addEnchanted() { enchanted++; }
	}
	
	/**
	 * Represents an entry in the Detailed data store.
	 * It is static, i.e. it cannot be edited once it has been created.
	 * @author bitWolfy
	 *
	 */
	public class DetailedDroppedItemsEntry implements _DetailedData {

		/**
		 * <b>Default constructor</b><br />
		 * Creates a new DetailedDroppedItemsEntry based on the data provided
		 * @param location
		 * @param itemStack
		 */
		public DetailedDroppedItemsEntry(Location location, ItemStack itemStack) {
			this.type = itemStack.getTypeId();
			this.data = itemStack.getData().getData();
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
				Detailed.DroppedItems.TableName.toString(),
				getValues(playerId)
			);
		}

		@Override
		public Map<String, Object> getValues(int playerId) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Detailed.DroppedItems.PlayerId.toString(), playerId);
			map.put(Detailed.DroppedItems.MaterialId.toString(), type);
			map.put(Detailed.DroppedItems.MaterialData.toString(), data);
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
	public class DetailedPickedupItemsEntry implements _DetailedData {
		
		/**
		 * <b>Default constructor</b><br />
		 * Creates a new DetailedPickedupItemsEntry based on the data provided
		 * @param location
		 * @param itemStack
		 */
		public DetailedPickedupItemsEntry(Location location, ItemStack itemStack) {
			this.type = itemStack.getTypeId();
			this.data = itemStack.getData().getData();
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
				Detailed.PickedupItems.TableName.toString(),
				getValues(playerId)
			);
		}

		@Override
		public Map<String, Object> getValues(int playerId) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Detailed.PickedupItems.PlayerId.toString(), playerId);
			map.put(Detailed.PickedupItems.MaterialId.toString(), type);
			map.put(Detailed.PickedupItems.MaterialData.toString(), data);
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
	public class DetailedUsedItemsEntry implements _DetailedData {
		
		/**
		 * <b>Default constructor</b><br />
		 * Creates a new DetailedUsedItemsEntry based on the data provided
		 * @param location
		 * @param itemStack
		 */
		public DetailedUsedItemsEntry(Location location, ItemStack itemStack) {
			this.type = itemStack.getTypeId();
			this.data = itemStack.getData().getData();
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
				Detailed.UsedItems.TableName.toString(),
				getValues(playerId)
			);
		}

		@Override
		public Map<String, Object> getValues(int playerId) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Detailed.UsedItems.PlayerId.toString(), playerId);
			map.put(Detailed.UsedItems.MaterialId.toString(), type);
			map.put(Detailed.UsedItems.MaterialData.toString(), data);
			map.put(Detailed.UsedItems.World.toString(), location.getWorld().getName());
			map.put(Detailed.UsedItems.XCoord.toString(), location.getBlockX());
			map.put(Detailed.UsedItems.YCoord.toString(), location.getBlockY());
			map.put(Detailed.UsedItems.ZCoord.toString(), location.getBlockZ());
			map.put(Detailed.UsedItems.Timestamp.toString(), timestamp);
			return map;
		}

	}
	
}
