package com.wolvencraft.yasp.db;

import java.util.Map;

/**
 * Represents the result of a SQL query to the database.<br />
 * This class wraps around a Map&lt;String, String&gt;, in which the key represents the column name,
 * and the value represents the value corresponding to the specified column.<br />
 * This class exists to prevent extremely confusing lists of maps, which can be quite a handful.
 * @author bitWolfy
 *
 */
public class QueryResult {
	private Map<String, String> fields;
	
	/**
	 * <b>Default constructor.</b><br />
	 * Creates a new QueryResult based on the specified column-value pairs
	 * @param fields Column-value pairs
	 */
	public QueryResult(Map<String, String> fields) {
		this.fields = fields;
	}
	
	/**
	 * Returns the value of the specified column.
	 * @param column Column name
	 * @return <b>String</b> The value of the specified column, or <b>null</b> if there isn't one.
	 */
	public String getValue(String column) {
		return fields.get(column);
	}
	
	
	/**
	 * Returns the value of the specified column.
	 * @param column Column name
	 * @return <b>boolean</b> The value of the specified column, or <b>null</b> if there isn't one.
	 */
	public boolean getValueAsBoolean(String column) {
		return fields.get(column).equalsIgnoreCase("1");
	}
	
	
	/**
	 * Returns the value of the specified column.
	 * @param column Column name
	 * @return <b>int</b> The value of the specified column, or <b>null</b> if there isn't one.
	 */
	public int getValueAsInteger(String column) {
		try { return Integer.parseInt(fields.get(column)); }
		catch (NumberFormatException e) { return -1; }
	}
	
	/**
	 * Returns the value of the specified column.
	 * @param column Column name
	 * @return <b>long</b> The value of the specified column, or <b>null</b> if there isn't one.
	 */
	public long getValueAsLong(String column) {
		try { return Long.parseLong(fields.get(column)); }
		catch (NumberFormatException e) { return -1; }
	}
}
