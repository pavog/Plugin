package com.wolvencraft.yasp.Database.tables;

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
	public String getColumnName() { return columnName; }
}
