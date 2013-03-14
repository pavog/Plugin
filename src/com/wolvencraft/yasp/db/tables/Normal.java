package com.wolvencraft.yasp.db.tables;

public class Normal {
	
	/**
	 * Represents the $prefix_settings table in the database.
	 * @author bitWolfy
	 *
	 */
	public enum SettingsTable {
		TableName("settings"),
		Key("key"),
		Value("value");
		
		SettingsTable (String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	/**
	 * Represents the $prefix_server_statistics table in the database.
	 * @author bitWolfy
	 *
	 */
	public enum ServerStatsTable {
		TableName("server_statistics"),
		Key("key"),
		Value("value");
		
		ServerStatsTable (String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	/**
	 * Represents the $prefix_distance_players table in the database.
	 * @author bitWolfy
	 *
	 */
	public enum DistancePlayersTable {
		TableName("distance_players"),
		PlayerId("player_id"),
		Foot("foot"),
		Swimmed("swimmed"),
		Boat("boat"),
		Minecart("minecart"),
		Pig("pig");
		
		DistancePlayersTable (String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	/**
	 * Represents the $prefix_players table in the database.
	 * @author bitWolfy
	 *
	 */
	public enum PlayersTable{
		TableName("players"),
		PlayerId("player_id"),
		Name("name"),
		Online("online"),
		SessionStart("login_time"),
		TotalPlaytime("playtime"),
		FirstLogin("first_login"),
		Logins("logins");
		
		PlayersTable (String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	/**
	 * Represents the $prefix_players_misc table in the database.
	 * @author bitWolfy
	 *
	 */
	public enum MiscInfoPlayersTable {
		TableName("misc_info_players"),
		EntryId("misc_info_players_id"),
		PlayerId("player_id"),
		Gamemode("gamemode"),
		ExperiencePercent("exp_perc"),
		ExperienceTotal("exp_total"),
		ExperienceLevel("exp_level"),
		FoodLevel("food_level"),
		HealthLevel("health"),
		FishCaught("fish_caught"),
		TimesKicked("times_kicked"),
		EggsThrown("eggs_thrown"),
		FoodEaten("food_eaten"),
		ArrowsShot("arrows_shot"),
		DamageTaken("damage_taken"),
		BedsEntered("beds_entered"),
		PortalsEntered("portals_entered"),
		WordsSaid("words_said"),
		CommandsSent("commands_sent");
		
		MiscInfoPlayersTable (String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	/**
	 * Represents the $prefix_total_blocks table in the database.
	 * @author bitWolfy
	 *
	 */
	public enum TotalBlocksTable {
		TableName("total_blocks"),
		EntryId("total_blocks_id"),
		Material("material_id"),
		PlayerId("player_id"),
		Destroyed("destroyed"),
		Placed("placed");
		
		TotalBlocksTable (String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	/**
	 * Represents the $prefix_total_items table in the database.
	 * @author bitWolfy
	 *
	 */
	public enum TotalItemsTable {
		TableName("total_items"),
		EntryId("total_items_id"),
		Material("material_id"),
		PlayerId("player_id"),
		Dropped("dropped"),
		PickedUp("picked_up"),
		Used("used"),
		Crafted("crafted"),
		Smelted("smelted"),
		Broken("broken"),
		Enchanted("enchanted");
		
		TotalItemsTable (String columnName) { this.columnName = columnName;}
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	/**
	 * Represents the $prefix_total_death_players table in the database.
	 * @author bitWolfy
	 *
	 */
	public enum TotalDeathPlayersTable {
		TableName("total_death_players"),
		EntryId("total_death_players_id"),
		PlayerId("player_id"),
		Cause("cause"),
		Times("times");
		
		TotalDeathPlayersTable (String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	/**
	 * Represents the $prefix_total_pve_kills table in the database.
	 * @author bitWolfy
	 *
	 */
	public enum TotalPVEKillsTable {
		TableName("total_pve_kills"),
		EntryId("total_pve_id"),
		Material("material_id"),
		CreatureId("entity_id"),
		PlayerId("player_id"),
		PlayerKilled("player_killed"),
		CreatureKilled("creature_killed");
		
		TotalPVEKillsTable (String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	/**
	 * Represents the $prefix_total_pvp_kills table in the database.
	 * @author bitWolfy
	 *
	 */
	public enum TotalPVPKillsTable {
		TableName("total_pvp_kills"),
		EntryId("total_pvp_id"),
		Material("material_id"),
		PlayerId("player_id"),
		VictimId("victim_id"),
		Times("times");
		
		TotalPVPKillsTable (String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
}
