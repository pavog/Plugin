package com.wolvencraft.yasp.Database.tables.detailed;

import com.wolvencraft.yasp.Database.tables.DBTable;

public enum ItemsDroppedTable implements DBTable {

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
	public String getColumnName() { return columnName; }
}
