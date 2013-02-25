package com.wolvencraft.yasp.db.tables.normal;

public enum TotalDeathPlayers implements _DynamicTable {
	
	TableName("total_death_players"),
	
	TotalBlocksId("total_death_players_id"),
	PlayerId("player_id"),
	Cause("cause"),
	Times("times");
	
	TotalDeathPlayers(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
