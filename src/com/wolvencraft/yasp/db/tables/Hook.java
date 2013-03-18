package com.wolvencraft.yasp.db.tables;

public class Hook {
	
	public enum VaultTable {
		TableName("hook_vault"),
		PlayerId("player_id"),
		GroupName("group_name"),
		Balance("balance");
		
		VaultTable (String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	public enum WorldGuardTable {
		TableName("hook_worldguard"),
		PlayerId("player_id"),
		RegionName("region_name");
		
		WorldGuardTable (String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
}
