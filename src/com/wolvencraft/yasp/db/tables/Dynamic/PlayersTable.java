package com.wolvencraft.yasp.db.tables.Dynamic;



public enum PlayersTable implements DynamicTable {
	
	TableName("players"),
	PlayerId("player_id"),
	Name("name"),
	Online("online"),
	ExperiencePercent("exp_perc"),
	ExperienceTotal("exp_total"),
	ExperienceLevel("exp_level"),
	FoodLevel("food_level"),
	HealthLevel("health_level"),
	FirstLogin("first_login"),
	Logins("logins"),
	Deaths("deaths");
	
	PlayersTable(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }

}
