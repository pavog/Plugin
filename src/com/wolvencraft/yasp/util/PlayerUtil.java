/* 
 * PlayerUtil.java
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.tables.Normal.PlayersTable;

/**
 * Caches player names and IDs server-side
 * @author bitWolfy
 *
 */
public class PlayerUtil implements Runnable {
    
    private static Map<String, Integer> players;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new hashmap for player data storage
     */
    public PlayerUtil() {
        players = new HashMap<String, Integer>();
    }
    
    @Override
    public void run() {
        Iterator<Entry<String, Integer>> it = players.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>)it.next();
            String playerName = pairs.getKey();
            if(Bukkit.getPlayerExact(playerName) == null) {
                players.remove(playerName);
            }
            it.remove();
        }
    }
    
    /**
     * Adds a new player to the cache
     * @param playerName Player name
     */
    public static void add(String playerName) {
        if(players.containsKey(playerName)) return;
        int playerId = fetchID(playerName);
        players.put(playerName, playerId);
    }
    
    /**
     * Fetches the player ID from the cache
     * @param playerName Player name
     * @return Player ID
     */
    public static int get(String playerName) {
        if(players.containsKey(playerName)) return players.get(playerName);
        int playerId = fetchID(playerName);
        players.put(playerName, playerId);
        return playerId;
    }
    
    /**
     * Returns the PlayerID corresponding with the specified username.<br />
     * If the username is not in the database, a dummy entry is created, and an ID is assigned.
     * @param player Player name to look up in the database
     * @return <b>Integer</b> PlayerID corresponding to the specified username
     */
    private static Integer fetchID(String player) {
        Message.debug("Retrieving a player ID for " + player);
        
        int playerId = -1;
        do {
            QueryResult playerRow = Query.table(PlayersTable.TableName)
                    .column(PlayersTable.PlayerId)
                    .condition(PlayersTable.Name, player)
                    .select();
            
            if(playerRow == null) {
                Query.table(PlayersTable.TableName)
                     .value(PlayersTable.Name, player)
                     .insert();
                continue;
            }
            playerId = playerRow.asInt(PlayersTable.PlayerId);
        } while (playerId == -1);
        Message.debug("User ID found: " + playerId);
        return playerId;
    }
}
