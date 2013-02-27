package com.wolvencraft.yasp.db.tables.detailed;

public enum DetailedDroppedItems implements _DetailedTable {

	TableName("detailed_dropped_items"),
	
	EntryId("detailed_dropped_items_id"),
	MaterialId("material_id"),
	MaterialData("material_data"),
	PlayerId("player_id"),
	World("world"),
	XCoord("x"),
	YCoord("y"),
	ZCoord("z"),
	Timestamp("time");
	
	DetailedDroppedItems(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
