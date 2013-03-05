package com.wolvencraft.yasp.db.tables.detailed;

public enum DetailedPVEKills implements _DetailedTable {

	TableName("detailed_pve_kills"),
	
	EntryId("detailed_pve_id"),
	PlayerID("player_id"),
	CreatureId("entity_id"),
	MaterialId("material_id"),
	MaterialData("material_data"),
	PlayerKilled("player_killed"),
	World("world"),
	XCoord("x"),
	YCoord("y"),
	ZCoord("z"),
	Timestamp("time");
	
	DetailedPVEKills (String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
