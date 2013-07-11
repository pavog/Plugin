/*
 * DeathData.java
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

package com.wolvencraft.yasp.db.data.deaths;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.wolvencraft.yasp.db.data.DataStore;
import com.wolvencraft.yasp.db.data.deaths.DetailedDeathStats.NaturalDeathEntry;
import com.wolvencraft.yasp.events.player.NaturalDeathEvent;
import com.wolvencraft.yasp.session.OnlineSession;

/**
 * Data store that handles all natural deaths on the server
 * @author bitWolfy
 *
 */
public class DeathData extends DataStore<TotalDeathStats, NaturalDeathEntry> {

    /**
     * <b>Default constructor</b><br />
     * Creates an empty data store to save the statistics until database synchronization.
     * @param session Player session
     */
    public DeathData(OnlineSession session) {
        super(session, DataStoreType.Deaths);
    }
    
    /**
     * Registers the player death in the data store
     * @param location Location of the event
     * @param cause Death cause
     */
    public void playerDied(Location location, DamageCause cause) {
        TotalDeathStats entry = null;
        for(TotalDeathStats testEntry : getNormalData()) {
            if(testEntry.getCause().equals(cause)) entry = testEntry;
        }
        
        if(entry == null) {
            entry = new TotalDeathStats(session.getId(), cause);
            normalData.add(entry);
        }
        
        entry.addTimes();
        NaturalDeathEntry detailedEntry = new NaturalDeathEntry(location, cause);
        detailedData.add(detailedEntry);
        
        Bukkit.getServer().getPluginManager().callEvent(new NaturalDeathEvent(session, detailedEntry));
    }
    
}
