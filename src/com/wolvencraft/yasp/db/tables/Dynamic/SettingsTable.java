package com.wolvencraft.yasp.db.tables.Dynamic;


public enum SettingsTable implements DynamicTable {
	
	TableName("settings"),
	Key("key"),
	Value("value");
	
	SettingsTable(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
