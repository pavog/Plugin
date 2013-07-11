/*
 * ServerTotals.java
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

package com.mctrakr.modules.totals;

import java.util.HashMap;
import java.util.Map;

import com.mctrakr.Statistics;
import com.mctrakr.database.Query;
import com.mctrakr.modules.stats.blocks.Tables.TotalBlocksTable;
import com.mctrakr.modules.stats.deaths.Tables.TotalDeathsTable;
import com.mctrakr.modules.stats.distance.Tables.DistancesTable;
import com.mctrakr.modules.stats.items.Tables.TotalItemsTable;
import com.mctrakr.modules.stats.pve.Tables.PveTotalsTable;
import com.mctrakr.modules.stats.pvp.Tables.PvpTotalsTable;
import com.mctrakr.util.VariableManager.ServerVariable;

/**
 * Generic Server information used on DisplaySigns and books.
 * @author bitWolfy
 *
 */
public class ServerTotals {
    
    /**
     * <b>Default Constructor</b><br />
     * Sets up the default values for the data holder.
     */
    public ServerTotals() {
        blocksBroken = 0;
        blocksPlaced = 0;
        
        distanceTotal = 0;
        distanceFoot = 0;
        distanceRide = 0;
        distanceMinecart = 0;
        distanceBoat = 0;
        distanceFlight = 0;
        distanceSwim = 0;
        
        toolsBroken = 0;
        itemsCrafted = 0;
        snacksEaten = 0;
        
        pvpKills = 0;
    }
    
    private int blocksBroken;
    private int blocksPlaced;
    
    private double distanceTotal;
    private double distanceFoot;
    private double distanceRide;
    private double distanceMinecart;
    private double distanceBoat;
    private double distanceFlight;
    private double distanceSwim;
    
    private int toolsBroken;
    private int itemsCrafted;
    private int snacksEaten;
    
    private int pvpKills;
    private int pveKills;
    private int deaths;
    
    /**
     * Fetches the data from the remote database.<br />
     * Automatically calculates values from the contents of corresponding tables.
     */
    public void fetchData() {

        if(!Statistics.getInstance().isEnabled()) return;
        
        blocksBroken = (int) Query.table(TotalBlocksTable.TableName).column(TotalBlocksTable.Destroyed).sum();
        blocksPlaced = (int) Query.table(TotalBlocksTable.TableName).column(TotalBlocksTable.Placed).sum();
        
        distanceFoot = Query.table(DistancesTable.TableName).column(DistancesTable.Foot).sum();
        distanceRide = Query.table(DistancesTable.TableName).column(DistancesTable.Foot).sum();
        distanceMinecart = Query.table(DistancesTable.TableName).column(DistancesTable.Foot).sum();
        distanceBoat = Query.table(DistancesTable.TableName).column(DistancesTable.Foot).sum();
        distanceFlight = Query.table(DistancesTable.TableName).column(DistancesTable.Foot).sum();
        distanceSwim = Query.table(DistancesTable.TableName).column(DistancesTable.Foot).sum();
        distanceTotal = distanceFoot + distanceRide + distanceMinecart + distanceBoat + distanceFlight + distanceSwim;
        
        toolsBroken = (int) Query.table(TotalItemsTable.TableName).column(TotalItemsTable.Broken).sum();
        itemsCrafted = (int) Query.table(TotalItemsTable.TableName).column(TotalItemsTable.Crafted).sum();
        snacksEaten = (int) Query.table(TotalItemsTable.TableName).column(TotalItemsTable.Used).sum();
        
        pvpKills = (int) Query.table(PvpTotalsTable.TableName).column(PvpTotalsTable.Times).sum();
        pveKills = (int) Query.table(PveTotalsTable.TableName).column(PveTotalsTable.CreatureKilled).sum();
        
        int pveDeaths = (int) Query.table(PveTotalsTable.TableName).column(PveTotalsTable.PlayerKilled).sum();
        int otherKills = (int) Query.table(TotalDeathsTable.TableName).column(TotalDeathsTable.Times).sum();
        deaths = pveDeaths + otherKills;
    }
    
    /**
     * Bundles up the values into one Map for ease of access.
     * @return Map of values
     */
    public Map<ServerVariable, Object> getValues() {
        @SuppressWarnings("serial")
        Map<ServerVariable, Object> values = new HashMap<ServerVariable, Object>()
        {{
            put(ServerVariable.BLOCKS_BROKEN, blocksBroken);
            put(ServerVariable.BLOCKS_PLACED, blocksPlaced);
            
            put(ServerVariable.DISTANCE_TRAVELED, distanceTotal);
            put(ServerVariable.DISTANCE_FOOT, distanceFoot);
            put(ServerVariable.DISTANCE_RIDE, distanceRide);
            put(ServerVariable.DISTANCE_CART, distanceMinecart);
            put(ServerVariable.DISTANCE_BOAT, distanceBoat);
            put(ServerVariable.DISTANCE_FLIGHT, distanceFlight);
            put(ServerVariable.DISTANCE_SWIM, distanceSwim);
            
            put(ServerVariable.ITEMS_BROKEN, toolsBroken);
            put(ServerVariable.ITEMS_CRAFTED, itemsCrafted);
            put(ServerVariable.ITEMS_EATEN, snacksEaten);
            
            put(ServerVariable.PVP_KILLS, pvpKills);
            put(ServerVariable.PVE_KILLS, pveKills);
            put(ServerVariable.DEATHS, deaths);
        }};
        
        values.putAll(Statistics.getServerStatistics().getValueMap());
        return values;
    }
    

    
    /**
     * Registers a block being broken
     */
    public void blockBreak() {
        blocksBroken++;
    }
    
    /**
     * Registers a block being places
     */
    public void blockPlace() {
        blocksPlaced++;
    }
    
    /**
     * Increases the distance traveled by different means
     * @param type Travel type
     * @param distance Distance traveled
     */
    public void addDistance(DistancesTable type, double distance) {
        distanceTotal += distance;
        switch(type) {
            case Foot:
                distanceFoot += distance;
                break;
            case Swim:
                distanceSwim += distance;
                break;
            case Flight:
                distanceFlight += distance;
                break;
            case Boat:
                distanceBoat += distance;
                break;
            case Minecart:
                distanceMinecart += distance;
                break;
            case Ride:
                distanceRide += distance;
                break;
            default:
                break;
        }
    }
    
    /**
     * Registers a tool being broken
     */
    public void toolBreak() {
        toolsBroken++;
    }
    
    /**
     * Registers an item being crafted
     */
    public void itemCraft() {
        itemsCrafted++;
    }
    
    /**
     * Registers a food item being eaten
     */
    public void snacksEaten() {
        snacksEaten++;
    }
    
    /**
     * Registers a player being killed in PvP
     */
    public void pvpKill() {
        pvpKills++;
    }
    
    /**
     * Registers the player dying
     */
    public void death() {
        deaths++;
    }
    
    /**
     * Registers a player killing a mob
     */
    public void pveKill() {
        pveKills++;
    }
    
}
