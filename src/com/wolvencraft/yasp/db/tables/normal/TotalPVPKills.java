package com.wolvencraft.yasp.db.tables.normal;


public enum TotalPVPKills implements _DynamicTable {
	
	TableName("total_pvp_kills"),
	
	TotalBlocksId("total_pvp_id"),
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
