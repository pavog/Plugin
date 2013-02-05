package com.wolvencraft.yasp.Database.data.normal;

import java.util.List;

import com.wolvencraft.yasp.Database.DBEntry;
import com.wolvencraft.yasp.Database.Database;

public class Settings implements NormalData {
	DATABASE_VERSION("dbVersion", "10");

	private Settings(String key, String value) {
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
			Settings setting = Settings.getByKey(entry.getValue("key"));
			setting.value = entry.getValue("value");
		}
	}
	
	private static Settings getByKey(String key) {
		for(Settings entry : Settings.values()) {
			if(entry.getKey().equals(key)) return entry;
		}
		return null;
	}
	@Override
	public void fetchData() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void pushData() {
		// TODO Auto-generated method stub
		
	}
}
