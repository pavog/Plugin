package com.wolvencraft.yasp.util;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.server.v1_4_R1.NBTTagCompound;
import net.minecraft.server.v1_4_R1.NBTTagList;
import net.minecraft.server.v1_4_R1.NBTTagString;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_4_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.DataCollector;

public class Util {
 	
	/**
	 * Replaces variables in the string with corresponding values
	 * @param str String to parse
	 * @return Resulting string
	 */
	public static String parseVars(String str) {
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
		return !player.hasPermission("stats.track");
	}
	
	/**
	 * Checks if the player is exempt from the statistics
	 * @param player Player in question
	 * @param statsType Permission modifier
	 * @return <b>true</b> if the player's stats should not be registered, <b>false</b> otherwise
	 */
	public static boolean isExempt(Player player, String statsType) {
		return !player.hasPermission("stats.track") && !player.hasPermission("stats.track." + statsType);
	}
	
	/**
	 * Compiles a book based on the parameters provided
	 * @param title
	 * @param author
	 * @param newPages
	 * @return
	 */
	public static ItemStack compileStatsBook (Player player) {
		net.minecraft.server.v1_4_R1.ItemStack item = CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(387, 1));
		
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