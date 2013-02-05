package com.wolvencraft.yasp.Database.tables.normal;

import com.wolvencraft.yasp.Database.tables.DBTable;


public enum PlayersTable implements DBTable {
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
	public String getColumnName() { return columnName; }

}
