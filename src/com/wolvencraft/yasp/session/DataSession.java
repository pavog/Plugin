/*
 * DataSession.java
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

package com.wolvencraft.yasp.session;

import org.bukkit.Bukkit;

import com.wolvencraft.yasp.db.totals.PlayerTotals;
import com.wolvencraft.yasp.util.cache.PlayerCache;

/**
 * A simplistic representation of a player's session.<br />
 * The player in question might be offline, or not exist at all.
 * @author bitWolfy
 *
 */
public class DataSession implements PlayerSession {
    
    private final int id;
    private final String name;
    
    private PlayerTotals playerTotals;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new player data session based on the username
     * @param name Player username
     */
    public DataSession(String name) {
        this.name = name;
        this.id = PlayerCache.get(name);
        this.playerTotals = new PlayerTotals(id);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isOnline() {
        if(Bukkit.getPlayerExact(name) == null) return false;
        return true;
    }
    
    /**
     * Returns the player's session length, in seconds
     * @return Player's session length
     */
    public long getSessionLength() {
        return (Long) playerTotals.getValues().get("rawCurrentSession");
    }
    
    /**
     * Returns the player's total playtime, in seconds
     * @return Player's total playtime
     */
    public long getTotalPlaytime() {
        return (Long) playerTotals.getValues().get("rawTotalPlaytime");
    }
    
    /**
     * Returns the total number of blocks the player has broken
     * @return Number of blocks
     */
    public int getBlocksBroken() {
        return (Integer) playerTotals.getValues().get("blocksBroken");
    }
    
    /**
     * Returns the number of blocks the player has placed
     * @return Number of blocks
     */
    public int getBlocksPlaced() {
        return (Integer) playerTotals.getValues().get("blocksPlaced");
    }
    
    /**
     * Returns the number of tools the player has broken
     * @return Number of items
     */
    public int getToolsBroken() {
        return (Integer) playerTotals.getValues().get("toolsBroken");
    }
    
    /**
     * Returns the number of items the player has crafted
     * @return Number of items
     */
    public int getItemsCrafted() {
        return (Integer) playerTotals.getValues().get("itemsCrafted");
    }
    
    /**
     * Returns the number of items the player has eaten
     * @return Number of items
     */
    public int getFoodEaten() {
        return (Integer) playerTotals.getValues().get("snacksEaten");
    }
    
    /**
     * Returns the number of times the player killed other players
     * @return Number of kills
     */
    public int getPVPKills() {
        return (Integer) playerTotals.getValues().get("pvpKills");
    }
    
    /**
     * Returns the number of mobs the player killed
     * @return Number of kills
     */
    public int getPVEKills() {
        return (Integer) playerTotals.getValues().get("pveKills");
    }
    
    /**
     * Returns the number of times the player has died
     * @return Number of deaths
     */
    public int getDeaths() {
        return (Integer) playerTotals.getValues().get("deaths");
    }
    
    /**
     * Returns the player's kill-to-death ratio
     * @return Player's KDR
     */
    public double getKDR() {
        return (Double) playerTotals.getValues().get("kdr");
    }
    
    /**
     * Returns the total distance the player has traveled
     * @return Distance traveled
     */
    public double getDistanceTotal() {
        return (Double) playerTotals.getValues().get("distTotal");
    }
    
    /**
     * Returns the total distance the player has traveled on a pig
     * @return Distance traveled
     */
    public double getDistancePig() {
        return (Double) playerTotals.getValues().get("distPiggybacked");
    }
    
    /**
     * Returns the total distance the player has traveled in a minecart
     * @return Distance traveled
     */
    public double getDistanceMinecart() {
        return (Double) playerTotals.getValues().get("distMinecarted");
    }
    
    /**
     * Returns the total distance the player has traveled in a boat
     * @return Distance traveled
     */
    public double getDistanceBoat() {
        return (Double) playerTotals.getValues().get("distBoated");
    }
    
    /**
     * Returns the total distance the player has traveled by flight
     * @return Distance traveled
     */
    public double getDistanceFlight() {
        return (Double) playerTotals.getValues().get("distFlight");
    }
    
    /**
     * Returns the total distance the player has swam
     * @return Distance traveled
     */
    public double getDistanceSwam() {
        return (Double) playerTotals.getValues().get("distSwam");
    }

}
