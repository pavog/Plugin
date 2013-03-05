package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Normal;

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
			Normal.TotalItems.TableName.toString(),
			new String[] {"*"},
			new String[] { Normal.TotalItems.PlayerId.toString(), playerId + ""},
			new String[] { Normal.TotalItems.MaterialId.toString(), type + ""},
			new String[] { Normal.TotalItems.MaterialData.toString(), data + ""}
		);
		
		if(results.isEmpty()) QueryUtils.insert(Normal.TotalItems.TableName.toString(), getValues(playerId));
		else {
			dropped = results.get(0).getValueAsInteger(Normal.TotalItems.Dropped.toString());
			pickedUp = results.get(0).getValueAsInteger(Normal.TotalItems.PickedUp.toString());
			used = results.get(0).getValueAsInteger(Normal.TotalItems.Used.toString());
			crafted = results.get(0).getValueAsInteger(Normal.TotalItems.Crafted.toString());
			broken = results.get(0).getValueAsInteger(Normal.TotalItems.Broken.toString());
			smelted = results.get(0).getValueAsInteger(Normal.TotalItems.Smelted.toString());
			enchanted = results.get(0).getValueAsInteger(Normal.TotalItems.Enchanted.toString());
		}
	}

	@Override
	public boolean pushData(int playerId) {
		return QueryUtils.update(
			Normal.TotalItems.TableName.toString(),
			getValues(playerId),
			new String[] { Normal.TotalItems.PlayerId.toString(), playerId + ""},
			new String[] { Normal.TotalItems.MaterialId.toString(), type + ""},
			new String[] { Normal.TotalItems.MaterialData.toString(), data + ""}
		);
	}
	
	@Override
	public Map<String, Object> getValues(int playerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Normal.TotalItems.PlayerId.toString(), playerId);
		map.put(Normal.TotalItems.MaterialId.toString(), type);
		map.put(Normal.TotalItems.MaterialData.toString(), data);
		map.put(Normal.TotalItems.Dropped.toString(), dropped);
		map.put(Normal.TotalItems.PickedUp.toString(), pickedUp);
		map.put(Normal.TotalItems.Used.toString(), used);
		map.put(Normal.TotalItems.Crafted.toString(), crafted);
		map.put(Normal.TotalItems.Broken.toString(), broken);
		map.put(Normal.TotalItems.Smelted.toString(), smelted);
		map.put(Normal.TotalItems.Enchanted.toString(), enchanted);
		return map;
	}
	
	/**
	 * Checks if the ItemStack corresponds to this entry 
	 * @param itemStack ItemStack to check
	 * @return <b>boolean</b>
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
