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

import net.minecraft.server.v1_5_R2.NBTTagCompound;
import net.minecraft.server.v1_5_R2.NBTTagList;
import net.minecraft.server.v1_5_R2.NBTTagString;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_5_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
	
	/**
	 * Creates a new statistics book based for the specified player.<br />
	 * Version-specific method. Include methods to check for CraftBukkit version in the implementation.
	 * @param player Player to use for statistics
	 * @return Book with player's statistics
	 */
	public static ItemStack compileStatsBook (Player player) {
		net.minecraft.server.v1_5_R2.ItemStack item = CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(387, 1));
		
		NBTTagCompound tags = item.getTag();
        if (tags == null) {
        	tags = new NBTTagCompound();
            item.setTag(tags);
        }
        
    	tags.setString("title", player.getPlayerListName() + " Statistics");
    	tags.setString("author", "YASP");
    	
    	NBTTagList pages = new NBTTagList("pages");
    	String[] newPages = getBookPages(player.getPlayerListName());
    	
        for(int i = 0; i < newPages.length; i++) {
        	pages.add(new NBTTagString("" + i + "", newPages[i]));
        }
    	tags.set("pages", pages);
    	item.setTag(tags);
		return CraftItemStack.asBukkitCopy(item);
	}
	
	/**
	 * Returns the pages for the book with player's statistics. Could be used for offline players.<br />
	 * It is safe to use this method with any version of CraftBukkit.
	 * @param playerName Player name to use for the statistics
	 * @return Array of strings, each of them representing a new page in the book.
	 */
	public static String[] getBookPages(String playerName) {
        Map<String, Object> stats = DataCollector.get(playerName).playerTotals().getValues();
    	return new String[] {
				ChatColor.DARK_RED + "" + " + " + ChatColor.BOLD + ChatColor.UNDERLINE + playerName + ChatColor.RESET + " + \n" + 
				ChatColor.WHITE + "." + "\n" + 
				ChatColor.BLACK + ChatColor.BOLD + "  Blocks and items \n" + 
				ChatColor.RED + ChatColor.BOLD + " - Blocks " + ChatColor.RESET + "\n" + 
				ChatColor.BLACK + " Broken: " + stats.get("blocksBroken") + "\n" + 
				ChatColor.BLACK + " Placed: " + stats.get("blocksPlaced") + "\n" + 
				ChatColor.WHITE + "." + "\n" + 
				ChatColor.RED + ChatColor.BOLD + "- Items" + ChatColor.RESET + "\n" +
				ChatColor.BLACK + " Crafted: " + stats.get("itemsCrafted") + "\n" + 
				ChatColor.BLACK + " Broken: " + stats.get("toolsBroken") + "\n" + 
				ChatColor.BLACK + " Eaten: " + stats.get("snacksEaten"),
				
				ChatColor.DARK_RED + "" + " + " + ChatColor.BOLD + ChatColor.UNDERLINE + playerName + ChatColor.RESET + " + \n" + 
				ChatColor.WHITE + "." + "\n" + 
				ChatColor.BLACK + ChatColor.BOLD + "  Kills and Deaths \n" + 
				ChatColor.RED + ChatColor.BOLD + " - PvP" + ChatColor.RESET + "\n" + 
				ChatColor.BLACK + " Kills: " + stats.get("pvpKills") + "\n" + 
				ChatColor.BLACK + " Deaths: " + stats.get("pvpDeaths") + "\n" + 
				ChatColor.BLACK + " K/D: " + stats.get("kdr") + "\n" + 
				ChatColor.WHITE + "." + "\n" + 
				ChatColor.RED + ChatColor.BOLD + " - Other \n" + ChatColor.RESET +
				ChatColor.BLACK + " Mob kills: " + stats.get("pveKills")
		};
	}
}