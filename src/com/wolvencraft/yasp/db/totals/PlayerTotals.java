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

package com.wolvencraft.yasp.db.totals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.tables.Normal.DistancePlayersTable;
import com.wolvencraft.yasp.db.tables.Normal.PlayersTable;
import com.wolvencraft.yasp.db.tables.Normal.TotalBlocksTable;
import com.wolvencraft.yasp.db.tables.Normal.TotalDeathPlayersTable;
import com.wolvencraft.yasp.db.tables.Normal.TotalItemsTable;
import com.wolvencraft.yasp.db.tables.Normal.TotalPVEKillsTable;
import com.wolvencraft.yasp.db.tables.Normal.TotalPVPKillsTable;
import com.wolvencraft.yasp.util.NamedInteger;
import com.wolvencraft.yasp.util.Util;
import com.wolvencraft.yasp.util.tasks.DatabaseTask;

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
        pveKills = 0;
        deaths = 0;
        kdr = 1;
        
        fetchData();
    }
    
    private int playerId;
    
    private long sessionStart;
    private long totalPlaytime;
    
    private int blocksBroken;
    private int blocksPlaced;
    
    private double distWalked;
    private double distBoated;
    private double distMinecarted;
    private double distPiggybacked;
    private double distSwam;
    private double distFlight;
    private double distTotal;
    
    private int toolsBroken;
    private int itemsCrafted;
    private int snacksEaten;
    
    private int pvpKills;
    private int pveKills;
    private int deaths;
    private double kdr;
    
    /**
     * Fetches the data from the remote database.<br />
     * Automatically calculates values from the contents of corresponding tables.
     */
    public void fetchData() {
        
        if(!Statistics.getInstance().isEnabled()) return;
        
        Bukkit.getScheduler().runTaskAsynchronously(Statistics.getInstance(), new Runnable() {
            
            @Override
            public void run() {
                try {   // XXX
                    sessionStart = Query.table(PlayersTable.TableName).column(PlayersTable.LoginTime).condition(PlayersTable.PlayerId, playerId).select().asLong(PlayersTable.LoginTime);
                } catch (NullPointerException ex) { sessionStart = Util.getTimestamp(); }
                
                totalPlaytime = Query.table(PlayersTable.TableName).column(PlayersTable.Playtime).condition(PlayersTable.PlayerId, playerId).select().asLong(PlayersTable.Playtime);
                
                blocksBroken = (int) Query.table(TotalBlocksTable.TableName).column(TotalBlocksTable.Destroyed).condition(TotalBlocksTable.PlayerId, playerId).sum();
                blocksPlaced = (int) Query.table(TotalBlocksTable.TableName).column(TotalBlocksTable.Placed).condition(TotalBlocksTable.PlayerId, playerId).sum();
                
                distWalked = Query.table(DistancePlayersTable.TableName).column(DistancePlayersTable.Foot).condition(DistancePlayersTable.PlayerId, playerId).sum();
                distBoated = Query.table(DistancePlayersTable.TableName).column(DistancePlayersTable.Boat).condition(DistancePlayersTable.PlayerId, playerId).sum();
                distMinecarted = Query.table(DistancePlayersTable.TableName).column(DistancePlayersTable.Minecart).condition(DistancePlayersTable.PlayerId, playerId).sum();
                distPiggybacked = Query.table(DistancePlayersTable.TableName).column(DistancePlayersTable.Pig).condition(DistancePlayersTable.PlayerId, playerId).sum();
                distSwam = Query.table(DistancePlayersTable.TableName).column(DistancePlayersTable.Swim).condition(DistancePlayersTable.PlayerId, playerId).sum();
                distFlight = Query.table(DistancePlayersTable.TableName).column(DistancePlayersTable.Flight).condition(DistancePlayersTable.PlayerId, playerId).sum();
                distTotal = distWalked + distBoated + distMinecarted + distPiggybacked + distSwam;
                
                toolsBroken = (int) Query.table(TotalItemsTable.TableName).column(TotalItemsTable.Broken).condition(TotalItemsTable.PlayerId, playerId).sum();
                itemsCrafted = (int) Query.table(TotalItemsTable.TableName).column(TotalItemsTable.Crafted).condition(TotalItemsTable.PlayerId, playerId).sum();
                snacksEaten = (int) Query.table(TotalItemsTable.TableName).column(TotalItemsTable.Used).condition(TotalItemsTable.PlayerId, playerId).sum();
                
                pvpKills = (int) Query.table(TotalPVPKillsTable.TableName).column(TotalPVPKillsTable.Times).condition(TotalPVPKillsTable.PlayerId, playerId).sum();
                pveKills = (int) Query.table(TotalPVEKillsTable.TableName).column(TotalPVEKillsTable.CreatureKilled).condition(TotalPVEKillsTable.PlayerId, playerId).sum();
                int pvpDeaths = (int) Query.table(TotalPVPKillsTable.TableName).column(TotalPVPKillsTable.Times).condition(TotalPVPKillsTable.VictimId, playerId).sum();
                int otherDeaths = (int) Query.table(TotalDeathPlayersTable.TableName).column(TotalDeathPlayersTable.Times).condition(TotalDeathPlayersTable.PlayerId, playerId).sum();
                
                deaths = pvpDeaths + otherDeaths;
                if(deaths != 0) kdr = (double) Math.round((pvpKills / deaths) * 100000) / 100000;
                else kdr = pvpKills;
            }
            
        });
    }
    
    /**
     * Bundles up the values into one Map for ease of access.
     * @return Map of values
     */
    public Map<String, Object> getValues() {
        if(deaths != 0) kdr = (double) Math.round((pvpKills / deaths) * 100000) / 100000;
        else kdr = pvpKills;
        
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("currentSession", Util.parseTimestamp(Util.getTimestamp() - sessionStart));
        values.put("rawCurrentSession", (Util.getTimestamp() - sessionStart));
        values.put("totalPlaytime", Util.parseTimestamp(totalPlaytime));
        values.put("rawTotalPlaytime", totalPlaytime);
        
        values.put("blocksBroken", blocksBroken);
        values.put("blocksPlaced", blocksPlaced);
        
        values.put("distWalked", distWalked);
        values.put("distBoated", distBoated);
        values.put("distMinecarted", distMinecarted);
        values.put("distPiggybacked", distPiggybacked);
        values.put("distSwam", distSwam);
        values.put("distFlight", distFlight);
        values.put("distTotal", distTotal);
        
        values.put("toolsBroken", toolsBroken);
        values.put("itemsCrafted", itemsCrafted);
        values.put("snacksEaten", snacksEaten);
        
        values.put("pvpKills", pvpKills);
        values.put("pveKills", pveKills);
        values.put("deaths", deaths);
        values.put("kdr", kdr);
        return values;
    }
    
    /**
     * Bundles up the Named values into one Map for ease of access.
     * @return Map of named values
     */
    public List<NamedInteger> getNamedValues() {
        List<NamedInteger> values = new LinkedList<NamedInteger>();
        values.add(getBlocksBroken());
        values.add(getBlocksPlaced());
        values.add(getCurrentSession());
        values.add(getTotalPlaytime());
        values.add(getDeaths());
        values.add(getPVPKills());
        values.add(getPVEKills());
        values.add(getDistance());
        return values;
    }
    
    /**
     * Returns the length of the current session.<br />
     * This method is intended to be used with a Scoreboard.
     * @return Current session lenght
     */
    public NamedInteger getCurrentSession() {
        long currentSession = Util.getTimestamp() - sessionStart;
        NamedInteger value = new NamedInteger();
        if(currentSession < 60) {
            value.setData (ChatColor.GREEN + "Online (sec)", (int) (currentSession));
        } else if(currentSession < 3600) {
            value.setData (ChatColor.GREEN + "Online (min)", (int) (currentSession / 60));
        } else {
            value.setData (ChatColor.GREEN + "Online (hours)", (int) (currentSession / 3600));
        }
        value.setPossibleNames(ChatColor.GREEN + "Online (sec)", ChatColor.GREEN + "Online (min)", ChatColor.GREEN + "Online (hours)");
        return value;
    }
    
    /**
     * Returns the total playtime.<br />
     * This method is intended to be used with a Scoreboard.
     * @return Total playtime
     */
    public NamedInteger getTotalPlaytime() {
        NamedInteger value = new NamedInteger();
        if(totalPlaytime < 60) {
            value.setData (ChatColor.GREEN + "Playtime (sec)", (int) (totalPlaytime));
        } else if(totalPlaytime < 3600) {
            value.setData (ChatColor.GREEN + "Playtime (min)", (int) (totalPlaytime / 60));
        } else {
            value.setData (ChatColor.GREEN + "Playtime (hrs)", (int) (totalPlaytime / 3600));
        }
        value.setPossibleNames(ChatColor.GREEN + "Playtime (sec)", ChatColor.GREEN + "Playtime (min)", ChatColor.GREEN + "Playtime (hrs)");
        return value;
    }
    
    /**
     * Returns the number of blocks the player has broken.<br />
     * This method is intended to be used with a Scoreboard.
     * @return Number of blocks
     */
    public NamedInteger getBlocksBroken() {
        NamedInteger value = new NamedInteger();
        if(blocksBroken < 100000) {
            value.setData (ChatColor.GOLD + "Broken", blocksBroken);
        } else {
            value.setData (ChatColor.GOLD + "Broken (k)", (int) (blocksBroken / 1000));
        }
        value.setPossibleNames(ChatColor.GOLD + "Broken", ChatColor.GOLD + "Broken (k)");
        return value;
    }
    
    /**
     * Returns the number of blocks the player has placed.<br />
     * This method is intended to be used with a Scoreboard.
     * @return Number of blocks
     */
    public NamedInteger getBlocksPlaced() {
        NamedInteger value = new NamedInteger();
        if(blocksPlaced < 100000) {
            value.setData (ChatColor.GOLD + "Placed", blocksPlaced);
        } else {
            value.setData (ChatColor.GOLD + "Placed (k)", (int) (blocksPlaced / 1000));
        }
        value.setPossibleNames(ChatColor.GOLD + "Placed", ChatColor.GOLD + "Placed (k)");
        return value;
    }
    
    /**
     * Returns the total distance the player has traveled.<br />
     * This method is intended to be used with a Scoreboard.
     * @return Distance traveled
     */
    public NamedInteger getDistance() {
        NamedInteger value = new NamedInteger();
        if(distTotal < 1000) {
            value.setData (ChatColor.BLUE + "Traveled (m)", (int) (distTotal));
        } else {
            value.setData (ChatColor.BLUE + "Traveled (km)", (int) (distTotal / 1000));
        }
        value.setPossibleNames(ChatColor.BLUE + "Traveled (m)", ChatColor.BLUE + "Traveled (km)");
        return value;
    }
    
    /**
     * Returns the total number of PVP kills.<br />
     * This method is intended to be used with a Scoreboard.
     * @return Number of kills
     */
    public NamedInteger getPVPKills() {
        return new NamedInteger (ChatColor.RED + "PVP Kills", pvpKills);
    }
    
    /**
     * Returns the total number of PVE kills.<br />
     * This method is intended to be used with a Scoreboard.
     * @return Number of kills
     */
    public NamedInteger getPVEKills() {
        return new NamedInteger (ChatColor.RED + "PVE Kills", pveKills);
    }
    
    /**
     * Returns the number of deaths.<br />
     * This method is intended to be used with a Scoreboard.
     * @return Number of deaths
     */
    public NamedInteger getDeaths() {
        return new NamedInteger (ChatColor.RED + "Deaths", deaths);
    }
    
    /**
     * Registers a block being broken
     */
    public void blockBreak() {
        blocksBroken++;
        DatabaseTask.getTotals().blockBreak();
    }
    
    /**
     * Registers a block being places
     */
    public void blockPlace() {
        blocksPlaced++;
        DatabaseTask.getTotals().blockPlace();
    }
    
    /**
     * Increases the distance traveled by different means
     * @param type Travel type
     * @param distance Distance traveled
     */
    public void addDistance(DistancePlayersTable type, double distance) {
        DatabaseTask.getTotals().addDistance(type, distance);
        distTotal += distance;
        switch(type) {
            case Foot:
                distWalked += distance;
                break;
            case Swim:
                distSwam += distance;
                break;
            case Flight:
                distFlight += distance;
                break;
            case Boat:
                distBoated += distance;
                break;
            case Minecart:
                distMinecarted += distance;
                break;
            case Pig:
                distPiggybacked += distance;
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
        DatabaseTask.getTotals().toolBreak();
    }
    
    /**
     * Registers an item being crafted
     */
    public void itemCraft() {
        itemsCrafted++;
        DatabaseTask.getTotals().itemCraft();
    }
    
    /**
     * Registers a food item being eaten
     */
    public void snacksEaten() {
        snacksEaten++;
        DatabaseTask.getTotals().snacksEaten();
    }
    
    /**
     * Registers a player being killed in PvP
     */
    public void pvpKill() {
        pvpKills++;
        DatabaseTask.getTotals().pvpKill();
    }
    
    /**
     * Registers the player dying
     */
    public void death() {
        deaths++;
        DatabaseTask.getTotals().death();
    }
    
    /**
     * Registers a player killing a mob
     */
    public void pveKill() {
        pveKills++;
        DatabaseTask.getTotals().pveKill();
    }
    
}
