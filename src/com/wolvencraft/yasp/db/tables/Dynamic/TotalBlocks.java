package com.wolvencraft.yasp.db.tables.Dynamic;


public enum TotalBlocks implements _DynamicTable {
	
	TableName("total_blocks"),
	
	EntryId("total_blocks_id"),
	MaterialId("material_id"),
	PlayerId("player_id"),
	Destroyed("destroyed"),
	Placed("placed");
	
	TotalBlocks(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
