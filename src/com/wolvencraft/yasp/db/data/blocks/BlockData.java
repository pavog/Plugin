/*
 * BlockData.java
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
import org.bukkit.block.BlockState;

import com.wolvencraft.yasp.db.data.DataStore;
import com.wolvencraft.yasp.db.data.DetailedData;
import com.wolvencraft.yasp.db.data.blocks.DetailedBlockStats.BlockBreakEntry;
import com.wolvencraft.yasp.db.data.blocks.DetailedBlockStats.BlockPlaceEntry;
import com.wolvencraft.yasp.events.player.TrackedBlockBreakEvent;
import com.wolvencraft.yasp.events.player.TrackedBlockPlaceEvent;
import com.wolvencraft.yasp.session.OnlineSession;

/**
 * Data store that handles all block interactions on the server
 * @author bitWolfy
 *
 */
public class BlockData extends DataStore<TotalBlockStats, DetailedData> {
    
    public BlockData(OnlineSession session) {
        super(session, DataStoreType.Blocks);
    }

    /**
     * Returns the specific entry from the data store.<br />
     * If the entry does not exist, it will be created.
     * @param block BlockState of the block
     * @return Corresponding entry
     */
    private TotalBlockStats getNormalData(BlockState block) {
        for(TotalBlockStats entry : getNormalData()) {
            if(entry.equals(block)) return entry;
        }
        TotalBlockStats entry = new TotalBlockStats(session.getId(), block);
        normalData.add(entry);
        return entry;
    }
    
    /**
     * Registers the broken block in the data stores
     * @param block BlockState of the block
     */
    public void blockBreak(BlockState block) {
        getNormalData(block).addBroken();
        BlockBreakEntry detailedEntry = new BlockBreakEntry(block);
        detailedData.add(detailedEntry);
        
        Bukkit.getServer().getPluginManager().callEvent(new TrackedBlockBreakEvent(session, detailedEntry));
    }
    
    /**
     * Registers the placed block in the data stores
     * @param block BlockState of the block
     */
    public void blockPlace(BlockState block) {
        getNormalData(block).addPlaced();
        BlockPlaceEntry detailedEntry = new BlockPlaceEntry(block);
        detailedData.add(detailedEntry);
        
        Bukkit.getServer().getPluginManager().callEvent(new TrackedBlockPlaceEvent(session, detailedEntry));
    }
    
}
