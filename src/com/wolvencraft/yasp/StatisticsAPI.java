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

import com.wolvencraft.yasp.session.DataSession;
import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.util.VariableManager.ServerVariable;
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
    public static OnlineSession getOnlinePlayer(Player player) {
        return OnlineSessionCache.fetch(player);
    }
    
    /**
     * Returns the DataSession for the player with the specified username.<br />
     * The player might not be online, or not exist at all.
     * @param username Player's username
     * @return DataSession with player's totals
     */
    public static DataSession getPlayer(String username) {
        return new DataSession(username);
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
