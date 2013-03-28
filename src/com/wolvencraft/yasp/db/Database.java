/*
 * Database.java
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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import com.wolvencraft.yasp.Settings;
import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.Settings.LocalConfiguration;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.exceptions.DatabaseConnectionException;
import com.wolvencraft.yasp.exceptions.RuntimeSQLException;
import com.wolvencraft.yasp.util.Message;

/**
 * Represents a running database instance.<br />
 * There can only be one instance running at any given time.
 * @author bitWolfy
 *
 */
public class Database {
    
    private static Database instance = null;
    private static Connection connection = null;
    
    /**
     * Default constructor. Connects to the remote database, performs patches if necessary, and holds to the DB info.<br />
     * @throws DatabaseConnectionException Thrown if the plugin could not connect to the database
     */
    public Database() throws DatabaseConnectionException {
        if (instance != null) throw new DatabaseConnectionException("Attempted to establish a duplicate connection with a remote database");
        
        try { Class.forName("com.mysql.jdbc.Driver"); }
        catch (ClassNotFoundException ex) { throw new DatabaseConnectionException("MySQL driver was not found!"); }
        
        instance = this;
        
        connect();
        
        try { if (connection.getAutoCommit()) connection.setAutoCommit(false); }
        catch (Throwable t) { throw new RuntimeSQLException("Could not set AutoCommit to false. Cause: " + t, t); }
        
        runPatch(false);
    }
    
    /**
     * Connects to the remote database according to the data stored in the configuration
     * @throws DatabaseConnectionException Thrown if an error occurs while connecting to the database
     */
    private void connect() throws DatabaseConnectionException {
        try {
            connection = DriverManager.getConnection(
                LocalConfiguration.DBConnect.asString(),
                LocalConfiguration.DBUser.asString(),
                LocalConfiguration.DBPass.asString()
            );
        } catch (SQLException e) { throw new DatabaseConnectionException(e); }
    }
    
    /**
     * Patches the remote database to the latest version.<br />
     * This method will run in the <b>main server thread</b> and therefore will freeze the server until the patch is complete.
     * @throws DatabaseConnectionException Thrown if the plugin is unable to patch the remote database
     */
    public void runPatch(boolean force) throws DatabaseConnectionException {
        int currentVersion = 0, latestVersion = 0;
        if(!force) { currentVersion = latestVersion = Settings.RemoteConfiguration.DatabaseVersion.asInteger(); }
        List<String> patches = new ArrayList<String>();
        do {
            String patch = (latestVersion + 1) + "";
            if(this.getClass().getClassLoader().getResourceAsStream("SQLPatches/yasp_v" + patch + ".sql") == null) break;
            patches.add(patch);
            latestVersion++;
        } while (true);
        
        Message.debug("Current version: " + currentVersion + ", latest version: " + latestVersion);
        
        if(currentVersion >= latestVersion) {
            Message.log("Target database is up to date");
            Statistics.setPaused(false);
            return;
        }

        ScriptRunner sr = new ScriptRunner(connection);
        Message.log("+-------] Database Patcher [-------+");
        for(String patch : patches) {
            Message.log("|       Applying patch " + patch + " / " + patches.size() + "       |");
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("SQLPatches/yasp_v" + patch + ".sql");
            try {sr.runScript(new InputStreamReader(is)); }
            catch (RuntimeSQLException e) { throw new DatabaseConnectionException("An error occured while patching the database to v." + patch, e); }
            
            Settings.RemoteConfiguration.DatabaseVersion.update(patch);
        }
        Message.log("+----------------------------------+");
        
        Statistics.setPaused(false);
    }
    
    /**
     * Applies a custom patch to the remote database
     * @param patchId Unique ID of the desired patch
     * @throws DatabaseConnectionException Thrown if the plugin is unable to patch the remote database
     */
    public void runCustomPatch(String patchId) throws DatabaseConnectionException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("SQLPatches/" + patchId + ".sql");
        if (is == null) return;
        Message.log("Executing database patch: " + patchId + ".sql");
        ScriptRunner sr = new ScriptRunner(connection);
        try {sr.runScript(new InputStreamReader(is)); }
        catch (RuntimeSQLException e) { throw new DatabaseConnectionException("An error occured while executing database patch: " + patchId + ".sql", e); }
    }
    
    /**
     * Attempts to reconnect to the remote server
     * @return <b>true</b> if the connection was present, or reconnect is successful. <b>false</b> otherwise.
     */
    public boolean reconnect() {
        try {
            if (connection.isValid(10)) {
                Message.log("Connection is still present. Malformed query detected.");
                return true;
            }
            else {
                Message.log(Level.WARNING, "Attempting to re-connect to the database");
                try {
                    connect();
                    Message.log("Connection re-established. No data is lost.");
                    return true;
                } catch (DatabaseConnectionException e) {
                    Message.log(Level.SEVERE, "Failed to re-connect to the database. Data is being stored locally.");
                    if (LocalConfiguration.Debug.asBoolean()) e.printStackTrace();
                    return false;
                }
            }
        } catch (SQLException e) {
            if (LocalConfiguration.Debug.asBoolean()) e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Pushes data to the remote database.<br />
     * This is a raw method and should never be used by itself. Use the <b>QueryUtils</b> wrapper for more options 
     * and proper error handling. This method is not to be used for regular commits to the database.
     * @param sql SQL query
     * @return <b>true</b> if the sync is successful, <b>false</b> otherwise
     */
    public boolean pushData(String sql) {
        int rowsChanged = 0;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            rowsChanged = statement.executeUpdate(sql);
            statement.close();
            connection.commit();
        } catch (SQLException e) {
            Message.log(Level.WARNING, "Failed to push data to the remote database");
            if(LocalConfiguration.Debug.asBoolean()) {
                Message.log(Level.WARNING, sql);
                e.printStackTrace();
            }
            return reconnect();
        } finally {
            if (statement != null) {
                try { statement.close(); }
                catch (SQLException e) { Message.log(Level.SEVERE, "Error closing database connection"); }
            }
        }
        return rowsChanged > 0;
    }
    
    /**
     * Returns the data from the remote server according to the SQL query.<br />
     * This is a raw method and should never be used by itself. Use the <b>QueryUtils</b> wrapper for more options 
     * and proper error handling. This method is not to be used for regular commits to the database.
     * @param sql SQL query
     * @return Data from the remote database
     */
    public List<QueryResult> fetchData(String sql) {
        List<QueryResult> colData = new ArrayList<QueryResult>();

        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                HashMap<String, String> rowToAdd = new HashMap<String, String>();
                for (int x = 1; x <= rs.getMetaData().getColumnCount(); ++x) {
                    rowToAdd.put(rs.getMetaData().getColumnName(x), rs.getString(x));
                }
                colData.add(Query.toQueryResult(rowToAdd));
            }
        } catch (SQLException e) {
            Message.log(Level.WARNING, "Error retrieving data from the database");
            if(LocalConfiguration.Debug.asBoolean()) {
                Message.log(Level.WARNING, e.getMessage());
                Message.log(Level.WARNING, sql);
            }
            reconnect();
            return new ArrayList<QueryResult>();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {}
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) { Message.log(Level.SEVERE, "Error closing database connection"); }
            }
        }
        return colData;
    }
    
    /**
     * Returns the current running instance of the database
     * @return Database instance
     * @throws DatabaseConnectionException Thrown if there is no active connection to the database
     */
    public static Database getInstance() throws DatabaseConnectionException {
        if(instance == null) throw new DatabaseConnectionException("Could not find an active connection to the database");
        return instance;
    }
    
    /**
     * Returns the current running instance of the database
     * @param silent If <b>true</b>, will not check if a database instance exists.
     * @return Database instance
     * @throws DatabaseConnectionException Thrown if there is no active connection to the database
     */
    public static Database getInstance(boolean silent) throws DatabaseConnectionException {
        if(!silent && instance == null) throw new DatabaseConnectionException("Could not find an active connection to the database");
        return instance;
    }
    
    /**
     * Cleans up the leftover database and connection instances to prevent memory leaks.
     */
    public static void cleanup() {
        connection = null;
        instance = null;
    }
}
