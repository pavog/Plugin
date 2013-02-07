package com.wolvencraft.yasp.db.tables.normal;

import com.wolvencraft.yasp.db.tables.DBTable;

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
