package com.wolvencraft.yasp.db.tables.detailed;

public enum DetailedUsedItems implements _DetailedTable {

	TableName("detailed_used_items"),
	
	EntryId("detailed_used_items_id"),
	MaterialId("material_id"),
	MaterialData("material_data"),
	PlayerId("player_id"),
	World("world"),
	XCoord("x"),
	YCoord("y"),
	ZCoord("z"),
	Timestamp("time");
	
	DetailedUsedItems(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
