/*
 * StatisticsAPI.java
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

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.listeners.handlers.HandlerManager;
import com.wolvencraft.yasp.session.OfflineSession;
import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.settings.Constants.StatPerms;
import com.wolvencraft.yasp.util.VariableManager.ServerVariable;
import com.wolvencraft.yasp.util.cache.OfflineSessionCache;
import com.wolvencraft.yasp.util.cache.OnlineSessionCache;

/**
 * Simple API for servers statistics
 * @author bitWolfy
 *
 */
public class StatisticsAPI {
    
    /**
     * Returns the online player session
     * @param player Player object
     * @return Player session
     */
    public static OnlineSession getSession(Player player) {
        if(!isTracked(player)) return null;
        return OnlineSessionCache.fetch(player);
    }
    
    /**
     * Returns the OfflineSession for the player with the specified username.<br />
     * The player might not be online, or not exist at all.
     * @param username Player's username
     * @return DataSession with player's totals
     */
    public static OfflineSession getSession(String username) {
        return OfflineSessionCache.fetch(username);
    }
    
    /**
     * Returns the OfflineSession for the player with the specified username.<br />
     * The player might not be online, or not exist at all.
     * @param username Player's username
     * @param cached <b>true</b> if you want the plugin to cache this session
     * @return DataSession with player's totals
     */
    public static OfflineSession getSession(String username, boolean cached) {
        if(cached) return getSession(username);
        else return new OfflineSession(username);
    }
    
    /**
     * Checks if the player is tracked by the plugin
     * @param player Player object
     * @return <b>true</b> if the player is tracked, <b>false</b> otherwise
     */
    public static boolean isTracked(Player player) {
        return HandlerManager.playerLookup(player, StatPerms.Statistics);
    }
    
    /**
     * Returns the value of the specified variable
     * @param type Variable type
     * @return Variable value
     */
    public static Object getValue(ServerVariable type) {
        return Statistics.getServerTotals().getValues().get(type);
    }
    
}
