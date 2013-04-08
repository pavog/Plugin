/* 
 * PlayerCache.java
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
public class PlayerCache implements Runnable {
    
    private static Map<String, Integer> players;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new hashmap for player data storage
     */
    public PlayerCache() {
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
     * @param username Player name
     */
    public static void add(String username) {
        if(players.containsKey(username)) return;
        int playerId = fetchID(username);
        players.put(username, playerId);
    }
    
    /**
     * Removes a name-ID pair from the cache
     * @param username Player name
     */
    public static void remove(String username) {
        if(!players.containsKey(username)) return;
        players.remove(username);
    }
    
    /**
     * Fetches the player ID from the cache
     * @param username Player name
     * @return Player ID
     */
    public static int get(String username) {
        if(players.containsKey(username)) return players.get(username);
        int playerId = fetchID(username);
        players.put(username, playerId);
        return playerId;
    }
    
    /**
     * Returns the PlayerID corresponding with the specified username.<br />
     * If the username is not in the database, a dummy entry is created, and an ID is assigned.
     * @param username Player name to look up in the database
     * @return <b>Integer</b> PlayerID corresponding to the specified username
     */
    private static Integer fetchID(String username) {
        Message.debug("Retrieving a player ID for " + username);
        
        int playerId = -1;
        do {
            QueryResult playerRow = Query.table(PlayersTable.TableName)
                    .column(PlayersTable.PlayerId)
                    .condition(PlayersTable.Name, username)
                    .select();
            
            if(playerRow == null) {
                Query.table(PlayersTable.TableName)
                    .value(PlayersTable.Name, username)
                    .insert();
                continue;
            }
            playerId = playerRow.asInt(PlayersTable.PlayerId);
        } while (playerId == -1);
        Message.debug("User ID found: " + playerId);
        return playerId;
    }
}
