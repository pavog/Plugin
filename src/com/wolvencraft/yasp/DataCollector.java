/* 
 * DataCollector.java
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

package com.wolvencraft.yasp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.data.receive.ServerTotals;
import com.wolvencraft.yasp.db.data.sync.ServerStatistics;
import com.wolvencraft.yasp.db.tables.Normal;
import com.wolvencraft.yasp.db.tables.Normal.PlayersTable;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.Util;

/**
 * Stores collected statistical data until it can be processed and sent to the database.<br />
 * This class is intended to be run in an asynchronous thread; all components are thread-safe.
 * @author bitWolfy
 *
 */
public class DataCollector implements Runnable {
    
    private static List<LocalSession> sessions;
    private static ServerTotals serverTotals;
    private static ServerStatistics serverStatistics;

    /**
     * <b>Default constructor.</b><br />
     * Initializes an empty list of LocalSessions
     */
    public DataCollector() {
        sessions = new ArrayList<LocalSession>();
        serverStatistics = new ServerStatistics();
        serverTotals = new ServerTotals();
        
        for(Player player : Bukkit.getServer().getOnlinePlayers()) {
            if(Util.isTracked(player)) get(player);
        }
    }
    
    /**
     * Main database synchronization method.<br />
     * Performs actions in the following order:<br />
     * <ul>
     * <li>Confirm that the synchronization is not paused.</li>
     * <li>Push all player data to the database</li>
     * <li>Push generic server statistics to the database</li>
     * <li>Fetch server totals for signs and statistics books</li>
     * </ul>
     * This method is likely to freeze the main server thread.
     * Asynchronous threading is strongly recommended.
     */
    @Override
    public void run() {
        if(Statistics.getPaused()) return;
        pushPlayerData();
        serverStatistics.pushData();
        serverTotals.fetchData();
    }
    
    /**
     * Forces the plugin to synchronize the player data to the database.<br />
     * If the player is not online, removes the session after the synchronization is complete.
     */
    public static void pushPlayerData() {
        Message.debug("Database synchronization in progress");
        for(LocalSession session : get()) {
            session.pushData();
            session.playerTotals().fetchData();
            
            if(session.isOnline()) continue;
            remove(session);
            
            if(session.getConfirmed()) continue;
            Query.table(Normal.PlayersTable.TableName)
                    .condition(PlayersTable.Name, session.getName())
                    .delete();
        }
    }
    
    /**
     * Cycles through all open player sessions and dumps their data.
     * After that, clears the session list.
     */
    public static void dumpPlayerData() {
        for(LocalSession session : get()) session.dump();
        sessions.clear();
    }
    
    /**
     * Returns all stored sessions.
     * @return List of stored player sessions
     */
    private static List<LocalSession> get() {
        List<LocalSession> tempList = new ArrayList<LocalSession>();
        for(LocalSession session : sessions) tempList.add(session);
        return tempList;
    }
    
    /**
     * Returns the LocalSession associated with the specified player.<br />
     * If no session is found, it will be created.
     * @param player Tracked player
     * @return LocalSession associated with the player.
     */
    public static LocalSession get(Player player) {
        String username = player.getName();
        for(LocalSession session : sessions) {
            if(session.getName().equals(username)) {
                return session;
            }
        }
        Message.debug("Creating a new user session for " + username);
        LocalSession newSession = new LocalSession(player);
        newSession.setConfirmed(false);
        sessions.add(newSession);
        if(Settings.RemoteConfiguration.ShowFirstJoinMessages.asBoolean())
            Message.send(
                player,
                Settings.RemoteConfiguration.FirstJoinMessage.asString().replace("<PLAYER>", player.getName())
            );
        return newSession;
    }
    
    /**
     * Returns the LocalSession associated with the specified player.<br />
     * If no session is found, returns <b>null</b>
     * @param playerId
     * @return LocalSession associated with the player, or <b>null<b> if there isn't one.
     */
    public static LocalSession get(int playerId) {
        for(LocalSession session : sessions) {
            if(session.getId() == playerId) {
                return session;
            }
        }
        return null;
    }
    
    /**
     * Attempts to find a player session by the name. The player might be offline.
     * @param playerName Name of the player
     * @return OfflineSession with the specified player name, even if there isn't one.
     */
    public static OfflineSession get(String playerName) {
        Message.debug("Fetching an offline session for " + playerName);
        return new OfflineSession(playerName);
    }
    
    /**
     * Removes the specified session
     * @param session Session to remove
     */
    public static void remove(LocalSession session) {
        Message.debug("Removing a user session for " + session.getName());
        sessions.remove(session);
    }
    
    /**
     * Returns the PlayerID corresponding with the specified username.<br />
     * If the username is not in the database, a dummy entry is created, and an ID is assigned.
     * @param player Player name to look up in the database
     * @return <b>Integer</b> PlayerID corresponding to the specified username
     */
    public static Integer getPlayerId(Player player) {
        String username = player.getName();
        Message.debug("Retrieving a player ID for " + username);
        
        int playerId = -1;
        QueryResult playerRow = Query.table(PlayersTable.TableName)
                .column(PlayersTable.PlayerId)
                .column(PlayersTable.Name)
                .condition(PlayersTable.Name, username)
                .select();
        
        if(playerRow == null) {
            Query.table(PlayersTable.TableName)
                    .value(PlayersTable.Name, username)
                    .insert();
            
            playerRow = Query
                    .table(PlayersTable.TableName)
                    .column(PlayersTable.PlayerId)
                    .column(PlayersTable.Name)
                    .condition(PlayersTable.Name, username)
                    .select();
        }
        
        playerId = playerRow.asInt(PlayersTable.PlayerId);
        
        Message.debug("User ID found: " + playerId);
        return playerId;
    }
    
    /**
     * Returns the server totals for signs and books
     * @return Server totals
     */
    public static ServerTotals getTotals() {
        return serverTotals;
    }
    
    /**
     * Returns the generic server statistics
     * @return ServerStatistics
     */
    public static ServerStatistics getStats() {
        return serverStatistics;
    }
}
