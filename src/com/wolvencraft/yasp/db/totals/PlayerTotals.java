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

import org.bukkit.ChatColor;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.tables.Normal.PlayerDistance;
import com.wolvencraft.yasp.db.tables.Normal.PlayerStats;
import com.wolvencraft.yasp.db.tables.Normal.BlockTotals;
import com.wolvencraft.yasp.db.tables.Normal.DeathTotals;
import com.wolvencraft.yasp.db.tables.Normal.ItemTotals;
import com.wolvencraft.yasp.db.tables.Normal.PVETotals;
import com.wolvencraft.yasp.db.tables.Normal.PVPTotals;
import com.wolvencraft.yasp.util.NamedInteger;
import com.wolvencraft.yasp.util.Util;
import com.wolvencraft.yasp.util.VariableManager.PlayerVariable;

/**
 * Generic Player information used on DisplaySigns and books.
 * @author bitWolfy
 *
 */
public class PlayerTotals {
    
    private int playerId;
    private Map<PlayerVariable, Object> values;
    
    /**
     * <b>Default Constructor</b><br />
     * Sets up the default values for the data holder.
     */
    public PlayerTotals(int playerId) {
        this.playerId = playerId;
        
        values = new HashMap<PlayerVariable, Object>();
        values.put(PlayerVariable.SESSION_START, Util.getTimestamp());
        values.put(PlayerVariable.KILL_DEATH_RATIO, 1);
        
        fetchData();
    }
    
    /**
     * Fetches the data from the remote database.<br />
     * Automatically calculates values from the contents of corresponding tables.
     */
    public void fetchData() {
        
        if(!Statistics.getInstance().isEnabled()) return;
        
        try { values.put(PlayerVariable.SESSION_START, Query.table(PlayerStats.TableName).column(PlayerStats.LoginTime).condition(PlayerStats.PlayerId, playerId).select().asLong(PlayerStats.LoginTime)); }
        catch (NullPointerException ex) { values.put(PlayerVariable.SESSION_START, Util.getTimestamp()); }
        
        long sessionStart = (Long) values.get(PlayerVariable.SESSION_START);
        long totalPlaytime = Query.table(PlayerStats.TableName).column(PlayerStats.Playtime).condition(PlayerStats.PlayerId, playerId).select().asLong(PlayerStats.Playtime);
        values.put(PlayerVariable.SESSION_LENGTH, Util.parseTimestamp(Util.getTimestamp() - sessionStart));
        values.put(PlayerVariable.SESSION_LENGTH_RAW, (Util.getTimestamp() - sessionStart));
        values.put(PlayerVariable.TOTAL_PLAYTIME, Util.parseTimestamp(totalPlaytime));
        values.put(PlayerVariable.TOTAL_PLAYTIME_RAW, totalPlaytime);
        
        values.put(PlayerVariable.BLOCKS_BROKEN, (int) Query.table(BlockTotals.TableName).column(BlockTotals.Destroyed).condition(BlockTotals.PlayerId, playerId).sum());
        values.put(PlayerVariable.BLOCKS_PLACED, (int) Query.table(BlockTotals.TableName).column(BlockTotals.Placed).condition(BlockTotals.PlayerId, playerId).sum());
        
        values.put(PlayerVariable.DISTANCE_FOOT, Query.table(PlayerDistance.TableName).column(PlayerDistance.Foot).condition(PlayerDistance.PlayerId, playerId).sum());
        values.put(PlayerVariable.DISTANCE_BOAT, Query.table(PlayerDistance.TableName).column(PlayerDistance.Boat).condition(PlayerDistance.PlayerId, playerId).sum());
        values.put(PlayerVariable.DISTANCE_CART, Query.table(PlayerDistance.TableName).column(PlayerDistance.Minecart).condition(PlayerDistance.PlayerId, playerId).sum());
        values.put(PlayerVariable.DISTANCE_RIDE, Query.table(PlayerDistance.TableName).column(PlayerDistance.Ride).condition(PlayerDistance.PlayerId, playerId).sum());
        values.put(PlayerVariable.DISTANCE_SWIM, Query.table(PlayerDistance.TableName).column(PlayerDistance.Swim).condition(PlayerDistance.PlayerId, playerId).sum());
        values.put(PlayerVariable.DISTANCE_FLIGHT, Query.table(PlayerDistance.TableName).column(PlayerDistance.Flight).condition(PlayerDistance.PlayerId, playerId).sum());
        
        double totalDistance = ((Double) values.get(PlayerVariable.DISTANCE_FOOT))
                + ((Double) values.get(PlayerVariable.DISTANCE_BOAT))
                + ((Double) values.get(PlayerVariable.DISTANCE_CART))
                + ((Double) values.get(PlayerVariable.DISTANCE_RIDE))
                + ((Double) values.get(PlayerVariable.DISTANCE_SWIM))
                + ((Double) values.get(PlayerVariable.DISTANCE_FLIGHT));
        values.put(PlayerVariable.DISTANCE_TRAVELED, totalDistance);
        
        values.put(PlayerVariable.ITEMS_BROKEN, (int) Query.table(ItemTotals.TableName).column(ItemTotals.Broken).condition(ItemTotals.PlayerId, playerId).sum());
        values.put(PlayerVariable.ITEMS_CRAFTED, (int) Query.table(ItemTotals.TableName).column(ItemTotals.Crafted).condition(ItemTotals.PlayerId, playerId).sum());
        values.put(PlayerVariable.ITEMS_EATEN, (int) Query.table(ItemTotals.TableName).column(ItemTotals.Used).condition(ItemTotals.PlayerId, playerId).sum());
        
        int pvpKills = (int) Query.table(PVPTotals.TableName).column(PVPTotals.Times).condition(PVPTotals.PlayerId, playerId).sum();
        values.put(PlayerVariable.PVP_KILLS, pvpKills);
        values.put(PlayerVariable.PVE_KILLS, (int) Query.table(PVETotals.TableName).column(PVETotals.CreatureKilled).condition(PVETotals.PlayerId, playerId).sum());
        
        int pvpDeaths = (int) Query.table(PVPTotals.TableName).column(PVPTotals.Times).condition(PVPTotals.VictimId, playerId).sum();
        int otherDeaths = (int) Query.table(DeathTotals.TableName).column(DeathTotals.Times).condition(DeathTotals.PlayerId, playerId).sum();
        int deaths = pvpDeaths + otherDeaths;
        values.put(PlayerVariable.DEATHS, deaths);
        
        double kdr = 1;
        if(deaths != 0) kdr = (double) Math.round((pvpKills / deaths) * 100000) / 100000;
        else kdr = pvpKills;
        values.put(PlayerVariable.KILL_DEATH_RATIO, kdr);
    }
    
    /**
     * Safely returns the value of the specified variable
     * @param type Variable to return
     * @return Variable value
     */
    public Object getValue(PlayerVariable type) {
        if(values.containsKey(type)) return values.get(type);
        values.put(type, 0);
        return 0;
    }
    
    /**
     * Safely increments the specified value by 1
     * @param type Value to increment
     */
    public void incrementValue(PlayerVariable type) {
        int value = 1;
        if(values.containsKey(type)) {
            Object valueObj = values.get(type);
            if(valueObj instanceof Double)
                value += ((Double) valueObj).doubleValue();
            else value += ((Integer) valueObj).doubleValue();
        }
        values.put(type, value);
    }
    
    /**
     * Safely increments the specified value by 1
     * @param type Value to increment
     */
    public void incrementValue(PlayerVariable type, double value) {
        if(values.containsKey(type)) {
            Object valueObj = values.get(type);
            if(valueObj instanceof Double)
                value += ((Double) valueObj).doubleValue();
            else value += ((Integer) valueObj).doubleValue();
        }
        values.put(type, value);
    }
    
    /**
     * Bundles up the Named values into one Map for ease of access.
     * @return Map of named values
     */
    @SuppressWarnings("serial")
    public List<NamedInteger> getNamedValues() {
        return new LinkedList<NamedInteger>()
                {{
                    add(getBlocksBroken());
                    add(getBlocksPlaced());
                    add(getCurrentSession());
                    add(getTotalPlaytime());
                    add(getDeaths());
                    add(getPVPKills());
                    add(getPVEKills());
                    add(getDistance());
                }};
    }
    
    /**
     * Returns the length of the current session.<br />
     * This method is intended to be used with a Scoreboard.
     * @return Current session lenght
     */
    public NamedInteger getCurrentSession() {
        long currentSession = Util.getTimestamp() - (Long) getValue(PlayerVariable.SESSION_START);
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
        long totalPlaytime = (Long) getValue(PlayerVariable.TOTAL_PLAYTIME);
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
        int blocksBroken = (Integer) getValue(PlayerVariable.BLOCKS_BROKEN);
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
        int blocksPlaced = (Integer) getValue(PlayerVariable.BLOCKS_PLACED);
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
        int distTotal = (Integer) getValue(PlayerVariable.DISTANCE_TRAVELED);
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
        return new NamedInteger (ChatColor.RED + "PVP Kills", (Integer) getValue(PlayerVariable.PVP_KILLS));
    }
    
    /**
     * Returns the total number of PVE kills.<br />
     * This method is intended to be used with a Scoreboard.
     * @return Number of kills
     */
    public NamedInteger getPVEKills() {
        return new NamedInteger (ChatColor.RED + "PVE Kills", (Integer) getValue(PlayerVariable.PVE_KILLS));
    }
    
    /**
     * Returns the number of deaths.<br />
     * This method is intended to be used with a Scoreboard.
     * @return Number of deaths
     */
    public NamedInteger getDeaths() {
        return new NamedInteger (ChatColor.RED + "Deaths", (Integer) getValue(PlayerVariable.DEATHS));
    }
    
    /**
     * Registers a block being broken
     */
    public void blockBreak() {
        incrementValue(PlayerVariable.BLOCKS_BROKEN);
        Statistics.getServerTotals().blockBreak();
    }
    
    /**
     * Registers a block being places
     */
    public void blockPlace() {
        incrementValue(PlayerVariable.BLOCKS_PLACED);
        Statistics.getServerTotals().blockPlace();
    }
    
    /**
     * Increases the distance traveled by different means
     * @param type Travel type
     * @param distance Distance traveled
     */
    public void addDistance(PlayerDistance type, double distance) {
        Statistics.getServerTotals().addDistance(type, distance);
        incrementValue(PlayerVariable.DISTANCE_TRAVELED, distance);
        switch(type) {
            case Foot:
                incrementValue(PlayerVariable.DISTANCE_FOOT, distance);
                break;
            case Swim:
                incrementValue(PlayerVariable.DISTANCE_SWIM, distance);
                break;
            case Flight:
                incrementValue(PlayerVariable.DISTANCE_FLIGHT, distance);
                break;
            case Boat:
                incrementValue(PlayerVariable.DISTANCE_BOAT, distance);
                break;
            case Minecart:
                incrementValue(PlayerVariable.DISTANCE_CART, distance);
                break;
            case Ride:
                incrementValue(PlayerVariable.DISTANCE_RIDE, distance);
                break;
            default:
                break;
        }
    }
    
    /**
     * Registers a tool being broken
     */
    public void toolBreak() {
        incrementValue(PlayerVariable.ITEMS_BROKEN);
        Statistics.getServerTotals().toolBreak();
    }
    
    /**
     * Registers an item being crafted
     */
    public void itemCraft() {
        incrementValue(PlayerVariable.ITEMS_CRAFTED);
        Statistics.getServerTotals().itemCraft();
    }
    
    /**
     * Registers a food item being eaten
     */
    public void snacksEaten() {
        incrementValue(PlayerVariable.ITEMS_EATEN);
        Statistics.getServerTotals().snacksEaten();
    }
    
    /**
     * Registers a player being killed in PvP
     */
    public void pvpKill() {
        double kdr = 1;
        int deaths = (Integer) values.get(PlayerVariable.DEATHS), pvpKills = (Integer) values.get(PlayerVariable.PVP_KILLS);
        if(deaths != 0) kdr = (double) Math.round((pvpKills / deaths) * 100000) / 100000;
        else kdr = pvpKills;
        values.put(PlayerVariable.KILL_DEATH_RATIO, kdr);
        
        incrementValue(PlayerVariable.PVP_KILLS);
        Statistics.getServerTotals().pvpKill();
    }
    
    /**
     * Registers the player dying
     */
    public void death() {
        double kdr = 1;
        int deaths = (Integer) values.get(PlayerVariable.DEATHS), pvpKills = (Integer) values.get(PlayerVariable.PVP_KILLS);
        if(deaths != 0) kdr = (double) Math.round((pvpKills / deaths) * 100000) / 100000;
        else kdr = pvpKills;
        values.put(PlayerVariable.KILL_DEATH_RATIO, kdr);
        
        incrementValue(PlayerVariable.DEATHS);
        Statistics.getServerTotals().death();
    }
    
    /**
     * Registers a player killing a mob
     */
    public void pveKill() {
        incrementValue(PlayerVariable.PVE_KILLS);
        Statistics.getServerTotals().pveKill();
    }
    
}
