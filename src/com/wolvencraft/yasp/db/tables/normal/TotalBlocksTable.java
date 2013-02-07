package com.wolvencraft.yasp.db.tables.normal;

import com.wolvencraft.yasp.db.tables.DBTable;

public enum TotalBlocksTable implements DBTable {
	
	TotalBlocksId("totalblocksID"),
	MaterialId("material_id"),
	PlayerId("player_id"),
	Destroyed("destroyed"),
	Placed("placed");
	
	TotalBlocksTable(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String getColumnName() { return columnName; }
}
