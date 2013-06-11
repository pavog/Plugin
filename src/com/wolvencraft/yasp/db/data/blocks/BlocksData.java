/*
 * BlocksData.java
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

package com.wolvencraft.yasp.db.data.blocks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;

import com.wolvencraft.yasp.api.events.player.TrackedBlockBrokenEvent;
import com.wolvencraft.yasp.api.events.player.TrackedBlockPlacedEvent;
import com.wolvencraft.yasp.db.data.AdvancedDataStore;
import com.wolvencraft.yasp.db.data.DetailedData;
import com.wolvencraft.yasp.session.OnlineSession;

/**
 * Data store that records all block interactions on the server.
 * @author bitWolfy
 *
 */
public class BlocksData extends AdvancedDataStore<TotalBlocksEntry, DetailedData> {
    
    public BlocksData(OnlineSession session) {
        super(session, DataStoreType.Blocks);
    }

    /**
     * Returns the specific entry from the data store.<br />
     * If the entry does not exist, it will be created.
     * @param block BlockState of the block
     * @return Corresponding entry
     */
    private TotalBlocksEntry getNormalData(BlockState block) {
        for(TotalBlocksEntry entry : normalData) {
            if(entry.equals(block)) return entry;
        }
        TotalBlocksEntry entry = new TotalBlocksEntry(session.getId(), block);
        normalData.add(entry);
        return entry;
    }
    
    /**
     * Registers the broken block in the data stores
     * @param session Corresponding OnlineSession
     * @param location Location of the block
     * @param block BlockState of the block
     */
    public void blockBreak(Location location, BlockState block) {
        getNormalData(block).addBroken();
        DetailedBlockBrokenEntry detailedEntry = new DetailedBlockBrokenEntry(location, block);
        detailedData.add(detailedEntry);
        
        Bukkit.getServer().getPluginManager().callEvent(new TrackedBlockBrokenEvent(session, detailedEntry));
    }
    
    /**
     * Registers the placed block in the data stores
     * @param session Corresponding OnlineSession
     * @param location Location of the block
     * @param block BlockState of the block
     */
    public void blockPlace(Location location, BlockState block) {
        getNormalData(block).addPlaced();
        DetailedBlockPlacedEntry detailedEntry = new DetailedBlockPlacedEntry(location, block);
        detailedData.add(detailedEntry);
        
        Bukkit.getServer().getPluginManager().callEvent(new TrackedBlockPlacedEvent(session, detailedEntry));
    }
    
}
