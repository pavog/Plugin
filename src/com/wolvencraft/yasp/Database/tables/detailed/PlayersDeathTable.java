package com.wolvencraft.yasp.Database.tables.detailed;

import com.wolvencraft.yasp.Database.tables.DBTable;


public enum PlayersDeathTable implements DBTable {

	TableName("detailed_pvp"),
	DeathId("pdeathID"),
	PlayerId("player_id"),
	Cause("cause"),
	World("world"),
	XCoord("x"),
	YCoord("y"),
	ZCoord("z"),
	Timestamp("time");
	
	PlayersDeathTable (String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String getColumnName() { return columnName; }
}
