package com.wolvencraft.yasp.db.tables.Static;

public enum PVETable implements StaticTable {

	TableName("detailed_pvp"),
	PveId("pve_id"),
	PlayerID("player_id"),
	CreatureID("creature_id"),
	MaterialID("material_id"),
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
	public String toString() { return columnName; }
}
