package com.wolvencraft.yasp.Database.tables.normal;

import com.wolvencraft.yasp.Database.tables.DBTable;

public enum SettingsTable implements DBTable {
	
	TableName("settings"),
	Key("key"),
	Value("value");
	
	SettingsTable(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String getColumnName() { return columnName; }
}
