package com.wolvencraft.yasp.db.tables.detailed;

import com.wolvencraft.yasp.db.tables.DBTable;

public enum ItemsUsedTable implements DBTable {

	TableName("items_use"),
	ItemUseId("iuseID"),
	MaterialId("material_id"),
	PlayerId("player_id"),
	Times("times");
	
	ItemsUsedTable(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
