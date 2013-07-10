/*
 * PveDataStore.java
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

package com.mctrakr.db.data.pve;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.mctrakr.db.data.ConfigLock;
import com.mctrakr.db.data.LargeDataStore;
import com.mctrakr.db.data.pve.PveDetailedStats.PVEEntry;
import com.mctrakr.events.player.TrackedPVEEvent;
import com.mctrakr.session.OnlineSession;

/**
 * Data store that handles all PVE statistics on the server
 * @author bitWolfy
 *
 */
public class PveDataStore extends LargeDataStore<PveTotalStats, PVEEntry> {
    
    public static ConfigLock lock = new ConfigLock(ModuleType.PVE);
    
    public PveDataStore(OnlineSession session) {
        super(session, ModuleType.PVE);
    }
    
    @Override
    public ConfigLock getLock() {
        return lock;
    }
    
    /**
     * Returns a specific entry from the data store.<br />
     * If an entry does not exist, it will be created.
     * @param type Entity type of the creature
     * @param weapon Weapon used in the event
     * @return Corresponding entry
     */
    public PveTotalStats getNormalData(EntityType type, ItemStack weapon) {
        for(PveTotalStats entry : getNormalData()) {
            if(entry.equals(type, weapon)) return entry;
        }
        PveTotalStats entry = new PveTotalStats(getSession().getId(), type, weapon);
        addNormalDataEntry(entry);
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
        addDetailedDataEntry(detailedEntry);
        
        Bukkit.getServer().getPluginManager().callEvent(new TrackedPVEEvent(getSession(), detailedEntry));
    }
    
    /**
     * Registers the player death in the data store
     * @param killer Creature that killed the player
     * @param weapon Weapon used by killer
     */
    public void creatureKilledPlayer(Entity killer, ItemStack weapon) {
        getNormalData(killer.getType(), weapon).addPlayerDeaths();
        PVEEntry detailedEntry = new PVEEntry(killer.getType(), killer.getLocation());
        addDetailedDataEntry(detailedEntry);
        
        Bukkit.getServer().getPluginManager().callEvent(new TrackedPVEEvent(getSession(), detailedEntry));
    }
    
}
