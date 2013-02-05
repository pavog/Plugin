package com.wolvencraft.yasp.Database.tables.normal;

import com.wolvencraft.yasp.Database.tables.DBTable;

public enum TotalPVPTable implements DBTable {
	
	TotalBlocksId("total_pvp_id"),
	PlayerId("player_id"),
	VictimId("victim_id"),
	Times("times");
	
	TotalPVPTable(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String getColumnName() { return columnName; }
}
