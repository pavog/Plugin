package com.wolvencraft.yasp.db.tables.Static;


public enum PlayersDeathTable implements StaticTable {

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
	public String toString() { return columnName; }
}
