package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal.TotalItems;

/**
 * Represents the total number of items player dropped and picked up.<br />
 * Each entry must have a unique player - material ID combination.
 * @author bitWolfy
 *
 */
public class TotalItemsEntry implements _NormalData {
	
	/**
	 * Default constructor. Takes in the Player object and the MaterialData of the block
	 * @param player <b>Player</b> tracked player
	 * @param material <b>MaterialData</b> block data
	 */
	public TotalItemsEntry(int playerId, ItemStack itemStack) {
		this.playerId = playerId;
		this.itemStack = itemStack;
		this.itemStack.setAmount(1);
		this.dropped = 0;
		this.pickedUp = 0;
		this.used = 0;
		this.crafted = 0;
		this.smelted = 0;
	}
	
	private int playerId;
	private ItemStack itemStack;
	private int dropped;
	private int pickedUp;
	private int used;
	private int crafted;
	private int smelted;
	
	@Override
	public void fetchData() {
		List<QueryResult> results = QueryUtils.select(
			TotalItems.TableName.toString(),
			new String[] {"*"},
			new String[] { TotalItems.PlayerId.toString(), playerId + ""},
			new String[] { TotalItems.MaterialId.toString(), itemStack.getTypeId() + ""},
			new String[] { TotalItems.MaterialData.toString(), itemStack.getData().getData() + ""}
		);
		
		if(results.isEmpty()) QueryUtils.insert(TotalItems.TableName.toString(), getValues());
		else {
			dropped = results.get(0).getValueAsInteger(TotalItems.Dropped.toString());
			pickedUp = results.get(0).getValueAsInteger(TotalItems.PickedUp.toString());
			used = results.get(0).getValueAsInteger(TotalItems.Used.toString());
			crafted = results.get(0).getValueAsInteger(TotalItems.Crafted.toString());
			smelted = results.get(0).getValueAsInteger(TotalItems.Smelted.toString());
		}
	}

	@Override
	public boolean pushData() {
		return QueryUtils.update(
			TotalItems.TableName.toString(),
			getValues(),
			new String[] { TotalItems.PlayerId.toString(), playerId + ""},
			new String[] { TotalItems.MaterialId.toString(), itemStack.getTypeId() + ""},
			new String[] { TotalItems.MaterialData.toString(), itemStack.getData().getData() + ""}
		);
	}
	
	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TotalItems.PlayerId.toString(), playerId);
		map.put(TotalItems.MaterialId.toString(), itemStack.getTypeId());
		map.put(TotalItems.MaterialData.toString(), itemStack.getData().getData());
		map.put(TotalItems.Dropped.toString(), dropped);
		map.put(TotalItems.PickedUp.toString(), pickedUp);
		map.put(TotalItems.Used.toString(), used);
		map.put(TotalItems.Crafted.toString(), crafted);
		map.put(TotalItems.Smelted.toString(), smelted);
		return map;
	}
	
	/**
	 * Returns the item stack
	 * @return <b>ItemStack</b> material
	 */
	public ItemStack getItemStack() { return itemStack; }
	
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
	 * Adds the specified number of blocks to the total number of items smelted
	 * @param blocks Items to add
	 */
	public void addSmelted() { smelted++; }
}
