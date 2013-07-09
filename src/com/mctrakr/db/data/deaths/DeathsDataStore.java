/*
 * DeathsDataStore.java
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

package com.mctrakr.db.data.deaths;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.mctrakr.db.data.ConfigLock;
import com.mctrakr.db.data.DataStore;
import com.mctrakr.db.data.deaths.DeathsDetailedStats.NaturalDeathEntry;
import com.mctrakr.events.player.NaturalDeathEvent;
import com.mctrakr.session.OnlineSession;

/**
 * Data store that handles all natural deaths on the server
 * @author bitWolfy
 *
 */
public class DeathsDataStore extends DataStore<DeathsTotalStats, NaturalDeathEntry> {
    
    public static ConfigLock lock = new ConfigLock(ModuleType.Deaths);
    
    public DeathsDataStore(OnlineSession session) {
        super(session, ModuleType.Deaths);
    }
    
    @Override
    public ConfigLock getLock() {
        return lock;
    }
    
    /**
     * Registers the player death in the data store
     * @param location Location of the event
     * @param cause Death cause
     */
    public void playerDied(Location location, DamageCause cause) {
        DeathsTotalStats entry = null;
        for(DeathsTotalStats testEntry : getNormalData()) {
            if(testEntry.getCause().equals(cause)) entry = testEntry;
        }
        
        if(entry == null) {
            entry = new DeathsTotalStats(getSession().getId(), cause);
            addNormalDataEntry(entry);
        }
        
        entry.addTimes();
        NaturalDeathEntry detailedEntry = new NaturalDeathEntry(location, cause);
        addDetailedDataEntry(detailedEntry);
        
        Bukkit.getServer().getPluginManager().callEvent(new NaturalDeathEvent(getSession(), detailedEntry));
    }
    
}
