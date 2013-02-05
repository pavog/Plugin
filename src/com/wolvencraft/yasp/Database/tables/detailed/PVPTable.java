package com.wolvencraft.yasp.Database.tables.detailed;

import com.wolvencraft.yasp.Database.tables.DBTable;


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
}
