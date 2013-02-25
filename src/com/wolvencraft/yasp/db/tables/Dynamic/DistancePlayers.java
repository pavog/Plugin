package com.wolvencraft.yasp.db.tables.Dynamic;

public enum DistancePlayers implements _DynamicTable {
	
	TableName("distance_players"),
	
	DistanceId("distance_player_id"),
	PlayerId("player_id"),
	Foot("foot"),
	Boat("boat"),
	Minecart("minecart"),
	Pig("pig");
	
	DistancePlayers(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
	
}
