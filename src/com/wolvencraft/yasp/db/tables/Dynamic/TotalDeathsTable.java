package com.wolvencraft.yasp.db.tables.Dynamic;


public enum TotalDeathsTable implements DynamicTable {
	
	TableName("total_deaths"),
	TotalBlocksId("totaldeathsID"),
	PlayerId("player_id"),
	Cause("cause"),
	Times("times");
	
	TotalDeathsTable(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
