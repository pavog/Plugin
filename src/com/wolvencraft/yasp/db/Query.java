/*
 * Query.java
 * 
 * Statistics
 * Copyright (C) 2013 bitWolfy <http://www.wolvencraft.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.wolvencraft.yasp.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import com.wolvencraft.yasp.Settings;
import com.wolvencraft.yasp.Settings.LocalConfiguration;
import com.wolvencraft.yasp.util.Message;

/**
 * Modular database query factory. Used to build and run SELECT, INSERT, and UPDATE queries.
 * @author bitWolfy
 *
 */
public class Query {
    
    private static Query instance;
    
    /**
     * Creates a new QueryFactory instance. Should be used once, on plugin startup
     */
    public Query() {
        instance = this;
    }
    
    /**
     * Returns a database query based on the table name provided
     * @param table Name of the table to query
     * @return Database query
     */
    public static DatabaseQuery table(String table) {
        return instance.new DatabaseQuery(table);
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
    
    
    /**
     * Represents a standard database query
     * @author bitWolfy
     *
     */
    public class DatabaseQuery {
        
        private DatabaseQuery instance;
        private String table;
        private List<String> columns;
        private Map<String, Object> values;
        private List<String> conditions;
        
        /**
         * <b>Default constructor</b><br />
         * Creates a new DatabaseQuery instance with the specified table name.<br />
         * While it is possible to create an instance of this class manually, it is recommended to use the table(String table) method in the Query class.
         * @param table
         */
        public DatabaseQuery(String table) {
            this.instance = this;
            this.table = table;
            this.columns = new ArrayList<String>();
            this.values = new HashMap<String, Object>();
            this.conditions = new ArrayList<String>();
        }
        
        /**
         * Defines which columns to return.<br />
         * If no columns are selected, returns everything
         * @param column Columns to include
         * @return Database query
         */
        public DatabaseQuery column(String... column) {
            for(String col : column) this.columns.add(col);
            return instance;
        }
        
        /**
         * Defines which columns to return.<br />
         * If no columns are selected, returns everything
         * @param column Columns to include
         * @return Database query
         */
        public DatabaseQuery columns(String[] column) {
            for(String col : column) this.columns.add(col);
            return instance;
        }
        
        /**
         * Applies a condition to the query
         * @param key Column name
         * @param value Column value
         * @return Database query
         */
        public DatabaseQuery condition(String key, String value) {
            this.conditions.add("`" + key + "`='" + value + "'");
            return instance;
        }

        /**
         * Applies a condition to the query
         * @param key Column name
         * @param value Column value
         * @return Database query
         */
        public DatabaseQuery condition(String key, Integer value) {
            this.conditions.add("`" + key + "`=" + value);
            return instance;
        }

        /**
         * Applies a condition to the query
         * @param key Column name
         * @param value Column value
         * @return Database query
         */
        public DatabaseQuery condition(String key, Double value) {
            this.conditions.add("`" + key + "`=" + value);
            return instance;
        }

        /**
         * Applies a condition to the query
         * @param key Column name
         * @param value Column value
         * @return Database query
         */
        public DatabaseQuery condition(String key, Long value) {
            this.conditions.add("`" + key + "`=" + value);
            return instance;
        }

        /**
         * Applies a condition to the query
         * @param key Column name
         * @param value Column value
         * @return Database query
         */
        public DatabaseQuery condition(String key, Boolean value) {
            if(value) this.conditions.add("`" + key + "`=1");
            else this.conditions.add("`" + key + "`=0");
            return instance;
        }

        /**
         * Applies a condition to the query
         * @param key Column name
         * @param value Column value
         * @return Database query
         */
        public DatabaseQuery condition(List<String> list) {
            this.conditions.addAll(list);
            return instance;
        }
        
        /**
         * Adds a value to be inserted into the database
         * @param key Column name
         * @param value Column value
         * @return Database query
         */
        public DatabaseQuery value(String key, Object value) {
            this.values.put(key, value);
            return instance;
        }
        
        /**
         * Adds a value to be inserted into the database
         * @param key Column name
         * @param value Column value
         * @return Database query
         */
        public DatabaseQuery value(String key, boolean value) {
            if(value) values.put(key, 1);
            else values.put(key, 0);
            return instance;
        }
        
        /**
         * Adds values to be inserted into the database
         * @param values Map of values to be added to the database
         * @return Database query
         */
        public DatabaseQuery value(Map<String, Object> values) {
            this.values.putAll(values);
            return instance;
        }

        /**
         * Bundles up the altered columns as an array
         * @return Array of columns
         */
        private String[] getColumnsFromValues() {
            List<String> columns = new ArrayList<String>();
            Iterator<Entry<String, Object>> it = instance.values.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> pairs = (Entry<String, Object>) it.next();
                columns.add(pairs.getKey());
            }
            String[] colArr = new String[columns.size()];
            colArr = columns.toArray(colArr);
            return colArr;
        }
        
        /**
         * Builds and runs the SELECT query that returns the first result found
         * @return <b>QueryResult</b> the first result found or <b>null</b> if there isn't one.
         */
        public QueryResult select() {
            return select(0);
        }
        
        /**
         * Builds and runs the SELECT query that returns the result with the specified index
         * @param index Index to turn to
         * @return <b>QueryResult</b> the result found or <b>null</b> if there isn't one.
         */
        public QueryResult select(int index) {
            String sql = "SELECT ";
            
            String columnString = "";
            if(instance.columns.isEmpty()) columnString = "*";
            else {
                for(String str : instance.columns) {
                    if(!columnString.equals("")) columnString += ", ";
                    columnString += "`" + str + "`";
                }
            }
            sql += columnString + " FROM `" + Settings.LocalConfiguration.DBPrefix.asString() + table + "`";
            
            String conditionString = "";
            for(String str : instance.conditions) {
                if(!conditionString.equals("")) conditionString += " AND ";
                conditionString += str;
            }
            if(!conditionString.equals("")) sql += " WHERE " + conditionString;
            
            try { return Query.fetchData(sql + ";").get(index); }
            catch (NullPointerException ex) { return null; }
            catch (IndexOutOfBoundsException aiex) { return null; }
        }
        
        /**
         * Builds and runs the SELECT query that returns a list of results from the database
         * @return List of results. Might be empty.
         */
        public List<QueryResult> selectAll() {
            String sql = "SELECT ";
            
            String columnString = "";
            if(instance.columns.isEmpty()) columnString = "*";
            else {
                for(String str : instance.columns) {
                    if(!columnString.equals("")) columnString += ", ";
                    columnString += "`" + str + "`";
                }
            }
            sql += columnString + " FROM `" + Settings.LocalConfiguration.DBPrefix.asString() + table + "`";
            
            String conditionString = "";
            for(String str : instance.conditions) {
                if(!conditionString.equals("")) conditionString += " AND ";
                conditionString += str;
            }
            if(!conditionString.equals("")) sql += " WHERE " + conditionString;
            
            return Query.fetchData(sql + ";");
        }
        
        /**
         * Checks if the query will yield any results
         * @return <b>true</b> if the query has any results, <b>false</b> if it is empty
         */
        public boolean exists() {
            return !selectAll().isEmpty();
        }
        
        /**
         * Calculates the sum of the rows in the specified column
         * @return <b>double</b> sum of rows in a specified column
         */
        public double sum() {
            String sql = "SELECT sum(";
            
            String columnString = "";
            if(instance.columns.isEmpty()) columnString = "*";
            else {
                for(String str : instance.columns) {
                    if(!columnString.equals("")) columnString += ", ";
                    else columnString += "`" + str + "`";
                }
            }
            sql += columnString + ") as `temp` FROM `" + Settings.LocalConfiguration.DBPrefix.asString() + table + "`";
            
            String conditionString = "";
            for(String str : instance.conditions) {
                if(!conditionString.equals("")) conditionString += " AND ";
                conditionString += str;
            }
            if(!conditionString.equals("")) sql += " WHERE " + conditionString;
            
            try { return Query.fetchData(sql + ";").get(0).getValueAsDouble("temp"); }
            catch (Exception e) { return 0; }
        }
        
        /**
         * Builds and runs the INSERT query
         * @return <b>true</b> if the value was successfully inserted, <b>false</b> if an error occurred
         */
        public boolean insert() {
            String sql = "INSERT INTO `" + Settings.LocalConfiguration.DBPrefix.asString() + table + "` (";
            
            String fieldString = "";
            String valueString = "";
            Iterator<Entry<String, Object>> it = instance.values.entrySet().iterator();
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
            for(String str : instance.conditions) {
                if(!conditionString.equals("")) conditionString += " AND ";
                conditionString += str;
            }
            if(!conditionString.equals("")) sql += " WHERE " + conditionString;
            
            return pushData(sql + ";");
        }
        
        /**
         * Builds and runs the UPDATE query
         * @return <b>true</b> if the value was successfully updated, <b>false</b> if an error occurred
         */
        public boolean update() {
            String sql = "UPDATE `" + Settings.LocalConfiguration.DBPrefix.asString() + table + "`";
            
            String valueString = "";
            Iterator<Entry<String, Object>> it = instance.values.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> pairs = (Entry<String, Object>) it.next();
                if(!valueString.equals("")) valueString += ", ";
                
                valueString += "`" + pairs.getKey().toString() + "`='" + pairs.getValue().toString() + "'";
                it.remove();
            }
            sql += " SET " + valueString;
            
            String conditionString = "";
            for(String str : instance.conditions) {
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
            if(!force) return instance.update();
            if(instance.columns(getColumnsFromValues()).exists()) return instance.update();
            else return instance.insert();
        }
        
        /**
         * Deletes a row from the database
         * @return <b>true</b> if the row was deleted, <b>false</b> if an error occurred
         */
        public boolean delete() {
            String sql = "DELETE FROM `" + Settings.LocalConfiguration.DBPrefix.asString() + table + "`";
            
            String conditionString = "";
            for(String str : instance.conditions) {
                if(!conditionString.equals("")) conditionString += " AND ";
                conditionString += str;
            }
            if(!conditionString.equals("")) sql += " WHERE " + conditionString;
            
            return pushData(sql + ";");
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
        
        public Object getRawValue(String column) {
            return fields.get(column);
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
