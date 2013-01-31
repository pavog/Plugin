package com.wolvencraft.yasp.Database.data;

import java.util.Map;

public class DBEntry {
	private Map<String, String> fields;
	
	public DBEntry(Map<String, String> fields) {
		this.fields = fields;
	}
	
	public String getValue(String column) {
		return fields.get(column);
	}
}
