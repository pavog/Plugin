package com.wolvencraft.yasp.hooks;

import java.util.Map;

import org.bukkit.entity.Player;

public interface _PluginHook {
	
	/**
	 * Patches the database to the latest version of the corresponding plugin
	 * @return <b>true</b> if the database was brought to date, <b>false</b> if an error occurred
	 */
	public boolean patch();
	
	/**
	 * Makes sure that there are no loose ends. Breaks the connection to the parent plugin if need be.
	 */
	public void cleanup();
	
	public interface PluginHookEntry {
		
		/**
		 * Fetches the data from the remote database.<br />
		 * Might not actually do anything if the plugin sends data in a log format.
		 * @param player Player object
		 */
		public void fetchData(Player player);
		
		/**
		 * Pushes the data to the remote database.
		 * @param playerId Player ID
		 * @return <b>true</b> if the data was sent successfully, <b>false</b> if an error occurred
		 */
		public boolean pushData(int playerId);
		

		/**
		 * Returns the data values of the DataHolder in a Map form
		 * @param playerId Player ID
		 * @return <b>Map</b> of column names and their corresponding values
		 */
		public Map<String, Object> getValues(int playerId);
	}
}
