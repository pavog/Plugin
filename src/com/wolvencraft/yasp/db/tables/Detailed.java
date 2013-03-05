package com.wolvencraft.yasp.db.tables;

public class Detailed {
	
	public enum DestroyedBlocks {
		TableName("detailed_destroyed_blocks"),
		EntryId("detailed_destroyed_blocks_id"),
		MaterialId("material_id"),
		MaterialData("material_data"),
		PlayerId("player_id"),
		World("world"),
		XCoord("x"),
		YCoord("y"),
		ZCoord("z"),
		Timestamp("time");
		
		DestroyedBlocks(String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	public enum PlacedBlocks {
		TableName("detailed_placed_blocks"),
		EntryId("detailed_placed_blocks_id"),
		MaterialId("material_id"),
		MaterialData("material_data"),
		PlayerId("player_id"),
		World("world"),
		XCoord("x"),
		YCoord("y"),
		ZCoord("z"),
		Timestamp("time");
		
		PlacedBlocks(String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	public enum DroppedItems {
		TableName("detailed_dropped_items"),
		EntryId("detailed_dropped_items_id"),
		MaterialId("material_id"),
		MaterialData("material_data"),
		PlayerId("player_id"),
		World("world"),
		XCoord("x"),
		YCoord("y"),
		ZCoord("z"),
		Timestamp("time");
		
		DroppedItems(String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	public enum PickedupItems {

		TableName("detailed_pickedup_items"),
		
		EntryId("detailed_pickedup_items_id"),
		MaterialId("material_id"),
		MaterialData("material_data"),
		PlayerId("player_id"),
		World("world"),
		XCoord("x"),
		YCoord("y"),
		ZCoord("z"),
		Timestamp("time");
		
		PickedupItems(String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	public enum UsedItems {
		TableName("detailed_used_items"),
		EntryId("detailed_used_items_id"),
		MaterialId("material_id"),
		MaterialData("material_data"),
		PlayerId("player_id"),
		World("world"),
		XCoord("x"),
		YCoord("y"),
		ZCoord("z"),
		Timestamp("time");
		
		UsedItems(String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	public enum LogPlayers {
		TableName("detailed_log_players"),
		EntryId("detailed_log_players_id"),
		PlayerId("player_id"),
		Timestamp("time"),
		World("world"),
		XCoord("x"),
		YCoord("y"),
		ZCoord("z"),
		IsLogin("is_login");
		
		LogPlayers(String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	public enum DeathPlayers {
		TableName("detailed_death_players"),
		EntryId("detailed_death_players_id"),
		PlayerId("player_id"),
		Cause("cause"),
		World("world"),
		XCoord("x"),
		YCoord("y"),
		ZCoord("z"),
		Timestamp("time");
		
		DeathPlayers (String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	public enum PVEKills {
		TableName("detailed_pve_kills"),
		EntryId("detailed_pve_id"),
		MaterialId("material_id"),
		MaterialData("material_data"),
		CreatureId("entity_id"),
		PlayerID("player_id"),
		World("world"),
		XCoord("x"),
		YCoord("y"),
		ZCoord("z"),
		Timestamp("time"),
		PlayerKilled("player_killed");
		
		PVEKills (String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	public enum PVPKills {
		TableName("detailed_pvp_kills"),
		EntryId("detailed_pvp_id"),
		MaterialID("material_id"),
		MaterialData("material_data"),
		KillerID("player_id"),
		VictimID("victim_id"),
		Cause("cause"),
		World("world"),
		XCoord("x"),
		YCoord("y"),
		ZCoord("z"),
		Timestamp("time");
		
		PVPKills (String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
}
