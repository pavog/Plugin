package com.wolvencraft.yasp.db.tables.Static;

public enum BlocksDestroyedTable implements StaticTable {
	
	TableName("detailed_blocks_destroyed"),
	DestroyedBlockId("bdestroyedID"),
	MaterialId("material_id"),
	PlayerId("player_id"),
	World("world"),
	XCoord("x"),
	YCoord("y"),
	ZCoord("z"),
	Timestamp("time");
	
	BlocksDestroyedTable(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
