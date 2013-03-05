package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;

import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal.TotalBlocks;

public class TotalBlocksEntry implements _NormalData {
	
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
			TotalBlocks.TableName.toString(),
			new String[] {"*"},
			new String[] { TotalBlocks.PlayerId.toString(), playerId + ""},
			new String[] { TotalBlocks.MaterialId.toString(), type + ""},
			new String[] { TotalBlocks.MaterialData.toString(), data + ""}
		);
		
		if(results.isEmpty()) QueryUtils.insert(TotalBlocks.TableName.toString(), getValues(playerId));
		else {
			broken = results.get(0).getValueAsInteger(TotalBlocks.Destroyed.toString());
			placed = results.get(0).getValueAsInteger(TotalBlocks.Placed.toString());
		}
	}

	@Override
	public boolean pushData(int playerId) {
		return QueryUtils.update(
			TotalBlocks.TableName.toString(),
			getValues(playerId),
			new String[] { TotalBlocks.PlayerId.toString(), playerId + ""},
			new String[] { TotalBlocks.MaterialId.toString(), type + ""},
			new String[] { TotalBlocks.MaterialData.toString(), data + ""}
		);
	}
	
	@Override
	public Map<String, Object> getValues(int playerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TotalBlocks.PlayerId.toString(), playerId);
		map.put(TotalBlocks.MaterialId.toString(), type);
		map.put(TotalBlocks.MaterialData.toString(), data);
		map.put(TotalBlocks.Destroyed.toString(), broken);
		map.put(TotalBlocks.Placed.toString(), placed);
		return map;
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
	
	/**
	 * Checks if the object corresponds to provided parameters
	 * @param testMaterial MaterialData object
	 * @return <b>true</b> if the conditions are met, <b>false</b> otherwise
	 */
	public boolean equals(Material materialType, byte data) {
		return this.type == materialType.getId() && this.data == data;
	}
}
