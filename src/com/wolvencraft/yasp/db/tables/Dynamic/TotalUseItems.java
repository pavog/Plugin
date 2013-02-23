package com.wolvencraft.yasp.db.tables.Dynamic;

public enum TotalUseItems implements _DynamicTable {
	
	TableName("total_use_items"),
	
	EntryId("total_items_use_id"),
	MaterialId("material_id"),
	PlayerId("player_id"),
	Times("times");
	
	TotalUseItems(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;

	@Override
	public String toString() { return columnName; }
}
