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

import com.wolvencraft.yasp.db.totals.HookTotals;
import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.Bukkit;

import com.wolvencraft.yasp.db.totals.PlayerTotals;
import com.wolvencraft.yasp.util.cache.PlayerCache;
import java.util.UUID;

/**
 * A simplistic representation of a player's session.<br />
 * The player in question might be offline, or not exist at all.
 * @author bitWolfy
 *
 */
@Getter(AccessLevel.PUBLIC)
public class OfflineSession implements PlayerSession {
    
    private final int id;
    private final String name;
    private final UUID uuid;
    
    private PlayerTotals playerTotals;
    private HookTotals hookTotals;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new player data session based on the username and uuid
     * @param name Player username
     */
    public OfflineSession(String name) {
        //I hope there is an way to get the uuid form an offlien player in the future without using sending requests to the mc auth server
        this.uuid = null;
        this.name = name;  
        this.id = PlayerCache.get(name);
        this.playerTotals = new PlayerTotals(id);
        this.hookTotals = new HookTotals(id);
    }

    @Override
    public boolean isOnline() {
        if(Bukkit.getPlayerExact(name) == null) return false;
        return true;
    }
    
    @Override
    public UUID getUUID() {
        return this.uuid;
    }

}
