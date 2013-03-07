package com.wolvencraft.yasp.util;

import java.sql.Timestamp;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Util {
 	
	/**
	 * Replaces variables in the string with corresponding values
	 * @param str String to parse
	 * @return Resulting string
	 */
	public static String parseVars(String str) {
		
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
			!player.hasPermission("stats.ignore") &&
			!player.hasPermission("statistician.ignore");
	}
	
	/**
	 * Checks if the player is exempt from the statistics
	 * @param player Player in question
	 * @param statsType Permission modifier
	 * @return <b>true</b> if the player's stats should not be registered, <b>false</b> otherwise
	 */
	public static boolean isExempt(Player player, String statsType) {
		return
			!player.hasPermission("stats.ignore") &&
			!player.hasPermission("statistician.ignore") &&
			!player.hasPermission("stats.ignore." + statsType);
	}
}