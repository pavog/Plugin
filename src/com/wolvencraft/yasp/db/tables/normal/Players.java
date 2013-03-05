package com.wolvencraft.yasp.db.tables.normal;

public enum Players implements _NormalTable {
	
	TableName("players"),
	
	PlayerId("player_id"),
	Name("name"),
	Online("online"),
	SessionStart("login_time"),
	FirstLogin("first_login"),
	Logins("logins");
	
	Players(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }

}
