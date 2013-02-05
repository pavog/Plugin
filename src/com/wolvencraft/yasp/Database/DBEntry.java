package com.wolvencraft.yasp.Database;

import java.util.Map;

public class DBEntry {
	private Map<String, String> fields;
	
	public DBEntry(Map<String, String> fields) {
		this.fields = fields;
	}
	
	public String getValue(String column) {
		return fields.get(column);
	}

	public boolean getValueAsBoolean(String column) {
		return fields.get(column).equalsIgnoreCase("y");
	}

	public int getValueAsInteger(String column) {
		try { return Integer.parseInt(fields.get(column)); }
		catch (NumberFormatException e) { return -1; }
	}
}
