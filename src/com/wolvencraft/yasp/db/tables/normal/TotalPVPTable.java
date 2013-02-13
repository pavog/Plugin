package com.wolvencraft.yasp.db.tables.normal;

import com.wolvencraft.yasp.db.tables.DBTable;

public enum TotalPVPTable implements DBTable {
	
	TableName("total_pvp"),
	TotalBlocksId("total_pvp_id"),
	PlayerId("player_id"),
	VictimId("victim_id"),
	Times("times");
	
	TotalPVPTable(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
