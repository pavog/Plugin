package com.wolvencraft.yasp.db.tables.Static;

public enum PVPTable implements StaticTable {
	
	TableName("detailed_pvp"),
	PvpId("pvpID"),
	PlayerID("player_id"),
	VictimID("victim_id"),
	MaterialID("material_id"),
	Cause("cause"),
	World("world"),
	XCoord("x"),
	YCoord("y"),
	ZCoord("z"),
	Timestamp("time");
	
	PVPTable (String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
