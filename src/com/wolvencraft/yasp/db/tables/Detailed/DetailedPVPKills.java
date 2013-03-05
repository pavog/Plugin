package com.wolvencraft.yasp.db.tables.detailed;

public enum DetailedPVPKills implements _DetailedTable {
	
	TableName("detailed_pvp_kills"),
	
	EntryId("detailed_pvp_id"),
	MaterialID("material_id"),
	MaterialData("material_data"),
	KillerID("player_id"),
	VictimID("victim_id"),
	Cause("cause"),
	World("world"),
	XCoord("x"),
	YCoord("y"),
	ZCoord("z"),
	Timestamp("time");
	
	DetailedPVPKills (String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
