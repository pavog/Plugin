package com.wolvencraft.yasp.db.tables.Static;

public enum ItemsDroppedTable implements StaticTable {

	TableName("detailed_items_drop"),
	DestroyedBlockId("idropID"),
	MaterialId("material_id"),
	PlayerId("player_id"),
	World("world"),
	XCoord("x"),
	YCoord("y"),
	ZCoord("z"),
	Timestamp("time");
	
	ItemsDroppedTable(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
