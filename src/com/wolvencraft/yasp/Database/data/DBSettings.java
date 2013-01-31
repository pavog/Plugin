package com.wolvencraft.yasp.Database.data;

import java.util.List;

import com.wolvencraft.yasp.Database.Database;

/**
 * Represents the database's <b>config</b> table
 * @author bitWolfy
 *
 */
public enum DBSettings {
	DATABASE_VERSION("dbVersion", "10");

	private DBSettings(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	private final String key;
	private String value;

	public String getKey() { return key; }
	public String getValue() { return value; }

	public boolean getValueAsBoolean() {
		return value.equalsIgnoreCase("y");
	}

	public int getValueAsInteger() {
		try { return Integer.parseInt(value); }
		catch (NumberFormatException e) { return -1; }
	}
	
	public static void refresh() {
		List<DBEntry> results = Database.getInstance().fetchData("SELECT * FROM config");
		for(DBEntry entry : results) {
			DBSettings setting = DBSettings.getByKey(entry.getValue("key"));
			setting.value = entry.getValue("value");
		}
	}
	
	private static DBSettings getByKey(String key) {
		for(DBSettings entry : DBSettings.values()) {
			if(entry.getKey().equals(key)) return entry;
		}
		return null;
	}
}
