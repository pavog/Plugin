package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.material.MaterialData;

import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal.TotalBlocks;

public class TotalBlocksEntry implements _NormalData {
	
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
		List<QueryResult> results = QueryUtils.select(
			TotalBlocks.TableName.toString(),
			"*",
			TotalBlocks.PlayerId.toString() + " = " + playerId,
			TotalBlocks.MaterialId.toString() + " = " + material.getItemTypeId(),
			TotalBlocks.MaterialData.toString() + " = " + material.getData()
		);
		
		if(results.isEmpty()) QueryUtils.insert(TotalBlocks.TableName.toString(), getValues());
		else {
			broken = results.get(0).getValueAsInteger(TotalBlocks.Destroyed.toString());
			placed = results.get(0).getValueAsInteger(TotalBlocks.Placed.toString());
		}
	}

	@Override
	public boolean pushData() {
		return QueryUtils.update(
			TotalBlocks.TableName.toString(),
			getValues(),
			TotalBlocks.PlayerId.toString() + " = " + playerId,
			TotalBlocks.MaterialId.toString() + " = " + material.getItemTypeId(),
			TotalBlocks.MaterialData.toString() + " = " + material.getData()
		);
	}
	
	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TotalBlocks.PlayerId.toString(), playerId);
		map.put(TotalBlocks.MaterialId.toString(), material.getItemTypeId());
		map.put(TotalBlocks.MaterialData.toString(), material.getData());
		map.put(TotalBlocks.Destroyed.toString(), broken);
		map.put(TotalBlocks.Placed.toString(), placed);
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
