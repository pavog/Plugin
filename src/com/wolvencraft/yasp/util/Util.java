package com.wolvencraft.yasp.util;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.DataCollector;

public class Util {
 	
	/**
	 * Replaces variables in the string with corresponding values
	 * @param str String to parse
	 * @return Resulting string
	 */
	public static String parseVars(String str) {
		Map<String, Object> values = DataCollector.serverTotals().getValues();
		Iterator<Entry<String, Object>> it = values.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
	        str = str.replace("<" + pairs.getKey() + ">", pairs.getValue() + "");
	        it.remove();
	    }
		return str;
	}
	
	/**
	 * Replaces the color codes with colors
	 * @param msg String to be parsed
	 * @return Parsed string
	 */
	public static String parseChatColors(String str) {
		if(str == null) return "";
		
		for(ChatColor color : ChatColor.values()) {
			str = str.replaceAll("&" + color.getChar(), color + "");
		}
		
		return str;
	}
	
	/**
	 * Returns the current time in Unix timestamp form
	 * @return Current time
	 */
	public static long getTimestamp() {
		Timestamp timestamp = new Timestamp(new java.util.Date().getTime());
		return timestamp.getTime() / 1000;
	}
	
	/**
	 * Checks if the player is exempt from the statistics
	 * @param player Player in question
	 * @return <b>true</b> if the player's stats should not be registered, <b>false</b> otherwise
	 */
	public static boolean isExempt(Player player) {
		return
			player.hasPermission("stats.exempt") ||
			player.hasPermission("statistician.ignore");
	}
	
	/**
	 * Checks if the player is exempt from the statistics
	 * @param player Player in question
	 * @param statsType Permission modifier
	 * @return <b>true</b> if the player's stats should not be registered, <b>false</b> otherwise
	 */
	public static boolean isExempt(Player player, String statsType) {
		return
			player.hasPermission("stats.exempt") ||
			player.hasPermission("statistician.ignore") || 
			player.hasPermission("stats.exempt." + statsType);
	}
	
	/**
	 * Creates an array of pages with player's statistical information
	 * @param player
	 * @return
	 */
	public static String[] getBookPages(Player player) {
		Map<String, Object> stats = DataCollector.get(player).playerTotals().getValues();
		return new String[] {
				ChatColor.BOLD + " + " + player.getPlayerListName() + " + ",
				"- Blocks",
				" Broken: " + stats.get("blocksBroken"),
				" Placed: " + stats.get("blocksPlaced"),
				ChatColor.WHITE + ".",
				"- Items",
				" Crafted: " + stats.get("itemsCrafted"),
				" Broken: " + stats.get("toolsBroken"),
				" Eaten: " + stats.get("snacksEaten")
		};
	}
}