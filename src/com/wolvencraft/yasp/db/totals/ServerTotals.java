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

package com.wolvencraft.yasp.db.totals;

import java.util.HashMap;
import java.util.Map;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.tables.Normal.*;
import com.wolvencraft.yasp.util.tasks.DatabaseTask;

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
        distancePig = 0;
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
    private double distancePig;
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
        
        distanceFoot = Query.table(DistancePlayersTable.TableName).column(DistancePlayersTable.Foot).sum();
        distancePig = Query.table(DistancePlayersTable.TableName).column(DistancePlayersTable.Foot).sum();
        distanceMinecart = Query.table(DistancePlayersTable.TableName).column(DistancePlayersTable.Foot).sum();
        distanceBoat = Query.table(DistancePlayersTable.TableName).column(DistancePlayersTable.Foot).sum();
        distanceFlight = Query.table(DistancePlayersTable.TableName).column(DistancePlayersTable.Foot).sum();
        distanceSwim = Query.table(DistancePlayersTable.TableName).column(DistancePlayersTable.Foot).sum();
        distanceTotal = distanceFoot + distancePig + distanceMinecart + distanceBoat + distanceFlight + distanceSwim;
        
        toolsBroken = (int) Query.table(TotalItemsTable.TableName).column(TotalItemsTable.Broken).sum();
        itemsCrafted = (int) Query.table(TotalItemsTable.TableName).column(TotalItemsTable.Crafted).sum();
        snacksEaten = (int) Query.table(TotalItemsTable.TableName).column(TotalItemsTable.Used).sum();
        
        pvpKills = (int) Query.table(TotalPVPKillsTable.TableName).column(TotalPVPKillsTable.Times).sum();
        pveKills = (int) Query.table(TotalPVEKillsTable.TableName).column(TotalPVEKillsTable.CreatureKilled).sum();
        
        int pveDeaths = (int) Query.table(TotalPVEKillsTable.TableName).column(TotalPVEKillsTable.PlayerKilled).sum();
        int otherKills = (int) Query.table(TotalDeathPlayersTable.TableName).column(TotalDeathPlayersTable.Times).sum();
        deaths = pveDeaths + otherKills;
    }
    
    /**
     * Bundles up the values into one Map for ease of access.
     * @return Map of values
     */
    public Map<String, Object> getValues() {
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("blBroken", blocksBroken);
        values.put("blPlaced", blocksPlaced);
        
        values.put("distTotal", distanceTotal);
        values.put("distFoot", distanceFoot);
        values.put("distPig", distancePig);
        values.put("distCart", distanceMinecart);
        values.put("distBoat", distanceBoat);
        values.put("distFlight", distanceFlight);
        values.put("distSwim", distanceSwim);
        
        values.put("itBroken", toolsBroken);
        values.put("itCrafted", itemsCrafted);
        values.put("itEaten", snacksEaten);
        values.put("pvpKills", pvpKills);
        values.put("pveKills", pveKills);
        values.put("deaths", deaths);
        values.putAll(DatabaseTask.getStats().getValueMap());
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
    public void addDistance(DistancePlayersTable type, double distance) {
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
            case Pig:
                distancePig += distance;
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
