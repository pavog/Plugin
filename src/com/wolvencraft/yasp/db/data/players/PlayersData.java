/*
 * PlayersData.java
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

package com.wolvencraft.yasp.db.data.players;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.data.DetailedData;
import com.wolvencraft.yasp.settings.Module;

/**
 * A unique data store that contains basic information about the player
 * @author bitWolfy
 *
 */
public class PlayersData {
    
    private int playerId;
    private PlayerEntry generalData;
    private DistancePlayerEntry distanceData;
    private MiscInfoPlayerEntry miscData;
    private InventoryEntry inventoryData;
    
    private List<DetailedData> detailedData;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new PlayersData object based on the data provided
     * @param player Player object
     * @param playerId Player ID
     */
    public PlayersData(Player player, int playerId) {
        this.playerId = playerId;
        generalData = new PlayerEntry(playerId, player);
        distanceData = new DistancePlayerEntry(playerId);
        miscData = new MiscInfoPlayerEntry(playerId, player);
        if(Module.Inventory.isEnabled()) inventoryData = new InventoryEntry(playerId, player);
        
        detailedData = new ArrayList<DetailedData>();
    }
    
    /**
     * Returns a static copy of DetailedData to prevent ConcurrentModificationException occurrences
     * @return List of DetailedData objects
     */
    private List<DetailedData> getDetailedData() {
        List<DetailedData> temp = new ArrayList<DetailedData>();
        for(DetailedData value : detailedData) temp.add(value);
        return temp;
    }
    
    /**
     * Pushes the data to the database
     */
    public void sync() {
        generalData.pushData(playerId);
        distanceData.pushData(playerId);
        miscData.pushData(playerId);
        if(Module.Inventory.isEnabled()) inventoryData.pushData(playerId);
        
        for(DetailedData entry : getDetailedData()) {
            if(entry.pushData(playerId)) { detailedData.remove(entry); }
        }
    }
    
    /**
     * Erases all locally stored data
     */
    public void dump() {
        for(DetailedData entry : getDetailedData()) {
            detailedData.remove(entry);
        }
    }
    
    /**
     * Returns the generic player data from the Players table.<br />
     * This information rarely changes
     * @return Players data store
     */
    public PlayerEntry getGeneralData() {
        return generalData;
    }
    
    /**
     * Returns the information from the Distances table.
     * @return Distances data store
     */
    public DistancePlayerEntry getDistanceData() {
        return distanceData;
    }
    
    /**
     * Returns the information from the Miscellaneous table.<br />
     * This information is likely to change rapidly.
     * @return Miscellaneous data store
     */
    public MiscInfoPlayerEntry getMiscData() {
        return miscData;
    }
    
    /**
     * Logs player's login/logout location
     * @param location Location of the login
     * @param isLogin <b>true</b> if the player has logged in, <b>false</b> otherwise
     */
    public void addPlayerLog(Location location, boolean isLogin) {
        detailedData.add(new DetailedLogPlayerEntry(location, isLogin));
    }
 
}
