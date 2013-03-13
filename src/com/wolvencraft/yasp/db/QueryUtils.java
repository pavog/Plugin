package com.wolvencraft.yasp.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import com.wolvencraft.yasp.db.data.sync.Settings;
import com.wolvencraft.yasp.exceptions.DatabaseConnectionException;
import com.wolvencraft.yasp.util.Message;

/**
 * Database object wrapper; provides additional methods for simpler fetching and pushing data.<br />
 * All methods are applied to the currently running instance of the Database
 * @author bitWolfy
 *
 */
public class QueryUtils {
	
	/**
	 * Safely pushes data to the remote database. <br />
	 * Wraps around the corresponding Database method and handles any errors that might occur in it.
	 * @param sql SQL query
	 * @return <b>true</b> if the sync is successful, <b>false</b> otherwise
	 */
	private static boolean pushData(String sql) {
		try {
			if(Settings.getDebug()) Message.log(Level.FINEST, sql);
			return Database.getInstance().pushData(sql);
		} catch (DatabaseConnectionException ex) {
			if(Settings.getDebug()) Message.log(Level.SEVERE, ex.getMessage());
			return false;
		} catch (Exception e) {
			Message.log(Level.SEVERE, "An error occurred while pushing data to the remote database.");
			if(Settings.getDebug()) {
				e.printStackTrace();
				Message.log(Level.SEVERE, "End of error log");
			}
			return false;
		}
	}
	
	/**
	 * Safely returns the data from the remote server according to the SQL query.<br />
	 * Wraps around the corresponding Database method and handles any errors that might occur in it.
	 * @param sql SQL query
	 * @return Data from the remote database
	 */
	private static List<QueryResult> fetchData(String sql) {
		try {
			Message.log(Level.FINEST, sql);
			return Database.getInstance().fetchData(sql);
		} catch (DatabaseConnectionException ex) {
			Message.log(Level.SEVERE, ex.getMessage());
			return new ArrayList<QueryResult>();
		} catch (Exception e) {
			Message.log(Level.SEVERE, "An error occurred while pushing data to the remote database.");
			e.printStackTrace();
			Message.log(Level.SEVERE, "End of error log");
			return new ArrayList<QueryResult>();
		}
	}
	
	/**
	 * Builds and runs a SELECT query based on arguments provided
	 * @param table Database table to select from (without prefix)
	 * @param subject The columns that should be selected from the table
	 * @param condition Conditions that should apply to columns
	 * @return Data from the remote database
	 */
	public static List<QueryResult> select(String table, String[] subject, String[]... condition) {
		String query = "";
		table = "`" + Settings.getTablePrefix() + table + "`";
		
		String subjects = "";
		for(String str : subject) {
			if(!subjects.equals("")) subjects += ", ";
			if(str.equals("*")) subjects += "*";
			else subjects += "`" + str + "`";
		}
		
		String conditions = "";
		for(String[] str : condition) {
			if(!conditions.equals("")) conditions += " AND ";
			conditions += "`" + str[0] + "`='" + str[1] + "'";
		}
		query = "SELECT " + subjects + " FROM " + table + " WHERE " + conditions + ";";
		return fetchData(query);
	}
	
	/**
	 * Checks if the specified query returns any results
	 * @param table Database table to select from (without prefix)
	 * @param condition Conditions that should apply to columns
	 * @return <b>true</b> if the row exists, <b>false</b> otherwise
	 */
	public static boolean exists(String table, String[][] condition) {
		String query = "";
		table = "`" + Settings.getTablePrefix() + table + "`";
		
		String conditions = "";
		for(String[] str : condition) {
			if(!conditions.equals("")) conditions += " AND ";
			conditions += "`" + str[0] + "`='" + str[1] + "'";
		}
		query = "SELECT * FROM " + table + " WHERE " + conditions + ";";
		return !fetchData(query).isEmpty();
	}
	
	/**
	 * Builds and runs a SELECT query based on arguments provided.<br />
	 * <b>Example:</b> QueryUtils.select(Settings.TableName.toString(), new String[] {"key", "value"});<br />
	 * <b>Becomes:</b> SELECT `yasp_settings`.`key`, `yasp_settings`.`value` FROM `yasp_settings`
	 * @param table Database table to select from (without prefix)
	 * @param subject The columns that should be selected from the table
	 * @return Data from the remote database
	 */
	public static List<QueryResult> select(String table, String[] subject) {
		table = "`" + Settings.getTablePrefix() + table + "`";
		String subjects = "";
		for(String str : subject) {
			if(!subjects.equals("")) subjects += ", ";
			if(str.equals("*")) subjects += "*";
			else subjects += "`" + str + "`";
		}
		String query = "SELECT " + subjects + " FROM " + table + ";";
		return fetchData(query);
	}
	
	/**
	 * Builds and runs a SELECT query that returns the sum of the specified column.<br />
	 * Quite obviously, the column should only contain numeric values and should fit in a double
	 * @param table Database table to select from (without prefix)
	 * @param column Column to sum up
	 * @return Sum of the selected column
	 */
	public static double sum(String table, String column) {
		table = "`" + Settings.getTablePrefix() + table + "`";
		String query = "SELECT sum(`" + column + "`) as `temp` FROM " + table + ";";
		QueryResult result = fetchData(query).get(0);
		if(result.getValue("temp") == null) return 0;
		else return result.getValueAsInteger("temp");
	}
	
	/**
	 * Builds and runs a SELECT query that returns the sum of the specified column.<br />
	 * Quite obviously, the column should only contain numeric values and should fit in a double
	 * @param table Database table to select from (without prefix)
	 * @param column Column to sum up
	 * @param condition Conditions that should apply to columns
	 * @return Sum of the selected column
	 */
	public static double sum(String table, String column, String[]... condition) {
		table = "`" + Settings.getTablePrefix() + table + "`";
		String conditions = "";
		for(String[] str : condition) {
			if(!conditions.equals("")) conditions += " AND ";
			conditions += "`" + str[0] + "`='" + str[1] + "'";
		}
		String query = "SELECT sum(`" + column + "`) as `temp` FROM " + table + " WHERE " + conditions + ";";
		QueryResult result = fetchData(query).get(0);
		if(result.getValue("temp") == null) return 0;
		else return result.getValueAsInteger("temp");
	}
	
	/**
	 * Builds and runs an INSERT query based on arguments provided
	 * @param table Database table to insert into (without prefix)
	 * @param valueMap Map of column names and values that are to be inserted into the database
	 * @return <b>true</b> if the insertion was successful, <b>false</b> if an error occurred
	 */
	public static boolean insert(String table, Map<String, Object> valueMap) {
		String query = "";
		String fields = "";
		String values = "";
		Iterator<Entry<String, Object>> it = valueMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> pairs = (Entry<String, Object>) it.next();
			if(!fields.equals("")) fields += ", ";
			if(!values.equals("")) values += ", ";
			
			fields += "`" + pairs.getKey() + "`";
			values += "'" + pairs.getValue().toString() + "'";
			it.remove();
		}
		query = "INSERT INTO `" + Settings.getTablePrefix() + table + "` (" + fields + ")  VALUES (" + values + ");";
		return pushData(query);
	}
	
	/**
	 * Builds and runs an INSERT query based on arguments provided
	 * @param table Database table to insert into (without prefix)
	 * @param field Name of the target column
	 * @param value Value of the field
	 * @return <b>true</b> if the insertion was successful, <b>false</b> if an error occurred
	 */
	public static boolean insert(String table, String field, String value) {
		field = "`" + field + "`";
		value = "'" + value + "'";
		
		String query = "INSERT INTO `" + Settings.getTablePrefix() + table + "` (" + field + ")  VALUES (" + value + ");";
		return pushData(query);
	}
	
	/**
	 * 
	 * Builds and runs an UPDATE query based on arguments provided
	 * @param table Database table to update (without prefix)
	 * @param field Field to update
	 * @param value The new value of a field
	 * @param condition Conditions that should apply to columns
	 * @return <b>true</b> if the update was successful, <b>false</b> if an error occurred
	 */
	public static boolean update(String table, String field, String value, String[]... condition) {
//		if(!exists(table, condition)) return insert(table, field, value);
		
		String query = "";
		String conditions = "";
		for(String[] str : condition) {
			if(!conditions.equals("")) conditions += " AND ";
			conditions += "`" + str[0] + "`='" + str[1] + "'";
		}
		query = "UPDATE `" + Settings.getTablePrefix() + table + "` SET `" + field + "`='" + value + "' WHERE " + conditions + ";";
		return pushData(query);
	}
	
	/**
	 * Builds and runs an UPDATE query based on arguments provided
	 * @param table Database table to update (without prefix)
	 * @param valueMap  Map of column names and values that are to be updated in the database
	 * @param condition Conditions that should apply to columns
	 * @return <b>true</b> if the update was successful, <b>false</b> if an error occurred
	 */
	public static boolean update(String table, Map<String, Object> valueMap, String[]... condition) {
		if(!exists(table, condition)) return insert(table, valueMap);
		
		String query = "";
		String fieldValues = "";
		String conditions = "";
		Iterator<Entry<String, Object>> it = valueMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> pairs = (Entry<String, Object>) it.next();
			if(!fieldValues.equals("")) fieldValues += ", ";
			fieldValues += "`" + pairs.getKey() + "` = '" + pairs.getValue().toString() + "'";
			it.remove();
		}
		for(String str[] : condition) {
			if(!conditions.equals("")) conditions += " AND ";
			conditions += "`" + str[0] + "`='" + str[1] + "'";
		}
		query = "UPDATE `" + Settings.getTablePrefix() + table + "` SET " + fieldValues + " WHERE " + conditions + ";";
		return pushData(query);
	}
}
