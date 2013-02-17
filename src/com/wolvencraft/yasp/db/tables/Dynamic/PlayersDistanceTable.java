package com.wolvencraft.yasp.db.tables.Dynamic;



public enum PlayersDistanceTable implements DynamicTable {
	
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
	public String toString() { return columnName; }
	
}
