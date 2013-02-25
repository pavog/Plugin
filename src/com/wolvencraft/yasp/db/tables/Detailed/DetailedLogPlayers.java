package com.wolvencraft.yasp.db.tables.detailed;

public enum DetailedLogPlayers implements _DetailedData {
	
	TableName("detailed_log_players"),
	
	EntryId("logID"),
	PlayerId("player_id"),
	Time("time"),
	IsLogin("is_login"),
	World("world"),
	XCoord("x"),
	YCoord("y"),
	ZCoord("z");
	
	DetailedLogPlayers(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }

}
