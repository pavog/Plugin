/*
 * OfflineSession.java
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

package com.wolvencraft.yasp.session;

import org.bukkit.Bukkit;

import com.wolvencraft.yasp.db.totals.PlayerTotals;
import com.wolvencraft.yasp.util.cache.PlayerCache;

/**
 * A cut-down version of OnlineSession.<br />
 * The player might be offline, or not exist at all
 * @author bitWolfy
 *
 */
public class OfflineSession implements PlayerSession {
    
    private final int id;
    private final String name;
    
    private PlayerTotals playerTotals;
    
    /**
     * <b>Default constructor</b><br />
     * Attempts to create a new user session for the player with the specified name.
     * @param name Player name
     */
    public OfflineSession(String name) {
        this.name = name;
        this.id = PlayerCache.get(name);
        
        this.playerTotals = new PlayerTotals(id);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isOnline() {
        if(Bukkit.getPlayerExact(name) == null) return false;
        return true;
    }
    
    /**
     * Returns the totals associated with this player
     * @return Player totals
     */
    public PlayerTotals getTotals() {
        return playerTotals;
    }

}
