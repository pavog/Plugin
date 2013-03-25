/*
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

package com.wolvencraft.yasp.util;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.DataCollector;

/**
 * Utility class containing assorted methods that do not fit other categories
 * @author bitWolfy
 *
 */
public class Util {
	
	/**
	 * Parses the specified string, replacing variables with corresponding values.<br />
	 * Borrows the variables and values from ServerTotals.
	 * @param str String to parse
	 * @return Parsed string
	 */
	public static String parseVars(String str) {
		if(str == null) return "";
		Map<String, Object> values = DataCollector.getServerTotals().getValues();
		Iterator<Entry<String, Object>> it = values.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
	        str = str.replace("<" + pairs.getKey() + ">", pairs.getValue() + "");
	        it.remove();
	    }
	    str = str.replace("<Y>", "");
		return parseChatColors(str);
	}
	
	/**
	 * Parses the string, replacing ampersand-codes with CraftBukkit color codes
	 * @param str String to be parsed
	 * @return Parsed string
	 */
	public static String parseChatColors(String str) {
		if(str == null) return "";
		for(ChatColor color : ChatColor.values()) str = str.replaceAll("&" + color.getChar(), color + "");
		return str;
	}
	
	/**
	 * Returns the current time in seconds.
	 * @return Current time
	 */
	public static long getTimestamp() {
		Timestamp timestamp = new Timestamp(new java.util.Date().getTime());
		return timestamp.getTime() / 1000;
	}
	
	/**
	 * Checks if the player is exempt from the statistics.<br />
	 * Performs a simple permissions check with node <i>stats.track</i>.
	 * @param player Player to check
	 * @return <b>true</b> if the player's statistics should not be registered, <b>false</b> otherwise.
	 */
	public static boolean isExempt(Player player) {
		return !player.hasPermission("stats.track");
	}
	
	/**
	 * Checks if the player is exempt from the statistics.
	 * @param player Player to check
	 * @param statsType Permission modifier
	 * @return <b>true</b> if the player's statistics should not be registered, <b>false</b> otherwise
	 */
	public static boolean isExempt(Player player, String statsType) {
		return !player.hasPermission("stats.track") && !player.hasPermission("stats.track." + statsType);
	}
	
}