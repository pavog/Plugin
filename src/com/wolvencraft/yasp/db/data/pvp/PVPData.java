/*
 * PVPData.java
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

package com.wolvencraft.yasp.db.data.pvp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.data.DataStore;
import com.wolvencraft.yasp.db.data.pvp.DetailedPVPStats.PVPEntry;
import com.wolvencraft.yasp.events.player.TrackedPVPEvent;
import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.util.cache.PlayerCache;

/**
 * Data store that handles all PVP statistics on the server
 * @author bitWolfy
 *
 */
public class PVPData extends DataStore<TotalPVPStats, PVPEntry> {
    
    public PVPData(OnlineSession session) {
        super(session, DataStoreType.PVP);
    }
    
    /**
     * Returns the specific entry from the data store.<br />
     * If the entry does not exist, it will be created.
     * @param victimId ID of the victim in a PVP event
     * @param weapon Weapon used in the event
     * @return Corresponding entry
     */
    public TotalPVPStats getNormalData(int victimId, ItemStack weapon) {
        for(TotalPVPStats entry : getNormalData()) {
            if(entry.equals(victimId, weapon)) return entry;
        }
        TotalPVPStats entry = new TotalPVPStats(session.getId(), victimId, weapon);
        normalData.add(entry);
        return entry;
    }
    
    /**
     * Registers the player death in the data store
     * @param victim Player who was killed 
     * @param weapon Weapon used by killer
     */
    public void playerKilledPlayer(Player victim, ItemStack weapon) {
        int victimId = PlayerCache.get(victim);
        getNormalData(victimId, weapon).addTimes();
        PVPEntry detailedEntry = new PVPEntry(victim.getLocation(), victimId, weapon);
        detailedData.add(detailedEntry);
        
        Bukkit.getServer().getPluginManager().callEvent(new TrackedPVPEvent(session, detailedEntry));
    }
    
}
