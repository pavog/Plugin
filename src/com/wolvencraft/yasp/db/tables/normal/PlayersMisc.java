package com.wolvencraft.yasp.db.tables.normal;

public enum PlayersMisc implements _NormalTable {
	
	TableName("players_misc"),
	
	PlayerId("player_id"),
	Gamemode("gamemode"),
	ExperiencePercent("exp_perc"),
	ExperienceTotal("exp_total"),
	ExperienceLevel("exp_level"),
	ExpOrbsPickedUp("exp_picked_up"),
	FoodLevel("food_level"),
	HealthLevel("health"),
	FishCaught("fish_caught"),
	TimesKicked("times_kicked"),
	EggsThrown("eggs_thrown"),
	FoodEaten("food_eaten"),
	ArrowsShot("arrows_shot"),
	DamageTaken("damage_taken"),
	WordsSaid("words_said"),
	CommandsSent("commands_sent");
	
	PlayersMisc (String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}
