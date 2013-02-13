package com.wolvencraft.yasp.db.tables.normal;

import com.wolvencraft.yasp.db.tables.DBTable;

public enum TotalItemsTable implements DBTable {
	
	TableName("total_items"),
	TotalBlocksId("total_items_id"),
	MaterialId("material_id"),
	PlayerId("player_id"),
	Dropped("drop"),
	PickedUp("pickup");
	
	TotalItemsTable(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
