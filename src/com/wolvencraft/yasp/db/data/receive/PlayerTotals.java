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
        currentSession = Util.getTimestamp() - Query.table(PlayersTable.TableName.toString()).column(PlayersTable.SessionStart.toString()).condition(PlayersTable.PlayerId.toString(), playerId).select().getValueAsLong(PlayersTable.SessionStart.toString());
        totalPlaytime = Query.table(PlayersTable.TableName.toString()).column(PlayersTable.TotalPlaytime.toString()).condition(PlayersTable.PlayerId.toString(), playerId).select().getValueAsLong(PlayersTable.TotalPlaytime.toString());
        
        blocksBroken = (int) Query.table(TotalBlocksTable.TableName.toString()).column(TotalBlocksTable.Destroyed.toString()).condition(TotalBlocksTable.PlayerId.toString(), playerId).sum();
        blocksPlaced = (int) Query.table(TotalBlocksTable.TableName.toString()).column(TotalBlocksTable.Placed.toString()).condition(TotalBlocksTable.PlayerId.toString(), playerId).sum();
        
        distWalked = Query.table(DistancePlayersTable.TableName.toString()).column(DistancePlayersTable.Foot.toString()).condition(DistancePlayersTable.PlayerId.toString(), playerId).sum();
        distBoated = Query.table(DistancePlayersTable.TableName.toString()).column(DistancePlayersTable.Boat.toString()).condition(DistancePlayersTable.PlayerId.toString(), playerId).sum();
        distMinecarted = Query.table(DistancePlayersTable.TableName.toString()).column(DistancePlayersTable.Minecart.toString()).condition(DistancePlayersTable.PlayerId.toString(), playerId).sum();
        distPiggybacked = Query.table(DistancePlayersTable.TableName.toString()).column(DistancePlayersTable.Pig.toString()).condition(DistancePlayersTable.PlayerId.toString(), playerId).sum();
        distSwam = Query.table(DistancePlayersTable.TableName.toString()).column(DistancePlayersTable.Swimmed.toString()).condition(DistancePlayersTable.PlayerId.toString(), playerId).sum();
        distTotal = distWalked + distBoated + distMinecarted + distPiggybacked + distSwam;
        
        toolsBroken = (int) Query.table(TotalItemsTable.TableName.toString()).column(TotalItemsTable.Broken.toString()).condition(TotalItemsTable.PlayerId.toString(), playerId + "").sum();
        itemsCrafted = (int) Query.table(TotalItemsTable.TableName.toString()).column(TotalItemsTable.Crafted.toString()).condition(TotalItemsTable.PlayerId.toString(), playerId + "").sum();
        snacksEaten = (int) Query.table(TotalItemsTable.TableName.toString()).column(TotalItemsTable.Used.toString()).condition(TotalItemsTable.PlayerId.toString(), playerId + "").sum();
        
        pvpKills = (int) Query.table(TotalPVPKillsTable.TableName.toString()).column(TotalPVPKillsTable.Times.toString()).condition(TotalPVPKillsTable.PlayerId.toString(), playerId + "").sum();
        pvpDeaths = (int) Query.table(TotalPVPKillsTable.TableName.toString()).column(TotalPVPKillsTable.Times.toString()).condition(TotalPVPKillsTable.VictimId.toString(), playerId + "").sum();
        if(pvpDeaths != 0) kdr = (double) Math.round((pvpKills / pvpDeaths) * 100000) / 100000;
        else kdr = pvpKills;
        
        pveKills = (int) Query.table(TotalPVEKillsTable.TableName.toString()).column(TotalPVEKillsTable.CreatureKilled.toString()).condition(TotalPVEKillsTable.PlayerId.toString(), playerId + "").sum();
        otherKills = (int) Query.table(TotalDeathPlayersTable.TableName.toString()).column(TotalDeathPlayersTable.Times.toString()).condition(TotalDeathPlayersTable.PlayerId.toString(), playerId + "").sum();
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
