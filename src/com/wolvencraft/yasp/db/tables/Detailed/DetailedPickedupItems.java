package com.wolvencraft.yasp.db.tables.Detailed;

public enum DetailedPickedupItems implements _DetailedData {

	TableName("detailed_pickedup_items"),
	
	EntryId("detailed_pickedup_items_id"),
	MaterialId("material_id"),
	PlayerId("player_id"),
	World("world"),
	XCoord("x"),
	YCoord("y"),
	ZCoord("z"),
	Timestamp("time");
	
	DetailedPickedupItems(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
