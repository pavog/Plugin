package com.wolvencraft.yasp.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import com.wolvencraft.yasp.db.data.sync.Settings;
import com.wolvencraft.yasp.db.data.sync.Settings.LocalConfiguration;
import com.wolvencraft.yasp.util.Message;

public class QueryUtils {
	
	private static QueryUtils instance;
	
	public QueryUtils() {
		instance = this;
	}
	
	/**
	 * Safely casts a Map to QueryResult
	 * @param map Map to apply the cast to
	 * @return <b>QueryResult</b> desired result
	 */
	public static QueryResult toQueryResult(Map<String, String> map) {
		return instance.new QueryResult(map);
	}
	
	/**
	 * Builds a SELECT query for the table provided.
	 * @param table Table to send the query to
	 * @return Database query
	 */
	public static SelectQuery select(String table) {
		return instance.new SelectQuery(table);
	}
	
	/**
	 * Builds an INSERT query for the table provided.
	 * @param table Table to send the query to
	 * @return Database query
	 */
	public static InsertQuery insert(String table) {
		return instance.new InsertQuery(table);
	}
	
	/**
	 * Builds an UPDATE query for the table provided.
	 * @param table Table to send the query to
	 * @return Database query
	 */
	public static UpdateQuery update(String table) {
		return instance.new UpdateQuery(table);
	}
	
	/**
	 * Safely pushes data to the remote database. <br />
	 * Wraps around the corresponding Database method and handles any errors that might occur in it.
	 * @param sql SQL query
	 * @return <b>true</b> if the sync is successful, <b>false</b> otherwise
	 */
	private static boolean pushData(String sql) {
		try {
			Message.debug(Level.FINEST, sql);
			return Database.getInstance().pushData(sql);
		} catch (Exception e) {
			Message.log(Level.SEVERE, "An error occurred while pushing data to the remote database.");
			Message.log(Level.SEVERE, e.getMessage());
			if(LocalConfiguration.Debug.asBoolean()) e.printStackTrace();
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
			Message.debug(Level.FINEST, sql);
			return Database.getInstance().fetchData(sql);
		} catch (Exception e) {
			Message.log(Level.SEVERE, "An error occurred while pushing data to the remote database.");
			Message.log(Level.SEVERE, e.getMessage());
			if(LocalConfiguration.Debug.asBoolean()) e.printStackTrace();
			return new ArrayList<QueryResult>();
		}
	}
	
	private interface DBQuery {
		
		/**
		 * Applies a condition to the query
		 * @param key Column name
		 * @param value Column value
		 * @return Database query
		 */
		public DBQuery condition(String key, String value);
		
		/**
		 * Applies a condition to the query
		 * @param key Column name
		 * @param value Column value
		 * @return Database query
		 */
		public DBQuery condition(String key, Integer value);
		
		/**
		 * Applies a condition to the query
		 * @param key Column name
		 * @param value Column value
		 * @return Database query
		 */
		public DBQuery condition(String key, Double value);
		
		/**
		 * Applies a condition to the query
		 * @param key Column name
		 * @param value Column value
		 * @return Database query
		 */
		public DBQuery condition(String key, Long value);
		
		/**
		 * Applies a condition to the query
		 * @param key Column name
		 * @param value Column value
		 * @return Database query
		 */
		public DBQuery condition(String key, Boolean value);
		
		/**
		 * Imports a list of conditions to the query
		 * @param key Column name
		 * @param value Column value
		 * @return Database query
		 */
		public DBQuery condition(List<String> list);
	}
	
	
	
	/**
	 * Represents a SELECT query to the database.<br />
	 * Complimentary methods can be used to refine the results.
	 * @author bitWolfy
	 *
	 */
	public class SelectQuery implements DBQuery {
		public SelectQuery(String table) {
			this.table = Settings.LocalConfiguration.DBPrefix.asString() + table;
			columns = new ArrayList<String>();
			conditions = new ArrayList<String>();
		}
		
		String table;
		List<String> columns;
		List<String> conditions;
		
		/**
		 * Defines which columns to return.<br />
		 * If no columns are selected, returns everything
		 * @param column Columns to include
		 * @return Database query
		 */
		public SelectQuery column(String... column) {
			for(String col : column) columns.add(col);
			return this;
		}
		
		/**
		 * Defines which columns to return.<br />
		 * If no columns are selected, returns everything
		 * @param column Columns to include
		 * @return Database query
		 */
		public SelectQuery columns(String[] column) {
			for(String col : column) columns.add(col);
			return this;
		}
		
		@Override
		public SelectQuery condition(String key, String value) {
			conditions.add("`" + key + "`='" + value + "'");
			return this;
		}
		
		@Override
		public SelectQuery condition(String key, Integer value) {
			conditions.add("`" + key + "`=" + value);
			return this;
		}
		
		@Override
		public SelectQuery condition(String key, Double value) {
			conditions.add("`" + key + "`=" + value);
			return this;
		}
		
		@Override
		public SelectQuery condition(String key, Long value) {
			conditions.add("`" + key + "`=" + value);
			return this;
		}
		
		@Override
		public SelectQuery condition(String key, Boolean value) {
			if(value) conditions.add("`" + key + "`=1");
			else  conditions.add("`" + key + "`=0");
			return this;
		}
		
		@Override
		public SelectQuery condition(List<String> list) {
			conditions.addAll(list);
			return this;
		}
		
		/**
		 * Builds and runs the SELECT query
		 * @return List of results. Might be empty.
		 */
		public List<QueryResult> select() {
			String sql = "SELECT ";
			
			String columnString = "";
			if(columns.isEmpty()) columnString = "*";
			else {
				for(String str : columns) {
					if(!columnString.equals("")) columnString += ", ";
					else columnString += "`" + str + "`";
				}
			}
			sql += columnString + " FROM `" + Settings.LocalConfiguration.DBPrefix.asString() + table + "`";
			
			String conditionString = "";
			for(String str : conditions) {
				if(!conditionString.equals("")) conditionString += " AND ";
				conditionString += str;
			}
			if(!conditionString.equals("")) sql += " WHERE " + conditionString;
			
			return QueryUtils.fetchData(sql + ";");
		}
		
		/**
		 * Checks if the query will yield any results
		 * @return <b>true</b> if the query has any results, <b>false</b> if it is empty
		 */
		public boolean exists() {
			return !select().isEmpty();
		}
		
		/**
		 * Calculates the sum of the rows in the specified column
		 * @return <b>double</b> sum of rows in a specified column
		 */
		public double sum() {
			String sql = "SELECT sum(";
			
			String columnString = "";
			if(columns.isEmpty()) columnString = "*";
			else {
				for(String str : columns) {
					if(!columnString.equals("")) columnString += ", ";
					else columnString += "`" + str + "`";
				}
			}
			sql += columnString + ") as `temp` FROM `" + Settings.LocalConfiguration.DBPrefix.asString() + table + "`";
			
			String conditionString = "";
			for(String str : conditions) {
				if(!conditionString.equals("")) conditionString += " AND ";
				conditionString += str;
			}
			if(!conditionString.equals("")) sql += " WHERE " + conditionString;
			
			return QueryUtils.fetchData(sql + ";").get(0).getValueAsDouble("temp");
		}
	}
	
	
	
	/**
	 * Represents an INSERT query to the database.<br />
	 * Complimentary methods can be used to refine the results.
	 * @author bitWolfy
	 *
	 */
	public class InsertQuery implements DBQuery {
		
		public InsertQuery(String table) {
			this.table = table;
			values = new HashMap<String, Object>();
			conditions = new ArrayList<String>();
		}
		
		String table;
		Map<String, Object> values;
		List<String> conditions;
		
		/**
		 * Adds a value to be inserted into the database
		 * @param key Column name
		 * @param value Column value
		 * @return Database query
		 */
		public InsertQuery value(String key, Object value) {
			values.put(key, value);
			return this;
		}
		
		public InsertQuery value(String key, boolean value) {
			if(value) values.put(key, 1);
			else values.put(key, 0);
			return this;
		}
		
		/**
		 * Adds values to be inserted into the database
		 * @param values Map of values to be added to the database
		 * @return Database query
		 */
		public InsertQuery value(Map<String, Object> values) {
			this.values.putAll(values);
			return this;
		}
		
		@Override
		public InsertQuery condition(String key, String value) {
			conditions.add("`" + key + "`='" + value + "'");
			return this;
		}
		
		@Override
		public InsertQuery condition(String key, Integer value) {
			conditions.add("`" + key + "`=" + value);
			return this;
		}
		
		@Override
		public InsertQuery condition(String key, Double value) {
			conditions.add("`" + key + "`=" + value);
			return this;
		}
		
		@Override
		public InsertQuery condition(String key, Long value) {
			conditions.add("`" + key + "`=" + value);
			return this;
		}
		
		@Override
		public InsertQuery condition(String key, Boolean value) {
			if(value) conditions.add("`" + key + "`=1");
			else  conditions.add("`" + key + "`=0");
			return this;
		}
		
		@Override
		public InsertQuery condition(List<String> list) {
			conditions.addAll(list);
			return this;
		}
		
		/**
		 * Builds and runs the INSERT query
		 * @return <b>true</b> if the value was successfully inserted, <b>false</b> if an error occurred
		 */
		public boolean insert() {
			String sql = "INSERT INTO `" + Settings.LocalConfiguration.DBPrefix.asString() + table + "` (";
			
			String fieldString = "";
			String valueString = "";
			Iterator<Entry<String, Object>> it = values.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> pairs = (Entry<String, Object>) it.next();
				if(!fieldString.equals("")) fieldString += ", ";
				if(!valueString.equals("")) valueString += ", ";
				
				fieldString += "`" + pairs.getKey().toString() + "`";
				valueString += "'" + pairs.getValue().toString() + "'";
				it.remove();
			}
			sql += fieldString + ") VALUES (" + valueString + ")";
			
			String conditionString = "";
			for(String str : conditions) {
				if(!conditionString.equals("")) conditionString += " AND ";
				conditionString += str;
			}
			if(!conditionString.equals("")) sql += " WHERE " + conditionString;
			
			return pushData(sql + ";");
		}
		
	}
	
	
	
	/**
	 * Represents an UPDATE query to the database.<br />
	 * Complimentary methods can be used to refine the results.
	 * @author bitWolfy
	 *
	 */
	public class UpdateQuery implements DBQuery {
		
		public UpdateQuery(String table) {
			this.table = table;
			values = new HashMap<String, Object>();
			conditions = new ArrayList<String>();
		}
		
		String table;
		Map<String, Object> values;
		List<String> conditions;
		
		/**
		 * Adds a value to be inserted into the database
		 * @param key Column name
		 * @param value Column value
		 * @return Database query
		 */
		public UpdateQuery value(String key, Object value) {
			values.put(key, value);
			return this;
		}
		
		/**
		 * Adds values to be inserted into the database
		 * @param values Map of values to be added to the database
		 * @return Database query
		 */
		public UpdateQuery value(Map<String, Object> values) {
			this.values.putAll(values);
			return this;
		}
		
		/**
		 * Bundles up the altered columns as an array
		 * @return Array of columns
		 */
		private String[] columns() {
			List<String> columns = new ArrayList<String>();
			Iterator<Entry<String, Object>> it = values.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> pairs = (Entry<String, Object>) it.next();
				columns.add(pairs.getKey());
				it.remove();
			}
			String[] colArr = new String[columns.size()];
			colArr = columns.toArray(colArr);
			Message.debug(colArr.toString());
			return colArr;
		}
		
		@Override
		public UpdateQuery condition(String key, String value) {
			conditions.add("`" + key + "`='" + value + "'");
			return this;
		}
		
		@Override
		public UpdateQuery condition(String key, Integer value) {
			conditions.add("`" + key + "`=" + value);
			return this;
		}
		
		@Override
		public UpdateQuery condition(String key, Double value) {
			conditions.add("`" + key + "`=" + value);
			return this;
		}
		
		@Override
		public UpdateQuery condition(String key, Long value) {
			conditions.add("`" + key + "`=" + value);
			return this;
		}
		
		@Override
		public UpdateQuery condition(String key, Boolean value) {
			if(value) conditions.add("`" + key + "`=1");
			else  conditions.add("`" + key + "`=0");
			return this;
		}
		
		@Override
		public UpdateQuery condition(List<String> list) {
			conditions.addAll(list);
			return this;
		}
		
		/**
		 * Builds and runs the UPDATE query
		 * @return <b>true</b> if the value was successfully updated, <b>false</b> if an error occurred
		 */
		public boolean update() {
			String sql = "UPDATE `" + Settings.LocalConfiguration.DBPrefix.asString() + table + "` ";
			
			String valueString = "";
			Iterator<Entry<String, Object>> it = values.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> pairs = (Entry<String, Object>) it.next();
				if(!valueString.equals("")) valueString += ", ";
				
				valueString += "`" + pairs.getKey().toString() + "`='" + pairs.getValue().toString() + "'";
				it.remove();
			}
			sql += " SET " + valueString;
			
			String conditionString = "";
			for(String str : conditions) {
				if(!conditionString.equals("")) conditionString += " AND ";
				conditionString += str;
			}
			if(!conditionString.equals("")) sql += " WHERE " + conditionString;
			
			return pushData(sql + ";");
		}
		
		/**
		 * Checks if the row that is up for updating exists. If it does not, it is created.
		 * @return  <b>true</b> if the value was successfully updated, <b>false</b> if an error occurred
		 */
		public boolean update(boolean force) {
			if(!force) return update();
			if(select(table).columns(columns()).condition(conditions).exists()) return update();
			else return insert(table).value(values).insert();
		}
	}
	
	
	
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
		
		/**
		 * Returns the value of the specified column.
		 * @param column Column name
		 * @return <b>double</b> The value of the specified column, or <b>null</b> if there isn't one.
		 */
		public double getValueAsDouble(String column) {
			try { return Double.parseDouble(fields.get(column)); }
			catch (NumberFormatException e) { return -1; }
		}
	}
	
}
