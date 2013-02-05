package com.wolvencraft.yasp.Database.tables.normal;

import com.wolvencraft.yasp.Database.tables.DBTable;


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
	
}
