package com.wolvencraft.yasp.db.tables.detailed;

public enum DetailedUsedItems implements _DetailedData {

	TableName("detailed_used_items"),
	
	ItemUseId("detailed_used_items_id"),
	MaterialId("material_id"),
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
