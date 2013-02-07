package com.wolvencraft.yasp.db.tables.detailed;

import com.wolvencraft.yasp.db.tables.DBTable;


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
