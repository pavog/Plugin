package com.wolvencraft.yasp.db.tables.normal;


public enum TotalPVEKills implements _DynamicTable {
	
	TableName("total_pve_kills"),
	
	TotalPVEId("total_pve_id"),
	PlayerId("player_id"),
	CreatureId("creature_id"),
	PlayerKilled("player_killed"),
	CreatureKilled("creature_killed");
	
	TotalPVEKills(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
