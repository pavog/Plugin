package com.wolvencraft.yasp.db.tables;

public class Normal {
	
	/**
	 * Represents the $prefix_settings table in the database.
	 * @author bitWolfy
	 *
	 */
	public enum Settings {
		TableName("settings"),
		Key("key"),
		Value("value");
		
		Settings(String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	/**
	 * Represents the $prefix_server_statistics table in the database.
	 * @author bitWolfy
	 *
	 */
	public enum ServerStatistics {
		TableName("server_statistics"),
		Key("key"),
		Value("value");
		
		ServerStatistics(String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	/**
	 * Represents the $prefix_distance_players table in the database.
	 * @author bitWolfy
	 *
	 */
	public enum DistancePlayers {
		TableName("distance_players"),
		EntryId("distance_player_id"),
		PlayerId("player_id"),
		Foot("foot"),
		Swimmed("swimmed"),
		Boat("boat"),
		Minecart("minecart"),
		Pig("pig");
		
		DistancePlayers(String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	/**
	 * Represents the $prefix_players table in the database.
	 * @author bitWolfy
	 *
	 */
	public enum Players{
		TableName("players"),
		PlayerId("player_id"),
		Name("name"),
		Online("online"),
		SessionStart("login_time"),
		FirstLogin("first_login"),
		Logins("logins");
		
		Players(String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	/**
	 * Represents the $prefix_players_misc table in the database.
	 * @author bitWolfy
	 *
	 */
	public enum PlayersMisc {
		TableName("misc_info_players"),
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
		
		PlayersMisc (String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	/**
	 * Represents the $prefix_total_blocks table in the database.
	 * @author bitWolfy
	 *
	 */
	public enum TotalBlocks {
		TableName("total_blocks"),
		EntryId("total_blocks_id"),
		MaterialId("material_id"),
		MaterialData("material_data"),
		PlayerId("player_id"),
		Destroyed("destroyed"),
		Placed("placed");
		
		TotalBlocks(String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	/**
	 * Represents the $prefix_total_items table in the database.
	 * @author bitWolfy
	 *
	 */
	public enum TotalItems {
		TableName("total_items"),
		EntryId("total_items_id"),
		MaterialId("material_id"),
		MaterialData("material_data"),
		PlayerId("player_id"),
		Dropped("dropped"),
		PickedUp("picked_up"),
		Used("used"),
		Crafted("crafted"),
		Smelted("smelted"),
		Broken("broken"),
		Enchanted("enchanted");
		
		TotalItems(String columnName) { this.columnName = columnName;}
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	/**
	 * Represents the $prefix_total_death_players table in the database.
	 * @author bitWolfy
	 *
	 */
	public enum TotalDeathPlayers {
		TableName("total_death_players"),
		EntryId("total_death_players_id"),
		PlayerId("player_id"),
		Cause("cause"),
		Times("times");
		
		TotalDeathPlayers(String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	/**
	 * Represents the $prefix_total_pve_kills table in the database.
	 * @author bitWolfy
	 *
	 */
	public enum TotalPVEKills {
		TableName("total_pve_kills"),
		EntryId("total_pve_id"),
		MaterialId("material_id"),
		MaterialData("material_data"),
		CreatureId("entity_id"),
		PlayerId("player_id"),
		PlayerKilled("player_killed"),
		CreatureKilled("creature_killed");
		
		TotalPVEKills(String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
	/**
	 * Represents the $prefix_total_pvp_kills table in the database.
	 * @author bitWolfy
	 *
	 */
	public enum TotalPVPKills {
		TableName("total_pvp_kills"),
		EntryId("total_pvp_id"),
		MaterialId("material_id"),
		MaterialData("material_data"),
		PlayerId("player_id"),
		VictimId("victim_id"),
		Times("times");
		
		TotalPVPKills(String columnName) { this.columnName = columnName; }
		
		private String columnName;
		
		@Override
		public String toString() { return columnName; }
	}
	
}
