package com.wolvencraft.yasp.db.tables.normal;

public enum _ServerStatistics implements _NormalTable {
	
	TableName("server_statistics"),
	
	Key("key"),
	Value("value");
	
	_ServerStatistics(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
	
}
