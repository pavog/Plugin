/*
 * BlocksDataStore.java
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

package com.mctrakr.db.data.blocks;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;

import com.mctrakr.db.data.DetailedData;
import com.mctrakr.db.data.LargeDataStore;
import com.mctrakr.db.data.blocks.BlocksDetailedStats.BlockBreakEntry;
import com.mctrakr.db.data.blocks.BlocksDetailedStats.BlockPlaceEntry;
import com.mctrakr.events.player.TrackedBlockBreakEvent;
import com.mctrakr.events.player.TrackedBlockPlaceEvent;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock;
import com.mctrakr.settings.ConfigLock.PrimaryType;

/**
 * Data store that handles all block interactions on the server
 * @author bitWolfy
 *
 */
public class BlocksDataStore extends LargeDataStore<BlocksTotalStats, DetailedData> {
    
    public static ConfigLock lock = new ConfigLock(PrimaryType.Blocks);
    
    public BlocksDataStore(OnlineSession session) {
        super(session, PrimaryType.Blocks);
    }
    
    @Override
    public ConfigLock getLock() {
        return lock;
    }
    
    /**
     * Returns the specific entry from the data store.<br />
     * If the entry does not exist, it will be created.
     * @param block BlockState of the block
     * @return Corresponding entry
     */
    private BlocksTotalStats getNormalData(BlockState block) {
        for(BlocksTotalStats entry : getNormalData()) {
            if(entry.equals(block)) return entry;
        }
        BlocksTotalStats entry = new BlocksTotalStats(getSession().getId(), block);
        addNormalDataEntry(entry);
        return entry;
    }
    
    /**
     * Registers the broken block in the data stores
     * @param block BlockState of the block
     */
    public void blockBreak(BlockState block) {
        getNormalData(block).addBroken();
        BlockBreakEntry detailedEntry = new BlockBreakEntry(block);
        addDetailedDataEntry(detailedEntry);
        
        Bukkit.getServer().getPluginManager().callEvent(new TrackedBlockBreakEvent(getSession(), detailedEntry));
    }
    
    /**
     * Registers the placed block in the data stores
     * @param block BlockState of the block
     */
    public void blockPlace(BlockState block) {
        getNormalData(block).addPlaced();
        BlockPlaceEntry detailedEntry = new BlockPlaceEntry(block);
        addDetailedDataEntry(detailedEntry);
        
        Bukkit.getServer().getPluginManager().callEvent(new TrackedBlockPlaceEvent(getSession(), detailedEntry));
    }
    
}
