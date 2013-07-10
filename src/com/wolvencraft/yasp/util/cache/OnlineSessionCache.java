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
import com.wolvencraft.yasp.db.tables.Normal;
import com.wolvencraft.yasp.db.tables.Normal.PlayerStats;
import com.wolvencraft.yasp.events.session.SessionCreateEvent;
import com.wolvencraft.yasp.events.session.SessionRemoveEvent;
import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.settings.Constants.StatPerms;
import com.wolvencraft.yasp.settings.RemoteConfiguration;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.cache.CachedData.CachedDataProcess;

/**
 * Caches Online player sessions server-side
 * @author bitWolfy
 *
 */
public class OnlineSessionCache implements CachedDataProcess {

    private final long REFRESH_RATE_TICKS = (long)(5 * 60 * 20);
    private static List<OnlineSession> sessions = new ArrayList<OnlineSession>();
    
    /**
     * <b>Default constructor</b><br />
     * Creates a List for data storage and loads the online players into the list at a delay
     */
    public OnlineSessionCache() {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Statistics.getInstance(), new Runnable() {
            
            @Override
            public void run() {
                for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if(StatPerms.Statistics.has(player))
                        fetch(player).getPlayersData().addPlayerLog(player.getLocation(), true);
                }
            }
            
        }, 100L);
    }
    
    @Override
    public long getRefreshRate() {
        return REFRESH_RATE_TICKS;
    }
    
    @Override
    public void run() {
        for(OnlineSession session : getSessions()) {
            if(session.isOnline()) continue;
            session.finalize();
            session.pushData();
            removeSession(session);
            
            long delay = RemoteConfiguration.LogDelay.asInteger();
            if(delay == 0 || session.getPlayersData().getGeneralData().getTotalPlaytime() > delay) continue;
            
            Query.table(Normal.PlayerStats.TableName)
                .condition(PlayerStats.Name, session.getName())
                .delete();
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
        for(OnlineSession session : sessions) {
            if(session.getName().equals(player.getName())) {
                if(login && RemoteConfiguration.ShowWelcomeMessages.asBoolean()) {
                    Message.send(player, RemoteConfiguration.WelcomeMessage.asString().replace("<PLAYER>", player.getPlayerListName()));
                }
                return session;
            }
        }
        Message.debug("Creating a new user session for " + player.getName() + "(#" + sessions.size() + ")");
        OnlineSession newSession = new OnlineSession(player);
        sessions.add(newSession);
        
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
        return new ArrayList<OnlineSession>(sessions);
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
    
    @Override
    public void finalize() {
        dumpSessions();
    }
    
}
