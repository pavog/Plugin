/* 
 * OfflineSessionCache.java
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

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.data.player.PlayerData;
import com.wolvencraft.yasp.db.data.player.Tables.PlayerStats;
import com.wolvencraft.yasp.events.session.SessionCreateEvent;
import com.wolvencraft.yasp.events.session.SessionRemoveEvent;
import com.wolvencraft.yasp.managers.CacheManager.Type;
import com.wolvencraft.yasp.session.OfflineSession;
import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.settings.Constants.StatPerms;
import com.wolvencraft.yasp.settings.RemoteConfiguration;
import com.wolvencraft.yasp.util.Message;

/**
 * Caches Online player sessions server-side
 * @author bitWolfy
 *
 */
public class SessionCache extends CachedData {
    
    private static List<OnlineSession> onlineSessions;
    private static List<OfflineSession> offlineSessions;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a List for data storage and loads the online players into the list at a delay
     */
    public SessionCache() {
        super(Type.SESSION, (long)(5 * 60 * 20));
        
        onlineSessions = new ArrayList<OnlineSession>();
        offlineSessions = new ArrayList<OfflineSession>();
        
        Bukkit.getScheduler().runTaskLaterAsynchronously(Statistics.getInstance(), new Runnable() {
            
            @Override
            public void run() {
                for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if(StatPerms.Statistics.has(player))
                        ((PlayerData) fetch(player, true)
                                .getDataStore(com.wolvencraft.yasp.db.data.DataStore.Type.Player))
                                .addPlayerLog(player.getLocation(), true);
                }
            }
            
        }, 100L);
    }
    
    @Override
    public void clearCache() {
        for(OnlineSession session : getOnlineSessions()) {
            if(session.isOnline()) continue;
            session.finalize();
            session.pushData();
            removeSession(session);
            
            long delay = RemoteConfiguration.LogDelay.asInteger();
            
            if(delay == 0) continue;
            long totalPlaytime = ((PlayerData) session.getDataStore(com.wolvencraft.yasp.db.data.DataStore.Type.Player)).get().getTotalPlaytime();
            if(totalPlaytime > delay) continue;
            
            Query.table(PlayerStats.TableName)
                .condition(PlayerStats.Name, session.getName())
                .delete();
        }
        
        for(OfflineSession session : new ArrayList<OfflineSession>(offlineSessions)) {
            if(!session.isOnline()) offlineSessions.remove(session);
        }
    }
    
    /**
     * Returns the OnlineSession associated with the specified player.<br />
     * If no session is found, it will be created.
     * @param player Tracked player
     * @param login login event
     * @return OnlineSession associated with the player
     */
    public static OnlineSession fetch(Player player, boolean login) {
        for(OnlineSession session : onlineSessions) {
            if(session.getName().equals(player.getName())) {
                if(login && RemoteConfiguration.ShowWelcomeMessages.asBoolean()) {
                    Message.send(player, RemoteConfiguration.WelcomeMessage.asString().replace("<PLAYER>", player.getPlayerListName()));
                }
                return session;
            }
        }
        Message.debug("Creating a new user session for " + player.getName() + "(#" + onlineSessions.size() + ")");
        OnlineSession newSession = new OnlineSession(player);
        onlineSessions.add(newSession);
        
        if(login && RemoteConfiguration.ShowFirstJoinMessages.asBoolean()) {
            Message.send(
                player,
                RemoteConfiguration.FirstJoinMessage.asString().replace("<PLAYER>", player.getName())
            );
        }
        
        Bukkit.getServer().getPluginManager().callEvent(new SessionCreateEvent(newSession));
        return newSession;
    }
    
    /**
     * Returns the OnlineSession associated with the specified player.<br />
     * If no session is found, it will be created.
     * @param player Tracked player
     * @return OnlineSession associated with the player.
     */
    public static OnlineSession fetch(Player player) {
        return fetch(player, false);
    }
    
    /**
     * Fetches the OfflineSession from the cache
     * @param username Player name
     * @return Offline session
     */
    public static OfflineSession fetch(String username) {
        for(OfflineSession session : offlineSessions) {
            if(session.getName().equals(username)) return session;
        }
        OfflineSession session = new OfflineSession(username);
        offlineSessions.add(session);
        return session;
    }
    
    /**
     * Removes the specified session
     * @param session Session to remove
     */
    private static void removeSession(OnlineSession session) {
        Message.debug("Removing a user session for " + session.getName());
        Bukkit.getServer().getPluginManager().callEvent(new SessionRemoveEvent(session.getName()));
        onlineSessions.remove(session);
    }
    
    /**
     * Returns all stored sessions.
     * @return List of stored player sessions
     */
    public static List<OnlineSession> getOnlineSessions() {
        return new ArrayList<OnlineSession>(onlineSessions);
    }
    
    /**
     * Returns all stored sessions.
     * @return List of stored player sessions
     */
    public static List<OfflineSession> getOfflineSessions() {
        return new ArrayList<OfflineSession>(offlineSessions);
    }

    
    /**
     * Cycles through all open player sessions and dumps their data.
     * After that, clears the session list.
     */
    public static void dumpSessions() {
        for(OnlineSession session : getOnlineSessions()) {
            session.dumpData();
            removeSession(session);
        }
        onlineSessions.clear();
        
        offlineSessions.clear();
    }
    
}
