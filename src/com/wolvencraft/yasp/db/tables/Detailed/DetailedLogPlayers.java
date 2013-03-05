package com.wolvencraft.yasp.db.tables.detailed;

public enum DetailedLogPlayers implements _DetailedTable {
	
	TableName("detailed_log_players"),
	
	EntryId("detailed_log_players_id"),
	PlayerId("player_id"),
	Timestamp("time"),
	World("world"),
	XCoord("x"),
	YCoord("y"),
	ZCoord("z"),
	IsLogin("is_login");
	
	DetailedLogPlayers(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }

}
