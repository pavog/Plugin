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
import com.wolvencraft.yasp.db.data.receive.ServerTotals;
import com.wolvencraft.yasp.db.data.sync.ServerStatistics;
import com.wolvencraft.yasp.db.tables.Normal;
import com.wolvencraft.yasp.db.tables.Normal.PlayersTable;
import com.wolvencraft.yasp.session.OfflineSession;
import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.Util;

/**
 * Stores collected statistical data until it can be processed and sent to the database.<br />
 * This class is intended to be run in an asynchronous thread; all components are thread-safe.
 * @author bitWolfy
 *
 */
public class DataCollector implements Runnable {
    
    private static List<OnlineSession> sessions;
    private static ServerTotals serverTotals;
    private static ServerStatistics serverStatistics;

    /**
     * <b>Default constructor.</b><br />
     * Initializes an empty list of OnlineSessions
     */
    public DataCollector() {
        sessions = new ArrayList<OnlineSession>();
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
     * <li>Clear settings cache</li>
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
        
        Settings.clearCache();
    }
    
    /**
     * Forces the plugin to synchronize the player data to the database.<br />
     * If the player is not online, removes the session after the synchronization is complete.
     * @deprecated
     */
    public static void pushPlayerData() {
        Message.debug("Database synchronization in progress");
        for(OnlineSession session : get()) {
            session.pushData();
            session.getTotals().fetchData();
            
            if(session.isOnline()) continue;
            remove(session);
            
            long delay = Settings.RemoteConfiguration.LogDelay.asInteger();
            if(delay == 0 || session.getPlaytime() > delay) continue;
            
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
        for(OnlineSession session : get()) session.dumpData();
        sessions.clear();
    }
    
    /**
     * Returns all stored sessions.
     * @return List of stored player sessions
     */
    private static List<OnlineSession> get() {
        List<OnlineSession> tempList = new ArrayList<OnlineSession>();
        for(OnlineSession session : sessions) tempList.add(session);
        return tempList;
    }
    
    /**
     * Returns the OnlineSession associated with the specified player.<br />
     * If no session is found, it will be created.
     * @param player Tracked player
     * @return OnlineSession associated with the player.
     */
    public static OnlineSession get(Player player) {
        String username = player.getName();
        for(OnlineSession session : sessions) {
            if(session.getName().equals(username)) {
                return session;
            }
        }
        Message.debug("Creating a new user session for " + username);
        OnlineSession newSession = new OnlineSession(player);
        sessions.add(newSession);
        if(Settings.RemoteConfiguration.ShowFirstJoinMessages.asBoolean())
            Message.send(
                player,
                Settings.RemoteConfiguration.FirstJoinMessage.asString().replace("<PLAYER>", player.getName())
            );
        return newSession;
    }
    
    /**
     * Returns the OnlineSession associated with the specified player.<br />
     * If no session is found, returns <b>null</b>
     * @param playerId
     * @return OnlineSession associated with the player, or <b>null<b> if there isn't one.
     */
    public static OnlineSession get(int playerId) {
        for(OnlineSession session : sessions) {
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
    public static void remove(OnlineSession session) {
        Message.debug("Removing a user session for " + session.getName());
        sessions.remove(session);
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
