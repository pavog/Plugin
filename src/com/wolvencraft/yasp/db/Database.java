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

import com.wolvencraft.yasp.db.data.normal.Settings;
import com.wolvencraft.yasp.db.exceptions.DatabaseConnectionException;
import com.wolvencraft.yasp.db.exceptions.RuntimeSQLException;
import com.wolvencraft.yasp.util.Message;

/**
 * Represents a running database instance.<br />
 * There can only be one instance running at any given time.
 * @author bitWolfy
 *
 */
public class Database {
	
	private static Database instance = null;
	private Connection connection = null;

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
		patch();
		Settings.retrieveData();
	}
	
	public static boolean testConnection() {
		Connection testConnection = null;
		try {
			testConnection = DriverManager.getConnection(
				Settings.getConnectionPath(),
				Settings.getDatabaseUsername(),
				Settings.getDatabasePassword()
			);
			testConnection.close();
		} catch (Exception e) { return false; }
		return true;
	}
	
	/**
	 * Connects to the remote database according to the data stored in the configuration
	 * @throws DatabaseConnectionException Thrown if an error occurs while connecting to the database
	 */
	private void connect() throws DatabaseConnectionException {
		try {
			this.connection = DriverManager.getConnection(
				Settings.getConnectionPath(),
				Settings.getDatabaseUsername(),
				Settings.getDatabasePassword()
			);
		} catch (SQLException e) { throw new DatabaseConnectionException(e); }
	}
	
	/**
	 * Patches the remote database to the latest version
	 * @throws DatabaseConnectionException Thrown if the plugin is unable to patch the remote database
	 */
	private void patch() throws DatabaseConnectionException {
		Message.log("Attempting to patch the database. This will take a while.");
		int databaseVersion = Settings.getDatabaseVersion();
		do {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("SQLPatches/yasp_v" + (databaseVersion + 1) + ".sql");
			if (is == null) break;
			databaseVersion++;
			Message.log("Executing database patch v." + databaseVersion);
			ScriptRunner sr = new ScriptRunner(connection);
			try {sr.runScript(new InputStreamReader(is)); }
			catch (RuntimeSQLException e) { throw new DatabaseConnectionException("An error occured while patching the database to v." + databaseVersion, e); }
		} while (true);
		
		Settings.updateVersion(databaseVersion);
		Message.log("Target database is up to date.");
	}
	
	/**
	 * Attempts to reconnect to the remote server
	 * @return <b>true</b> if the connection was present, or reconnect is successful. <b>false</b> otherwise.
	 */
	private boolean reconnect() {
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
					if (Settings.getDebug()) e.printStackTrace();
					return false;
				}
			}
		} catch (SQLException e) {
			if (Settings.getDebug()) e.printStackTrace();
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
		} catch (SQLException e) {
			Message.log(Level.WARNING, "Failed to push data to the remote database");
			if(Settings.getDebug()) Message.log(Level.WARNING, sql);
			if (Settings.getDebug()) e.printStackTrace();
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
			statement = this.connection.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				HashMap<String, String> rowToAdd = new HashMap<String, String>();
				for (int x = 1; x <= rs.getMetaData().getColumnCount(); ++x) {
					rowToAdd.put(rs.getMetaData().getColumnName(x), rs.getString(x));
				}
				colData.add(new QueryResult(rowToAdd));
			}
		} catch (SQLException e) {
			Message.log(Level.WARNING, "Error retrieving data from the database");
			if(Settings.getDebug()) {
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
}
