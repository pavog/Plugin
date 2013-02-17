package com.wolvencraft.yasp.db.data.Dynamic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.DBEntry;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Dynamic.TotalItemsTable;

/**
 * Represents the total number of items player dropped and picked up.<br />
 * Each entry must have a unique player - material ID combination.
 * @author bitWolfy
 *
 */
public class TotalItemsEntry implements DynamicData {
	
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
	}
	
	private int playerId;
	private ItemStack itemStack;
	private int dropped;
	private int pickedUp;
	
	@Override
	public void fetchData() {
		List<DBEntry> results = QueryUtils.select(
			TotalItemsTable.TableName.toString(),
			"*",
			TotalItemsTable.PlayerId + " = " + playerId
		);
		
		if(results.isEmpty()) QueryUtils.insert(TotalItemsTable.TableName.toString(), getValues());
		else {
			dropped = results.get(0).getValueAsInteger(TotalItemsTable.Dropped.toString());
			pickedUp = results.get(0).getValueAsInteger(TotalItemsTable.PickedUp.toString());
		}
	}

	@Override
	public boolean pushData() {
		return QueryUtils.update(
			TotalItemsTable.TableName.toString(),
			getValues(),
			TotalItemsTable.PlayerId + " = " + playerId,
			TotalItemsTable.MaterialId + " = " + itemStack.getTypeId()
		);
	}
	
	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TotalItemsTable.PlayerId.toString(), playerId);
		map.put(TotalItemsTable.MaterialId.toString(), itemStack.getTypeId());
		map.put(TotalItemsTable.Dropped.toString(), dropped);
		map.put(TotalItemsTable.PickedUp.toString(), pickedUp);
		return map;
	}
	
	/**
	 * Returns the item stack
	 * @return <b>ItemStack</b> material
	 */
	public ItemStack getItemStack() { return itemStack; }
	
	/**
	 * Adds the specified number of blocks to the total number of blocks dropped
	 * @param blocks Items to add
	 */
	public void addDropped() { dropped++; }
	
	/**
	 * Adds the specified number of blocks to the total number of blocks picked up
	 * @param blocks Items to add
	 */
	public void addPickedUp() { pickedUp++; }

}
