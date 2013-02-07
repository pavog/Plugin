package com.wolvencraft.yasp.db;

import java.util.List;

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
	 * @param UUID UUID of the player
	 * @return <b>true</b> if the user is tracked, <b>false</b> otherwise
	 */
	public static boolean isPlayerRegistered(String username) {
		List<DBEntry> results = Database.getInstance().fetchData("SELECT name FROM players WHERE name = '" + username + "'");
		if(results.isEmpty()) return false;
		return true;
	}
	
	/**
	 * Calls on a stored procedure in the database to signify that the plugin is starting up.
	 * @deprecated
	 * @return <b>true</b> if called successfully, <b>false</b> otherwise
	 */
	public static boolean pluginStartup() {
		return Database.getInstance().callStoredProcedure(DBProcedure.PLUGIN_STARTUP);
	}
	
	/**
	 * Calls on a stored procedure in the database to signify that the plugin is shutting up.
	 * @deprecated
	 * @return <b>true</b> if called successfully, <b>false</b> otherwise
	 */
	public static boolean pluginShutdown() {
		return Database.getInstance().callStoredProcedure(DBProcedure.PLUGIN_SHUTDOWN);
	}
}
