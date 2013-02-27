package com.wolvencraft.yasp.db.tables.normal;


public enum TotalItems implements _NormalTable {
	
	TableName("total_items"),
	
	TotalBlocksId("total_items_id"),
	MaterialId("material_id"),
	PlayerId("player_id"),
	Dropped("dropped"),
	PickedUp("picked_up"),
	Used("used"),
	Crafted("crafted"),
	Smelted("smelted");
	
	TotalItems(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
