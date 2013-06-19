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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.db.data.DataStore;
import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.settings.Module;
import com.wolvencraft.yasp.util.VariableManager.ServerVariable;

/**
 * Utility class containing assorted methods that do not fit other categories
 * @author bitWolfy
 *
 */
public class Util {
    
    private Util() { }
    
    public static String parseString(String str) {
        if(str == null) return "NULL";

        str = str.replace("\u00A7", "&");
        str = StringEscapeUtils.escapeSql(str);
        
        return str;
    }
    
    /**
     * Composes a list of active modules for the player
     * @param session Player session
     * @return List of modules
     */
    @SuppressWarnings("rawtypes")
    public static List<DataStore> getModules(OnlineSession session) {
        List<DataStore> dataStores = new ArrayList<DataStore>();
        for(Module module : Module.values()) {
            if(module.isHook() || !module.isActive()) continue;
            for(Class<? extends DataStore> store : module.getDataStores()) {
                DataStore storeObj = null;
                try { storeObj = store.getDeclaredConstructor(OnlineSession.class).newInstance(session); }
                catch (Throwable t) { ExceptionHandler.handle(t); }
                if(storeObj == null) continue;
                dataStores.add(storeObj);
            }
        }
        return dataStores;
    }
    
    /**
     * Composes a list of active plugin hooks for the player
     * @param session Player session
     * @return List of plugin hooks
     */
    @SuppressWarnings("rawtypes")
    public static List<DataStore> getHooks(OnlineSession session) {
        List<DataStore> dataStores = new ArrayList<DataStore>();
        for(Module module : Module.values()) {
            if(!module.isHook() || !module.isActive()) continue;
            for(Class<? extends DataStore> store : module.getDataStores()) {
                DataStore storeObj = null;
                try { storeObj = store.getDeclaredConstructor(OnlineSession.class).newInstance(session); }
                catch (Throwable t) { ExceptionHandler.handle(t); }
                if(storeObj == null) continue;
                dataStores.add(storeObj);
            }
        }
        return dataStores;
    }
    
    /**
     * Compresses a List into a single-line json array
     * @param source List to compress
     * @return String json array
     */
    public static String toJsonArray(List<?> source) {
        return Statistics.getGson().toJson(source.toArray());
    }
    
    /**
     * Parses the specified string, replacing variables with corresponding values.<br />
     * Borrows the variables and values from ServerTotals.
     * @param str String to parse
     * @return Parsed string
     */
    public static String parseVars(String str) {
        if(str == null) return "";
        Map<ServerVariable, Object> values = Statistics.getServerTotals().getValues();
        Iterator<Entry<ServerVariable, Object>> it = values.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<ServerVariable, Object> pairs = (Map.Entry<ServerVariable, Object>)it.next();
            str = str.replace("<" + pairs.getKey().getAlias() + ">", pairs.getValue() + "");
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
        int days = (int) (timestamp / 86400);
        int hours = (int) ((timestamp - (days * 86400)) / 3600);
        int minutes = (int) ((timestamp - (days * 86400) - (hours * 3600)) / 60);
        int seconds = (int) ((timestamp - (days * 86400) - (hours * 3600) - (minutes * 60)) - minutes);
        if(days != 0) result += days + " days, ";
        result += hours + ":" + minutes + ":" + seconds;
        return result;
    }
    
    /**
     * Calculates the player's armor rating (out of 20)
     * @param inv Player's inventory
     * @return armor rating
     */
    public static int getArmorRating(PlayerInventory inv) {
        int armorRating = 0;
        if(inv.getHelmet() != null)
            switch(inv.getHelmet().getType()) {
                case LEATHER_HELMET: { armorRating += 1; break; }
                case GOLD_HELMET: { armorRating += 2; break; }
                case CHAINMAIL_HELMET: {armorRating += 2; break; }
                case IRON_HELMET: { armorRating += 2; break; }
                case DIAMOND_HELMET: { armorRating += 3; break; }
                default: break;
            }
        if(inv.getChestplate() != null)
            switch(inv.getChestplate().getType()) {
                case LEATHER_CHESTPLATE: { armorRating += 3; break; }
                case GOLD_CHESTPLATE: { armorRating += 5; break; }
                case CHAINMAIL_CHESTPLATE: {armorRating += 5; break; }
                case IRON_CHESTPLATE: { armorRating += 6; break; }
                case DIAMOND_CHESTPLATE: { armorRating += 8; break; }
                default: break;
            }
        if(inv.getLeggings() != null)
            switch(inv.getLeggings().getType()) {
                case LEATHER_LEGGINGS: { armorRating += 2; break; }
                case GOLD_LEGGINGS: { armorRating += 3; break; }
                case CHAINMAIL_LEGGINGS: {armorRating += 4; break; }
                case IRON_LEGGINGS: { armorRating += 5; break; }
                case DIAMOND_LEGGINGS: { armorRating += 6; break; }
                default: break;
            }
        if(inv.getBoots() != null)
            switch(inv.getBoots().getType()) {
                case LEATHER_BOOTS: { armorRating += 1; break; }
                case GOLD_BOOTS: { armorRating += 1; break; }
                case CHAINMAIL_BOOTS: {armorRating += 1; break; }
                case IRON_BOOTS: { armorRating += 2; break; }
                case DIAMOND_BOOTS: { armorRating += 3; break; }
                default: break;
            }
        return armorRating;
    }
    
    /**
     * Checks if the player is exempt from the statistics.<br />
     * Performs a simple permissions check with node <i>stats.track</i>.
     * @deprecated Use Settings.Permissions instead
     * @param player Player to check
     * @return <b>true</b> if the player's statistics should not be registered, <b>false</b> otherwise.
     */
    public static boolean isTracked(Player player) {
        return player.isOp()
            || player.hasPermission("stats.track");
    }
    
    /**
     * Checks if the player is exempt from the statistics.
     * @deprecated Use Settings.Permissions instead
     * @param player Player to check
     * @param statsType Permission modifier
     * @return <b>true</b> if the player's statistics should not be registered, <b>false</b> otherwise
     */
    public static boolean isTracked(Player player, String statsType) {
        return player.isOp()
            || player.hasPermission("stats.track")
            || player.hasPermission("stats.track." + statsType);
    }
    
}