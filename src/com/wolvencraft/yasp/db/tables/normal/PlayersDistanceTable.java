package com.wolvencraft.yasp.db.tables.normal;

import com.wolvencraft.yasp.db.tables.DBTable;


public enum PlayersDistanceTable implements DBTable {
	
	TableName("players_distance"),
	DistanceId("distID"),
	PlayerId("player_id"),
	Foot("foot"),
	Boat("boat"),
	Minecart("minecart"),
	Pig("pig");
	
	PlayersDistanceTable(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String getColumnName() { return columnName; }
	
	@Override
	public String toString() { return columnName; }
	
}
