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

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.data.Dynamic.Settings;
import com.wolvencraft.yasp.db.exceptions.DatabaseConnectionException;
import com.wolvencraft.yasp.db.exceptions.RuntimeSQLException;
import com.wolvencraft.yasp.util.Message;

public class Database {
	private static Database instance = null;

	private Connection connection = null;

	/**
	 * Default constructor. Connects to the remote database, performs patches if necessary, and holds to the DB info.<br />
	 * @throws ClassNotFoundException Thrown if mysql.jdbc.Driver is not available
	 * @throws DatabaseConnectionException Thrown if the plugin could not connect to the database
	 */
	public Database() throws ClassNotFoundException, DatabaseConnectionException {
		if (instance != null) {
			Message.log(Level.SEVERE, "Attempted to establish a duplicate connection with a remote database");
			return;
		}
		
		Class.forName("com.mysql.jdbc.Driver");
		connectToDB();
		patchDB();

		instance = this;
	}
	
	/**
	 * Connects to the remote database according to the data stored in the configuration
	 * @throws DatabaseConnectionException Thrown if an error occurs while connecting to the database
	 */
	private void connectToDB() throws DatabaseConnectionException {
		try {
			Settings settings = StatsPlugin.getSettings();
			this.connection = DriverManager.getConnection(settings.getConnectionPath(), settings.getDatabaseUsername(), settings.getDatabasePassword());
		} catch (SQLException e) { throw new DatabaseConnectionException(e); }
	}
	
	/**
	 * Patches the remote database to the latest version
	 * @throws DatabaseConnectionException Thrown if the plugin is unable to patch the remote database
	 */
	private void patchDB() throws DatabaseConnectionException {
		Settings settings = StatsPlugin.getSettings();
		int remoteVersion = settings.getRemoteVersion();
		int currentVersion = settings.getLatestVersion();
		if(remoteVersion < currentVersion) {
			Message.log("Target database is outdated. Patching database: v." + currentVersion + " => v." + remoteVersion);
			
			while(remoteVersion < currentVersion) {
				remoteVersion++;
				InputStream is = this.getClass().getClassLoader().getResourceAsStream("SQLPatches/stats_v" + remoteVersion + ".sql");
				if (is == null) throw new DatabaseConnectionException("Unable to patch the database to v." + remoteVersion);
				ScriptRunner sr = new ScriptRunner(connection);
				try {sr.runScript(new InputStreamReader(is)); }
				catch (RuntimeSQLException e) { throw new DatabaseConnectionException("An error occured while patching the database to v." + remoteVersion, e); }
			}
		} else {
			Message.log("Target database is up to date.");
		}
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
					connectToDB();
					Message.log("Connection re-established. No data is lost.");
					return true;
				} catch (DatabaseConnectionException e) {
					Message.log(Level.SEVERE, "Failed to re-connect to the database. Data is being stored locally.");
					if (StatsPlugin.getSettings().getDebug()) e.printStackTrace();
					return false;
				}
			}
		} catch (SQLException e) {
			if (StatsPlugin.getSettings().getDebug()) e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Pushes data to the remote database
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
			Message.log(Level.WARNING, sql + " Failed to push data to the remote database. Checking connection . . .");
			if (StatsPlugin.getSettings().getDebug()) e.printStackTrace();
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
	 * Returns the data from the remote server according to the sql query
	 * @param sql SQL query
	 * @return Data from the remote database
	 */
	public List<DBEntry> fetchData(String sql) {
		List<DBEntry> colData = new ArrayList<DBEntry>();

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
				colData.add(new DBEntry(rowToAdd));
			}
		} catch (SQLException e) {
			Message.log(Level.WARNING, sql + " :: Query failed, checking connection... (" + e.getMessage() + ")");
			if (StatsPlugin.getSettings().getDebug()) e.printStackTrace();
			reconnect();
			return null;
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
	 * Calls a pre-defined procedured with custom variables
	 * @deprecated
	 * @param procName Procedure to call
	 * @param variables Variables
	 * @return <b>true</b> if the procedure was successfully called, <b>false</b> otherwise
	 */
	public boolean callStoredProcedure(DBProcedure procedure, String... variables) {
		StringBuilder sb = new StringBuilder("CALL `" + StatsPlugin.getSettings().getDatabaseName() + "`." + procedure.getName() + "(");
		if (variables != null && variables.length != 0) {
			for (String variable : variables) { sb.append("'" + variable + "',"); }
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(");");

		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sb.toString());
		} catch (SQLException e) {
			Message.log(Level.WARNING, sb.toString() + " :: Stored procedure failed, checking connection... (" + e.getMessage() + ")");
			if (StatsPlugin.getSettings().getDebug()) e.printStackTrace();
			return reconnect();
		} finally {
			if (statement != null) {
				try { statement.close(); }
				catch (SQLException e) { Message.log(Level.SEVERE, "Error closing database connection"); }
			}
		}

		return true;
	}
	
	/**
	 * Returns the current running instance of the database
	 * @return Database instance
	 */
	public static Database getInstance() { return instance; }
}
