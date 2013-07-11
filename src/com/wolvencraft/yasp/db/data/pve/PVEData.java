/*
 * PVEData.java
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

package com.wolvencraft.yasp.db.data.pve;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.data.DataStore;
import com.wolvencraft.yasp.db.data.pve.DetailedPVEStats.PVEEntry;
import com.wolvencraft.yasp.events.player.TrackedPVEEvent;
import com.wolvencraft.yasp.session.OnlineSession;

/**
 * Data store that handles all PVE statistics on the server
 * @author bitWolfy
 *
 */
public class PVEData extends DataStore<TotalPVEStats, PVEEntry> {
    
    public PVEData(OnlineSession session) {
        super(session, DataStoreType.PVE);
    }
    
    /**
     * Returns a specific entry from the data store.<br />
     * If an entry does not exist, it will be created.
     * @param type Entity type of the creature
     * @param weapon Weapon used in the event
     * @return Corresponding entry
     */
    public TotalPVEStats getNormalData(EntityType type, ItemStack weapon) {
        for(TotalPVEStats entry : getNormalData()) {
            if(entry.equals(type, weapon)) return entry;
        }
        TotalPVEStats entry = new TotalPVEStats(session.getId(), type, weapon);
        normalData.add(entry);
        return entry;
    }
    
    /**
     * Registers the creature death in the data store
     * @param victim Creature killed
     * @param weapon Weapon used by killer
     */
    public void playerKilledCreature(Entity victim, ItemStack weapon) {
        getNormalData(victim.getType(), weapon).addCreatureDeaths();
        PVEEntry detailedEntry = new PVEEntry(victim.getType(), victim.getLocation(), weapon);
        detailedData.add(detailedEntry);
        
        Bukkit.getServer().getPluginManager().callEvent(new TrackedPVEEvent(session, detailedEntry));
    }
    
    /**
     * Registers the player death in the data store
     * @param killer Creature that killed the player
     * @param weapon Weapon used by killer
     */
    public void creatureKilledPlayer(Entity killer, ItemStack weapon) {
        getNormalData(killer.getType(), weapon).addPlayerDeaths();
        PVEEntry detailedEntry = new PVEEntry(killer.getType(), killer.getLocation());
        detailedData.add(detailedEntry);
        
        Bukkit.getServer().getPluginManager().callEvent(new TrackedPVEEvent(session, detailedEntry));
    }
    
}
