package com.wolvencraft.yasp.db.data.hooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

public class _HookFactory {
	
	public static List<PluginHookData> init(Player player, int playerId) {
		List<PluginHookData> stores = new ArrayList<PluginHookData>();
		stores.add((PluginHookData) VaultHook.getInstance().new VaultHookData(player, playerId));
		stores.add((PluginHookData) WorldGuardHook.getInstance().new WorldGuardHookData(player, playerId));
		return stores;
	}
	
	/**
	 * Common interface for plugin hooks
	 * @author bitWolfy
	 *
	 */
	public interface PluginHook {
		
		/**
		 * Patches the database to the latest version of the corresponding plugin
		 * @return <b>true</b> if the database was brought to date, <b>false</b> if an error occurred
		 */
		public boolean patch();
		
		/**
		 * Makes sure that there are no loose ends. Breaks the connection to the parent plugin if need be.
		 */
		public void cleanup();
	}
	
	/**
	 * Common interface for plugin hook data stores
	 * @author bitWolfy
	 *
	 */
	public interface PluginHookData {
		
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
		public boolean pushData();
		

		/**
		 * Returns the data values of the DataHolder in a Map form
		 * @param playerId Player ID
		 * @return <b>Map</b> of column names and their corresponding values
		 */
		public Map<String, Object> getValues();
	}
}
