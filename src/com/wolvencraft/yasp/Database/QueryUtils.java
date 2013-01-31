package com.wolvencraft.yasp.Database;

import java.util.List;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.Database.data.DBEntry;
import com.wolvencraft.yasp.Stats.KillTag;
import com.wolvencraft.yasp.Utils.DBProcedure;

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
	public static boolean isPlayerRegistered(String UUID) {
		List<DBEntry> results = Database.getInstance().fetchData("SELECT uuid FROM players WHERE uuid = '" + UUID + "'");
		if(results.isEmpty()) return false;
		return true;
	}
	
	/**
	 * Calls on a stored procedure in the database to signify that the plugin is starting up.
	 * @return <b>true</b> if called successfully, <b>false</b> otherwise
	 */
	public static boolean pluginStartup() {
		return Database.getInstance().callStoredProcedure(DBProcedure.PLUGIN_STARTUP);
	}
	
	/**
	 * Calls on a stored procedure in the database to signify that the plugin is shutting up.
	 * @return <b>true</b> if called successfully, <b>false</b> otherwise
	 */
	public static boolean pluginShutdown() {
		return StatsPlugin.getInstance().getDB().callStoredProcedure(DBProcedure.PLUGIN_SHUTDOWN);
	}
	
	/**
	 * Calls on a stored procedure in the database to register a new player joining the server
	 * @param UUID UUID of the player in question
	 * @param playerName Username of the player to track
	 * @return <b>true</b> if the data is stored, <b>false</b> if an error occurs
	 */
	public static boolean playerFirstJoin(String UUID, String playerName) {
		return StatsPlugin.getInstance().getDB().callStoredProcedure(DBProcedure.PLAYER_JOIN, UUID, playerName);
	}
	
	/**
	 * Calls on a stored procedure in the database to register player logging in.<br />
	 * This method is used to register players that are already tracked. Use <b>playerFirstJoin()</b> for new players
	 * @param UUID UUID of the player in question
	 * @return <b>true</b> if the data is stored, <b>false</b> if an error occurs
	 */
	public static boolean playerLogin(String UUID) {
		return StatsPlugin.getInstance().getDB().callStoredProcedure(DBProcedure.PLAYER_LOGIN, UUID);
	}
	
	/**
	 * Calls on a stored procedure in the database to register player logging off
	 * @param UUID UUID of the player in question
	 * @return <b>true</b> if the data is stored, <b>false</b> if an error occurs
	 */
	public static boolean playerLogout(String UUID) {
		return StatsPlugin.getInstance().getDB().callStoredProcedure(DBProcedure.PLAYER_LOGOUT, UUID);
	}
	
	/**
	 * Calls on a stored procedure in the database to increment the number of blocks placed by the player
	 * @param UUID UUID of the player in question
	 * @param id ID of blocks placed
	 * @param number Number of blocks placed
	 * @return <b>true</b> if the data is stored, <b>false</b> if an error occurs
	 */
	public static boolean blockPlaced(String UUID, Integer id, Integer number) {
		return Database.getInstance().callStoredProcedure(DBProcedure.INCREMENT_BLOCK_PLACED, UUID, id.toString(), number.toString());
	}
	
	/**
	 * Calls on a stored procedure in the database to increment the number of blocks broken by the player
	 * @param UUID UUID of the player in question
	 * @param id ID of blocks broken
	 * @param number Number of blocks broken
	 * @return <b>true</b> if the data is stored, <b>false</b> if an error occurs
	 */
	public static boolean blockBroken(String UUID, Integer id, Integer number) {
		return Database.getInstance().callStoredProcedure(DBProcedure.INCREMENT_BLOCK_DESTROY, UUID, id.toString(), number.toString());
	}

	/**
	 * Calls on a stored procedure in the database to increment the number of blocks picked up by the player
	 * @param UUID UUID of the player in question
	 * @param id ID of items picked up
	 * @param number Number of items picked up
	 * @return <b>true</b> if the data is stored, <b>false</b> if an error occurs
	 */
	public static boolean itemPickedUp(String UUID, Integer id, Integer number) {
		return Database.getInstance().callStoredProcedure(DBProcedure.INCREMENT_ITEM_PICKEDUP, UUID, id.toString(), number.toString());
	}
	
	/**
	 * Calls on a stored procedure in the database to increment the number of blocks dropped by the player
	 * @param UUID UUID of the player in question
	 * @param id ID of the item dropped
	 * @param number Number of items dropped
	 * @return <b>true</b> if the data is stored, <b>false</b> if an error occurs
	 */
	public static boolean itemDropped(String UUID, Integer id, Integer number) {
		return Database.getInstance().callStoredProcedure(DBProcedure.INCREMENT_ITEM_DROPPED, UUID, id.toString(), number.toString());
	}
	
	/**
	 * Calls on a stored procedure in the database to register a new kill in the database
	 * @param kt KillTag that describes the killing
	 * @return <b>true</b> if the data is stored, <b>false</b> if an error occurs
	 */
	public static boolean newKill(KillTag kt) {
		return Database.getInstance().callStoredProcedure(DBProcedure.KILL, kt.Killed.getID().toString(),
				kt.KilledBy.getID().toString(),
				kt.KillType.getID().toString(),
				kt.KilledUsing.toString(),
				kt.KillProjectile.getID().toString(),
				kt.KilledBy_UUID,
				kt.Killed_UUID);
	}
	
	/**
	 * Calls on a stored procedure in the database to update the most players online count
	 * @return <b>true</b> if the data is stored, <b>false</b> if an error occurs
	 */
	public static boolean updateMaxOnlineCount() {
		return Database.getInstance().callStoredProcedure(DBProcedure.UPDATE_MOST_EVER_ONLINE);
	}
}
