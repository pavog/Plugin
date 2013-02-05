package com.wolvencraft.yasp.Database.tables.detailed;

import com.wolvencraft.yasp.Database.tables.DBTable;

public enum BlocksPlacedTable implements DBTable {
	
	TableName("detailed_blocks_placed"),
	PlacedBlockId("bplacedID"),
	MaterialId("material_id"),
	PlayerId("player_id"),
	World("world"),
	XCoord("x"),
	YCoord("y"),
	ZCoord("z"),
	Timestamp("time");
	
	BlocksPlacedTable(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String getColumnName() { return columnName; }
}
