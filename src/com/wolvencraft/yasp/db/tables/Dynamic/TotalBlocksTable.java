package com.wolvencraft.yasp.db.tables.Dynamic;


public enum TotalBlocksTable implements DynamicTable {
	
	TableName("total_blocks"),
	TotalBlocksId("total_blocks_id"),
	MaterialId("material_id"),
	PlayerId("player_id"),
	Destroyed("destroyed"),
	Placed("placed");
	
	TotalBlocksTable(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
