/*
 * PlayerTotals.java
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

package com.wolvencraft.yasp.db.data.receive;

import java.util.HashMap;
import java.util.Map;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.tables.Normal.DistancePlayersTable;
import com.wolvencraft.yasp.db.tables.Normal.PlayersTable;
import com.wolvencraft.yasp.db.tables.Normal.TotalBlocksTable;
import com.wolvencraft.yasp.db.tables.Normal.TotalDeathPlayersTable;
import com.wolvencraft.yasp.db.tables.Normal.TotalItemsTable;
import com.wolvencraft.yasp.db.tables.Normal.TotalPVEKillsTable;
import com.wolvencraft.yasp.db.tables.Normal.TotalPVPKillsTable;
import com.wolvencraft.yasp.util.Util;

/**
 * Generic Player information used on DisplaySigns and books.
 * @author bitWolfy
 *
 */
public class PlayerTotals {
    
    /**
     * <b>Default Constructor</b><br />
     * Sets up the default values for the data holder.
     */
    public PlayerTotals(int playerId) {
        this.playerId = playerId;
        
        sessionStart = Util.getTimestamp();
        currentSession = 0;
        totalPlaytime = 0;
        
        blocksBroken = 0;
        blocksPlaced = 0;
        
        distWalked = 0;
        distBoated = 0;
        distMinecarted = 0;
        distPiggybacked = 0;
        distSwam = 0;
        distTotal = 0;
        
        toolsBroken = 0;
        itemsCrafted = 0;
        snacksEaten = 0;
        
        pvpKills = 0;
        pvpDeaths = 0;
        kdr = 1;
        pveKills = 0;
        otherKills = 0;
        
        fetchData();
    }
    
    private int playerId;
    
    private long sessionStart;
    private long currentSession;
    private long totalPlaytime;
    
    private int blocksBroken;
    private int blocksPlaced;
    
    private double distWalked;
    private double distBoated;
    private double distMinecarted;
    private double distPiggybacked;
    private double distSwam;
    private double distTotal;
    
    private int toolsBroken;
    private int itemsCrafted;
    private int snacksEaten;
    
    private int pvpKills;
    private int pvpDeaths;
    private double kdr;
    private int pveKills;
    private int otherKills;
    
    /**
     * Fetches the data from the remote database.<br />
     * Automatically calculates values from the contents of corresponding tables.
     */
    public void fetchData() {
        try {   // XXX
            sessionStart = Query.table(PlayersTable.TableName).column(PlayersTable.SessionStart).condition(PlayersTable.PlayerId, playerId).select().asLong(PlayersTable.SessionStart);
        } catch (NullPointerException ex) { sessionStart = Util.getTimestamp(); }
        currentSession = Util.getTimestamp() - sessionStart;
        totalPlaytime = Query.table(PlayersTable.TableName).column(PlayersTable.TotalPlaytime).condition(PlayersTable.PlayerId, playerId).select().asLong(PlayersTable.TotalPlaytime);
        
        blocksBroken = (int) Query.table(TotalBlocksTable.TableName).column(TotalBlocksTable.Destroyed).condition(TotalBlocksTable.PlayerId, playerId).sum();
        blocksPlaced = (int) Query.table(TotalBlocksTable.TableName).column(TotalBlocksTable.Placed).condition(TotalBlocksTable.PlayerId, playerId).sum();
        
        distWalked = Query.table(DistancePlayersTable.TableName).column(DistancePlayersTable.Foot).condition(DistancePlayersTable.PlayerId, playerId).sum();
        distBoated = Query.table(DistancePlayersTable.TableName).column(DistancePlayersTable.Boat).condition(DistancePlayersTable.PlayerId, playerId).sum();
        distMinecarted = Query.table(DistancePlayersTable.TableName).column(DistancePlayersTable.Minecart).condition(DistancePlayersTable.PlayerId, playerId).sum();
        distPiggybacked = Query.table(DistancePlayersTable.TableName).column(DistancePlayersTable.Pig).condition(DistancePlayersTable.PlayerId, playerId).sum();
        distSwam = Query.table(DistancePlayersTable.TableName).column(DistancePlayersTable.Swimmed).condition(DistancePlayersTable.PlayerId, playerId).sum();
        distTotal = distWalked + distBoated + distMinecarted + distPiggybacked + distSwam;
        
        toolsBroken = (int) Query.table(TotalItemsTable.TableName).column(TotalItemsTable.Broken).condition(TotalItemsTable.PlayerId, playerId).sum();
        itemsCrafted = (int) Query.table(TotalItemsTable.TableName).column(TotalItemsTable.Crafted).condition(TotalItemsTable.PlayerId, playerId).sum();
        snacksEaten = (int) Query.table(TotalItemsTable.TableName).column(TotalItemsTable.Used).condition(TotalItemsTable.PlayerId, playerId).sum();
        
        pvpKills = (int) Query.table(TotalPVPKillsTable.TableName).column(TotalPVPKillsTable.Times).condition(TotalPVPKillsTable.PlayerId, playerId).sum();
        pvpDeaths = (int) Query.table(TotalPVPKillsTable.TableName).column(TotalPVPKillsTable.Times).condition(TotalPVPKillsTable.VictimId, playerId).sum();
        if(pvpDeaths != 0) kdr = (double) Math.round((pvpKills / pvpDeaths) * 100000) / 100000;
        else kdr = pvpKills;
        
        pveKills = (int) Query.table(TotalPVEKillsTable.TableName).column(TotalPVEKillsTable.CreatureKilled).condition(TotalPVEKillsTable.PlayerId, playerId).sum();
        otherKills = (int) Query.table(TotalDeathPlayersTable.TableName).column(TotalDeathPlayersTable.Times).condition(TotalDeathPlayersTable.PlayerId, playerId).sum();
    }
    
    /**
     * Bundles up the values into one Map for ease of access.
     * @return Map of values
     */
    public Map<String, Object> getValues() {
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("currentSession", Util.parseTimestamp(currentSession));
        values.put("totalPlaytime", Util.parseTimestamp(totalPlaytime));
        
        values.put("blocksBroken", blocksBroken);
        values.put("blocksPlaced", blocksPlaced);
        
        values.put("distWalked", distWalked);
        values.put("distBoated", distBoated);
        values.put("distMinecarted", distMinecarted);
        values.put("distPiggybacked", distPiggybacked);
        values.put("distSwam", distSwam);
        values.put("distTotal", distTotal);
        
        values.put("toolsBroken", toolsBroken);
        values.put("itemsCrafted", itemsCrafted);
        values.put("snacksEaten", snacksEaten);
        
        values.put("pvpKills", pvpKills);
        values.put("pvpDeaths", pvpDeaths);
        values.put("kdr", kdr);
        values.put("pveKills", pveKills);
        values.put("otherKills", otherKills);
        return values;
    }
}
