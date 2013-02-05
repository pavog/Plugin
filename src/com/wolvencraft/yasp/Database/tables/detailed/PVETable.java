package com.wolvencraft.yasp.Database.tables.detailed;

import com.wolvencraft.yasp.Database.tables.DBTable;


public enum PVETable implements DBTable {

	TableName("detailed_pvp"),
	PveId("pve_id"),
	PlayerID("player_id"),
	CreatureID("creature_id"),
	MaterialID("material_id"),
	Cause("cause"),
	PlayerKilled("player_killed"),
	World("world"),
	XCoord("x"),
	YCoord("y"),
	ZCoord("z"),
	Timestamp("time");
	
	PVETable (String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String getColumnName() { return columnName; }
}
