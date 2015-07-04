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

package com.wolvencraft.yasp.util.cache;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.tables.Normal.PlayerStats;
import com.wolvencraft.yasp.util.Message;

import java.util.UUID;
import org.bukkit.Bukkit;

/**
 * Caches player names and IDs server-side
 * @author bitWolfy
 *
 */
public class PlayerCache {
    
    private PlayerCache() { }
    
    /**
     * Returns the ID of the player
     * @param player Player to look up
     * @return Player ID
     */
    public static int get(Player player) {
        long start = System.currentTimeMillis();
        if(player.hasMetadata("stats_id") && !player.getMetadata("stats_id").isEmpty()) {
            int playerId = player.getMetadata("stats_id").get(0).asInt();
            if (playerId != -1){
                                
                long stop = System.currentTimeMillis();
                Message.debug("Took "+(stop-start)+"ms to retrieve " + player.getName()+"'s ID from cache.");
                
                return playerId;
            } else {
                playerId = get(player.getName(),player.getUniqueId());
                player.setMetadata("stats_id", new FixedMetadataValue(Statistics.getInstance(), playerId));
                
                long stop = System.currentTimeMillis();
                Message.debug("Took "+(stop-start)+"ms to retrieve " + player.getName()+"'s ID from database.");
                
                return playerId;  
            }
        } else {
            int playerId = get(player.getName(),player.getUniqueId());
            player.setMetadata("stats_id", new FixedMetadataValue(Statistics.getInstance(), playerId));
                            
            long stop = System.currentTimeMillis();
            Message.debug("Took "+(stop-start)+"ms to retrieve " + player.getName()+"'s ID from database.");
                
            return playerId;
        }
    }
    
    /**
     * Returns the player ID based on his name.<br />
     * Very resource-heavy; if possible, use <code>get(Player player);</code>
     * @param username Player name to look up
     * @return Player ID or -1 if players wasn#t found
     */
    public static int get(String username) {
        Message.debug("Retrieving a player ID for " + username);
        
        int playerId = -1;
        
        QueryResult playerRow = Query.table(PlayerStats.TableName)
                                     .column(PlayerStats.PlayerId)
                                     .condition(PlayerStats.Name, username)
                                     .condition(PlayerStats.UUID, "NULL")
                                     .select();      
        if(playerRow == null) {
            Message.debug("User ID of Player "+username+" not found.");
            return -1;
        }
        playerId = playerRow.asInt(PlayerStats.PlayerId);
        Message.debug("User ID (" + playerId +") found.");
        return playerId;
    }
    
    /**
     * Returns the player ID based on his uuid.<br />
     * Very resource-heavy; if possible, use <code>get(Player player);</code>
     * @param uuid Player's uuid to look up
     * @return Player ID or -1 if players wasn#t found
     */
    public static int get(UUID uuid) {
        int playerId = -1;
        QueryResult playerRow = Query.table(PlayerStats.TableName)
                                     .column(PlayerStats.PlayerId)
                                     .condition(PlayerStats.UUID, uuid.toString())
                                     .select();      
        if(playerRow == null) {
            return -1;
        }
        playerId = playerRow.asInt(PlayerStats.PlayerId);
        Message.debug("User ID (" + playerId +") found.");
        return playerId;
    }
    
    /**
     * Returns the player Name based on his uuid.<br />
     * @param uuid Player's uuid to look up
     * @return Player Name or null if players wasn't found
     */
    public static String getName(UUID uuid) {
        String playerName;
        QueryResult playerRow = Query.table(PlayerStats.TableName)
                                     .column(PlayerStats.Name)
                                     .condition(PlayerStats.UUID, uuid.toString())
                                     .select();      
        if(playerRow == null) {
            return null;
        }
        playerName = playerRow.asString(PlayerStats.Name);
        return playerName;
    }


    /**
     * Returns the player ID based on his uuid.<br />
     * If the no player is found with the given uuid searches for an player by the given name in the database and updates the uuid.
     * This is needen to support also pre 1.8 databases with no uuid's at all.
     * Very resource-heavy; if possible, use <code>get(Player player);</code>
     * @param username_ Player name to look up
     * @param uuid_ Players uuid to look up
     * @return Player ID or -1 if players wasn#t found
     */
    public static int get(String username_, UUID uuid_) {
        final String username = username_; 
        final UUID uuid = uuid_;
        int playerId = -1;
        int tries = 0;
        
        Message.debug("Retrieving a player ID for " + username);
        do {
            tries++;
            QueryResult playerRow = Query.table(PlayerStats.TableName)
                    .column(PlayerStats.PlayerId)
                    .condition(PlayerStats.UUID, uuid.toString())
                    .select();
            
            if(playerRow == null) {
                
                //Try if a player with the given name already exists in the database
                playerId = get(username);
                //If the player exists updtae his UUID
                if(playerId != -1){
                    
                    Bukkit.getScheduler().runTaskAsynchronously(Statistics.getInstance(), new Runnable() {
                        @Override
                        public void run(){
                            Query.table(PlayerStats.TableName)
                            .value(PlayerStats.UUID, uuid)
                            .condition(PlayerStats.Name, username)
                            .update();
                        }
                    });
                    
                    break;
                //If the player does not exist creat a new entry in the database    
                } else {
                    
                    Query.table(PlayerStats.TableName)
                    .value(PlayerStats.Name, username)
                    .value(PlayerStats.UUID, uuid)
                    .insert();
                    
                continue;    
                }              
            } else {
                //If an player with the given UUID exists in the database update his name
                Bukkit.getScheduler().runTaskAsynchronously(Statistics.getInstance(), new Runnable() {
                    @Override
                    public void run(){
                        Query.table(PlayerStats.TableName)
                        .value(PlayerStats.Name, username)
                        .condition(PlayerStats.UUID, uuid.toString())
                        .update();
                    }
                });               
            }
            playerId = playerRow.asInt(PlayerStats.PlayerId);
        } while (playerId == -1);
        Message.debug("User ID (" + playerId +") after "+tries +" tries found.");
        return playerId;
    }
}
