package com.wolvencraft.yasp.Database.tables.detailed;

import com.wolvencraft.yasp.Database.tables.DBTable;


public enum PlayersLogTable implements DBTable {
	
	TableName("detailed_players_log"),
	LogId("logID"),
	PlayerId("player_id"),
	LoggedIn("logged_in"),
	LoggedOut("logged_out"),
	World("world"),
	XCoord("x"),
	YCoord("y"),
	ZCoord("z");
	
	PlayersLogTable(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String getColumnName() { return columnName; }

}
