package com.wolvencraft.yasp.db.tables.Static;

public enum BlocksPlacedTable implements StaticTable {
	
	TableName("detailed_blocks_placed"),
	PlacedBlockId("bplacedID"),
	MaterialId("material_id"),
	PlayerId("player_id"),
	World("world"),
	XCoord("x"),
	YCoord("y"),
	ZCoord("z"),
	Timestamp("time");
	
	BlocksPlacedTable(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
