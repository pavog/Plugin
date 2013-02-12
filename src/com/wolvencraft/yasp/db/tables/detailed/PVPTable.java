package com.wolvencraft.yasp.db.tables.detailed;

import com.wolvencraft.yasp.db.tables.DBTable;


public enum PVPTable implements DBTable {
	
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
	public String getColumnName() { return columnName; }
	
	@Override
	public String toString() { return columnName; }
}
