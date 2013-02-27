package com.wolvencraft.yasp.db.tables.normal;


public enum TotalPVEKills implements _NormalTable {
	
	TableName("total_pve_kills"),
	
	EntryId("total_pve_id"),
	MaterialId("material_id"),
	MaterialData("material_data"),
	CreatureId("creature_id"),
	PlayerId("player_id"),
	PlayerKilled("player_killed"),
	CreatureKilled("creature_killed");
	
	TotalPVEKills(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
