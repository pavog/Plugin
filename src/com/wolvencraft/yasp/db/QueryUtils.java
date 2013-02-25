package com.wolvencraft.yasp.db;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.wolvencraft.yasp.StatsPlugin;

/**
 * Database object wrapper; provides additional methods for simpler fetching and pushing data.<br />
 * All methods are applied to the currently running instance of the Database
 * @author bitWolfy
 *
 */
public class QueryUtils {
	
	/**
	 * Pushes data to the remote database. <br />
	 * Wraps around the corresponding Database method.
	 * @param sql SQL query
	 * @return <b>true</b> if the sync is successful, <b>false</b> otherwise
	 */
	public static boolean pushData(String sql) {
		return Database.getInstance().pushData(sql);
	}
	
	/**
	 * Returns the data from the remote server according to the sql query.<br />
	 * Wraps around the corresponding Database method.
	 * @param sql SQL query
	 * @return Data from the remote database
	 */
	public static List<DBEntry> fetchData(String sql) {
		return Database.getInstance().fetchData(sql);
	}
	
	/**
	 * Builds a SELECT query based on arguments provided
	 * @param table Database table to select from (without prefix)
	 * @param subject The columns that should be selected from the table
	 * @param condition Conditions that should apply to columns
	 * @return <b>String</b> SELECT query
	 */
	public static String buildSelectQuery(String table, String subject, String... condition) {
		String query = "";
		String conditions = "";
		for(String str : condition) {
			if(!conditions.equals("")) conditions += " AND ";
			conditions += str;
		}
		query = "SELECT " + subject + " FROM " + StatsPlugin.getSettings().getTablePrefix() + table + " WHERE " + conditions;
		return query;
	}
	
	/**
	 * Builds a SELECT query based on arguments provided
	 * @param table Database table to select from (without prefix)
	 * @param subject The columns that should be selected from the table
	 * @return <b>String</b> SELECT query
	 */
	public static String buildSelectQuery(String table, String subject) {
		return "SELECT " + subject + " FROM " + StatsPlugin.getSettings().getTablePrefix() + table;
	}
	
	/**
	 * Builds and runs a SELECT query based on arguments provided
	 * @param table Database table to select from (without prefix)
	 * @param subject The columns that should be selected from the table
	 * @param condition Conditions that should apply to columns
	 * @return Data from the remote database
	 */
	public static List<DBEntry> select(String table, String subject, String... condition) {
		return Database.getInstance().fetchData(buildSelectQuery(table, subject, condition));
	}
	
	/**
	 * Builds and runs a SELECT query based on arguments provided
	 * @param table Database table to select from (without prefix)
	 * @param subject The columns that should be selected from the table
	 * @return Data from the remote database
	 */
	public static List<DBEntry> select(String table, String subject) {
		return Database.getInstance().fetchData(buildSelectQuery(table, subject));
	}
	
	/**
	 * Builds an INSERT query based on arguments provided
	 * @param table Database table to insert into (without prefix)
	 * @param valueMap Map of column names and values that are to be inserted into the database
	 * @return <b>String</b> INSERT query
	 */
	public static String buildInsertQuery(String table, Map<String, Object> valueMap) {
		String query = "";
		String fields = "";
		String values = "";
		Iterator<Entry<String, Object>> it = valueMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> pairs = (Entry<String, Object>) it.next();
			if(!fields.equals("")) fields += ", ";
			if(!values.equals("")) values += ", ";
			
			fields += pairs.getKey();
			values += pairs.getValue().toString();
			it.remove();
		}
		query = "INSERT INTO " + StatsPlugin.getSettings().getTablePrefix() + table + " (" + fields + ")  VALUES (" + values + ")";
		return query;
	}
	
	/**
	 * Builds and runs an INSERT query based on arguments provided
	 * @param table Database table to insert into (without prefix)
	 * @param valueMap Map of column names and values that are to be inserted into the database
	 * @return <b>true</b> if the insertion was successful, <b>false</b> if an error occurred
	 */
	public static boolean insert(String table, Map<String, Object> valueMap) {
		return Database.getInstance().pushData(buildInsertQuery(table, valueMap));
	}
	
	/**
	 * Builds an UPDATE query based on arguments provided
	 * @param table Database table to update (without prefix)
	 * @param valueMap  Map of column names and values that are to be updated in the database
	 * @param condition Conditions that should apply to columns
	 * @return <b>String</b> UPDATE query
	 */
	public static String buildUpdateQuery(String table, Map<String, Object> valueMap, String... condition) {
		String query = "";
		String fields = "";
		String values = "";
		String conditions = "";
		Iterator<Entry<String, Object>> it = valueMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> pairs = (Entry<String, Object>) it.next();
			if(!fields.equals("")) fields += ", ";
			if(!values.equals("")) values += ", ";
			
			fields += pairs.getKey();
			values += pairs.getValue().toString();
			it.remove();
		}
		for(String str : condition) {
			if(!conditions.equals("")) conditions += " AND ";
			conditions += str;
		}
		query = "UPDATE " + StatsPlugin.getSettings().getTablePrefix() + table + " (" + fields + ")  SET (" + values + ") WHERE " + conditions;
		return query;
	}
	
	/**
	 * Builds and runs an UPDATE query based on arguments provided
	 * @param table Database table to update (without prefix)
	 * @param valueMap  Map of column names and values that are to be updated in the database
	 * @param condition Conditions that should apply to columns
	 * @return <b>true</b> if the update was successful, <b>false</b> if an error occurred
	 */
	public static boolean update(String table, Map<String, Object> valueMap, String... condition) {
		return Database.getInstance().pushData(buildUpdateQuery(table, valueMap, condition));
	}
}
