package com.wolvencraft.yasp.Database.DataValues;

public enum DataStores {
	CONFIGURATION("config"),
	PLAYER("players");

	private final String tableName;

	private DataStores(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return this.tableName;
	}
}
