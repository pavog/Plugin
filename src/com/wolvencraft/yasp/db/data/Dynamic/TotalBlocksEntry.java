package com.wolvencraft.yasp.db.data.Dynamic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.material.MaterialData;

import com.wolvencraft.yasp.db.DBEntry;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Dynamic.TotalBlocksTable;

public class TotalBlocksEntry implements DynamicData {
	
	public TotalBlocksEntry(int playerId, MaterialData material) {
		
		this.playerId = playerId;
		this.material = material;
		this.broken = 0;
		this.placed = 0;
	}
	
	private int playerId;
	private MaterialData material;
	private int broken;
	private int placed;
	
	@Override
	public void fetchData() {
		List<DBEntry> results = QueryUtils.select(
			TotalBlocksTable.TableName.toString(),
			"*",
			TotalBlocksTable.PlayerId + " = " + playerId
		);
		
		if(results.isEmpty()) QueryUtils.insert(TotalBlocksTable.TableName.toString(), getValues());
		else {
			broken = results.get(0).getValueAsInteger(TotalBlocksTable.Destroyed.toString());
			placed = results.get(0).getValueAsInteger(TotalBlocksTable.Placed.toString());
		}
	}

	@Override
	public boolean pushData() {
		return QueryUtils.update(
			TotalBlocksTable.TableName.toString(),
			getValues(),
			TotalBlocksTable.PlayerId + " = " + playerId,
			TotalBlocksTable.MaterialId + " = " + material.getItemTypeId()
		);
	}
	
	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TotalBlocksTable.PlayerId.toString(), playerId);
		map.put(TotalBlocksTable.MaterialId.toString(), material.getItemTypeId());
		map.put(TotalBlocksTable.Destroyed.toString(), broken);
		map.put(TotalBlocksTable.Placed.toString(), placed);
		return map;
	}
	
	/**
	 * Returns the material data
	 * @return <b>MaterialData</b> material
	 */
	public MaterialData getMaterial() { return material; }
	
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
	
	/**
	 * Checks if the object corresponds to provided parameters
	 * @param testMaterial MaterialData object
	 * @return <b>true</b> if the conditions are met, <b>false</b> otherwise
	 */
	public boolean equals(MaterialData testMaterial) {
		return material.equals(testMaterial);
	}
}
