package com.wolvencraft.yasp.db.tables.normal;

import com.wolvencraft.yasp.db.tables.DBTable;

public enum TotalDeathsTable implements DBTable {
	
	TotalBlocksId("totaldeathsID"),
	PlayerId("player_id"),
	Cause("cause"),
	Times("times");
	
	TotalDeathsTable(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String getColumnName() { return columnName; }
}
