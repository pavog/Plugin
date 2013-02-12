package com.wolvencraft.yasp.db;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
	public static void pushData(String sql) {
		Database.getInstance().pushData(sql);
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
	 * Fetches the version number from the database
	 * @return Version number, or <b>null</b> if it does not exist
	 */
	public static int getDatabaseVersion() {
		List<DBEntry> results = Database.getInstance().fetchData("SELECT dbVersion FROM config");
		if(results == null) return 0;
		return Integer.parseInt(results.get(0).getValue(""));
	}
	
	/**
	 * Confirms that the player is tracked (has an entry in the players table in the database)
	 * @param username Username of the checked player
	 * @return <b>true</b> if the user is tracked, <b>false</b> otherwise
	 */
	public static boolean isPlayerRegistered(String username) {
		List<DBEntry> results = Database.getInstance().fetchData("SELECT name FROM players WHERE name = '" + username + "'");
		if(results.isEmpty()) return false;
		return true;
	}
	
	/**
	 * Builds a SELECT query based on arguments provided
	 * @param table Database table to select from
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
		query = "SELECT " + subject + " FROM " + table + " WHERE " + conditions;
		return query;
	}
	
	/**
	 * Builds a SELECT query based on arguments provided
	 * @param table Database table to select from
	 * @param subject The columns that should be selected from the table
	 * @return <b>String</b> SELECT query
	 */
	public static String buildSelectQuery(String table, String subject) {
		return "SELECT " + subject + " FROM " + table;
	}
	
	/**
	 * Builds and runs a SELECT query based on arguments provided
	 * @param table Database table to select from
	 * @param subject The columns that should be selected from the table
	 * @param condition Conditions that should apply to columns
	 * @return Data from the remote database
	 */
	public static List<DBEntry> select(String table, String subject, String... condition) {
		return Database.getInstance().fetchData(buildSelectQuery(table, subject, condition));
	}
	
	/**
	 * Builds and runs a SELECT query based on arguments provided
	 * @param table Database table to select from
	 * @param subject The columns that should be selected from the table
	 * @return Data from the remote database
	 */
	public static List<DBEntry> select(String table, String subject) {
		return Database.getInstance().fetchData(buildSelectQuery(table, subject));
	}
	
	/**
	 * Builds an INSERT query based on arguments provided
	 * @param table Database table to insert into
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
		query = "INSERT INTO " + table + " (" + fields + ")  VALUES (" + values + ")";
		return query;
	}
	
	/**
	 * Builds and runs an INSERT query based on arguments provided
	 * @param table Database table to insert into
	 * @param valueMap Map of column names and values that are to be inserted into the database
	 * @return <b>true</b> if the insertion was successful, <b>false</b> if an error occurred
	 */
	public static boolean insert(String table, Map<String, Object> valueMap) {
		return Database.getInstance().pushData(buildInsertQuery(table, valueMap));
	}
	
	/**
	 * Builds an UPDATE query based on arguments provided
	 * @param table Database table to update
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
		query = "UPDATE " + table + " (" + fields + ")  SET (" + values + ") WHERE " + conditions;
		return query;
	}
	
	/**
	 * Builds and runs an UPDATE query based on arguments provided
	 * @param table Database table to update
	 * @param valueMap  Map of column names and values that are to be updated in the database
	 * @param condition Conditions that should apply to columns
	 * @return <b>true</b> if the update was successful, <b>false</b> if an error occurred
	 */
	public static boolean update(String table, Map<String, Object> valueMap, String... condition) {
		return Database.getInstance().pushData(buildUpdateQuery(table, valueMap, condition));
	}
}
