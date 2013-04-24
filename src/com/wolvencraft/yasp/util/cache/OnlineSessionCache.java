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
import com.wolvencraft.yasp.api.events.SessionRemoveEvent;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.tables.Normal;
import com.wolvencraft.yasp.db.tables.Normal.PlayersTable;
import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.settings.RemoteConfiguration;
import com.wolvencraft.yasp.settings.Constants.StatPerms;
import com.wolvencraft.yasp.util.Message;

/**
 * Caches Online player sessions server-side
 * @author bitWolfy
 *
 */
public class OnlineSessionCache implements CachedData {
    
    private static List<OnlineSession> sessions;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a List for data storage and loads the online players into the list at a delay
     */
    public OnlineSessionCache() {
        sessions = new ArrayList<OnlineSession>();
        
        Bukkit.getScheduler().runTaskLaterAsynchronously(Statistics.getInstance(), new Runnable() {
            
            @Override
            public void run() {
                for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if(StatPerms.Statistics.has(player)) fetch(player);
                }
            }
            
        }, 100L);
    }
    
    @Override
    public void run() {
        for(OnlineSession session : getSessions()) {
            if(session.isOnline()) continue;
            session.logout();
            session.pushData();
            removeSession(session);
            
            long delay = RemoteConfiguration.LogDelay.asInteger();
            if(delay == 0 || session.getPlaytime() > delay) continue;
            
            Query.table(Normal.PlayersTable.TableName)
                .condition(PlayersTable.Name, session.getName())
                .delete();
        }
    }
    
    /**
     * Returns the OnlineSession associated with the specified player.<br />
     * If no session is found, it will be created.
     * @param player Tracked player
     * @return OnlineSession associated with the player.
     */
    public static OnlineSession fetch(Player player) {
        String username = player.getName();
        for(OnlineSession session : sessions) {
            if(session.getName().equals(username)) {
                return session;
            }
        }
        Message.debug("Creating a new user session for " + username);
        OnlineSession newSession = new OnlineSession(player);
        sessions.add(newSession);
        if(RemoteConfiguration.ShowFirstJoinMessages.asBoolean()) {
            Message.send(
                player,
                RemoteConfiguration.FirstJoinMessage.asString().replace("<PLAYER>", player.getName())
            );
        }
        return newSession;
    }
    
    /**
     * Removes the specified session
     * @param session Session to remove
     */
    private static void removeSession(OnlineSession session) {
        Message.debug("Removing a user session for " + session.getName());
        Bukkit.getServer().getPluginManager().callEvent(new SessionRemoveEvent(session.getName()));
        sessions.remove(session);
    }
    
    /**
     * Returns all stored sessions.
     * @return List of stored player sessions
     */
    public static List<OnlineSession> getSessions() {
        List<OnlineSession> tempList = new ArrayList<OnlineSession>();
        for(OnlineSession session : sessions) tempList.add(session);
        return tempList;
    }

    
    /**
     * Cycles through all open player sessions and dumps their data.
     * After that, clears the session list.
     */
    public static void dumpSessions() {
        for(OnlineSession session : getSessions()) {
            session.dumpData();
            removeSession(session);
        }
        sessions.clear();
    }
    
}
