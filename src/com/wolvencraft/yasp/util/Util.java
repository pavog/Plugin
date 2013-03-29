/*
 * Util.java
 * 
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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.Settings;
import com.wolvencraft.yasp.Settings.ItemsWithMetadata;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.tables.Normal.PlayersTable;

/**
 * Utility class containing assorted methods that do not fit other categories
 * @author bitWolfy
 *
 */
public class Util {
    
    /**
     * Returns the PlayerID corresponding with the specified username.<br />
     * If the username is not in the database, a dummy entry is created, and an ID is assigned.
     * @param player Player name to look up in the database
     * @return <b>Integer</b> PlayerID corresponding to the specified username
     */
    public static Integer getPlayerIdAsynchronously(Player player) {
        String username = player.getName();
        Message.debug("Retrieving a player ID for " + username);
        
        int playerId = -1;
        QueryResult playerRow = Query.table(PlayersTable.TableName.toString())
                .column(PlayersTable.PlayerId.toString())
                .column(PlayersTable.Name.toString())
                .condition(PlayersTable.Name.toString(), username)
                .select();
        
        if(playerRow == null) {
            Query.table(PlayersTable.TableName.toString())
                    .value(PlayersTable.Name.toString(), username)
                    .insert();
            
            playerRow = Query
                    .table(PlayersTable.TableName.toString())
                    .column(PlayersTable.PlayerId.toString())
                    .column(PlayersTable.Name.toString())
                    .condition(PlayersTable.Name.toString(), username)
                    .select();
        }
        
        playerId = playerRow.getValueAsInteger(PlayersTable.PlayerId.toString());
        
        Message.debug("User ID found: " + playerId);
        return playerId;
    }
    
    /**
     * Parses the block and returns a database-safe string
     * @param type Block ID
     * @param data Block damage value
     * @return Database-safe string
     */
    public static String getBlockString(int type, int data) {
        if(type == -1) return "-1:0";
        if(Material.getMaterial(type) == null) return "0:0";
        String result = "" + type;
        if(!Settings.ItemsWithMetadata.checkAgainst(type)) return result + ":0";
        if(data <= Material.getMaterial(type).getMaxDurability()) result += ":" + data;
        else result += ":" + ItemsWithMetadata.get(type).getData();
        return result;
    }
    
    /**
     * Parses the block and returns a database-safe string
     * @param data Material data
     * @return Database-safe string
     */
    public static String getBlockString(MaterialData data) {
        return getBlockString(data.getItemTypeId(), data.getData());
    }
    
    /**
     * Parses the block and returns a database-safe string
     * @param stack Item stack
     * @return Database-safe string
     */
    public static String getBlockString(ItemStack stack) {
        return getBlockString(stack.getTypeId(), stack.getData().getData());
    }
    
    /**
     * Parses the specified string, replacing variables with corresponding values.<br />
     * Borrows the variables and values from ServerTotals.
     * @param str String to parse
     * @return Parsed string
     */
    public static String parseVars(String str) {
        if(str == null) return "";
        Map<String, Object> values = DataCollector.getTotals().getValues();
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
     * Changes a timestamp into a user-friendly form
     * @param timestamp Timestamp to parse
     * @return User-friendly output
     */
    public static String parseTimestamp(long timestamp) {
        String result = "";
        int minutes = (int) (timestamp / 60);
        int seconds = (int) (timestamp - minutes);
        int hours = (int) (timestamp / 3600);
        int days = (int) (timestamp / 86400);
        if(days != 0) result += days + " days, ";
        result += hours + ":" + minutes + ":" + seconds;
        return result;
    }
    
    /**
     * Checks if the player is exempt from the statistics.<br />
     * Performs a simple permissions check with node <i>stats.track</i>.
     * @param player Player to check
     * @return <b>true</b> if the player's statistics should not be registered, <b>false</b> otherwise.
     */
    public static boolean isTracked(Player player) {
        return player.isOp() || player.hasPermission("stats.track");
    }
    
    /**
     * Checks if the player is exempt from the statistics.
     * @param player Player to check
     * @param statsType Permission modifier
     * @return <b>true</b> if the player's statistics should not be registered, <b>false</b> otherwise
     */
    public static boolean isTracked(Player player, String statsType) {
        return player.isOp() || player.hasPermission("stats.track") || player.hasPermission("stats.track." + statsType);
    }
    
}