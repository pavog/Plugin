package com.wolvencraft.yasp.db.tables.normal;


public enum TotalPVPKills implements _NormalTable {
	
	TableName("total_pvp_kills"),
	
	EntryId("total_pvp_id"),
	MaterialId("material_id"),
	MaterialData("material_data"),
	PlayerId("player_id"),
	VictimId("victim_id"),
	Times("times");
	
	TotalPVPKills(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
