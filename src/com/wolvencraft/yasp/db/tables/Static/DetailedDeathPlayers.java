package com.wolvencraft.yasp.db.tables.Static;


public enum DetailedDeathPlayers implements _StaticTable {

	TableName("detailed_death_players"),
	
	EntryId("detailed_death_players_id"),
	PlayerId("player_id"),
	Cause("cause"),
	World("world"),
	XCoord("x"),
	YCoord("y"),
	ZCoord("z"),
	Timestamp("time");
	
	DetailedDeathPlayers (String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
