package com.wolvencraft.yasp.db.tables.detailed;

public enum DetailedPlacedBlocks implements _DetailedData {
	
	TableName("detailed_placed_blocks"),
	
	EntryId("detailed_placed_blocks_id"),
	MaterialId("material_id"),
	PlayerId("player_id"),
	World("world"),
	XCoord("x"),
	YCoord("y"),
	ZCoord("z"),
	Timestamp("time");
	
	DetailedPlacedBlocks(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
