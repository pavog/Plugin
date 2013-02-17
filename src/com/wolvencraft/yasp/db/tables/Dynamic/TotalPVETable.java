package com.wolvencraft.yasp.db.tables.Dynamic;


public enum TotalPVETable implements DynamicTable {
	
	TableName("total_pve"),
	TotalPVEId("total_pve_id"),
	PlayerId("player_id"),
	CreatureId("creature_id"),
	PlayerKilled("player_killed"),
	CreatureKilled("creature_killed");
	
	TotalPVETable(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
