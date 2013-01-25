package com.wolvencraft.statistician.Utils;

import java.sql.Timestamp;

import org.bukkit.ChatColor;

public class Util {
 		
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
	 * Returns the current time in a timestamp form
	 * @return Current timestamp
	 */
	public static Timestamp getCurrentTime() {
		java.util.Date date= new java.util.Date();
		return new Timestamp(date.getTime());
	}
}