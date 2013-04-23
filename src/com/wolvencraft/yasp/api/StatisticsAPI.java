/*
 * StatisticsAPI.java
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

package com.wolvencraft.yasp.api;

import com.wolvencraft.yasp.session.DataSession;
import com.wolvencraft.yasp.util.tasks.DatabaseTask;

/**
 * Simple API for servers statistics
 * @author bitWolfy
 *
 */
public class StatisticsAPI {
    
    /**
     * Returns the DataSession for the player with the specified username.<br />
     * The player might not be online, or not exist at all.
     * @param username Player's username
     * @return DataSession with player's totals
     */
    public static DataSession getPlayer(String username) {
        return new DataSession(username);
    }
    
    /**
     * Returns the total number of blocks broken on the server
     * @return Number of blocks broken
     */
    public static int getBlocksBroken() {
        return (Integer) DatabaseTask.getTotals().getValues().get("blBroken");
    }
    
    /**
     * Returns the total number of blocks placed on the server
     * @return Number of blocks placed
     */
    public static int getBlocksPlaced() {
        return (Integer) DatabaseTask.getTotals().getValues().get("blPlaced");
    }
    
    /**
     * Returns the total number of tools broken on the server
     * @return Number of tools broken
     */
    public static int getToolsBroken() {
        return (Integer) DatabaseTask.getTotals().getValues().get("itBroken");
    }
    
    /**
     * Returns the total number of items crafted on the server
     * @return Number of items crafted
     */
    public static int getItemsCrafted() {
        return (Integer) DatabaseTask.getTotals().getValues().get("itCrafted");
    }
    
    /**
     * Returns the total number of food items eaten
     * @return Number of food items eaten
     */
    public static int getFoodEaten() {
        return (Integer) DatabaseTask.getTotals().getValues().get("itEaten");
    }
    
    /**
     * Returns the total number of times one player killed another player
     * @return Number of PVP kills
     */
    public static int getPVPKills() {
        return (Integer) DatabaseTask.getTotals().getValues().get("pvpKills");
    }
    
    /**
     * Returns the total number of times a player killed a mob
     * @return Number of PVE kills
     */
    public static int getPVEKills() {
        return (Integer) DatabaseTask.getTotals().getValues().get("pveKills");
    }
    
    /**
     * Returns the total number of times players died
     * @return Total number of deaths
     */
    public static int getDeaths() {
        return (Integer) DatabaseTask.getTotals().getValues().get("deaths");
    }
    
    /**
     * Returns the total distance traveled (by any means)
     * @return Total distance traveled
     */
    public static double getDistanceTotal() {
        return (Double) DatabaseTask.getTotals().getValues().get("distTotal");
    }
    
    /**
     * Returns the total distance traveled by pig
     * @return Total distance traveled
     */
    public static double getDistancePig() {
        return (Double) DatabaseTask.getTotals().getValues().get("distPig");
    }
    
    /**
     * Returns the total distance traveled via minecart
     * @return Total distance traveled
     */
    public static double getDistanceMinecart() {
        return (Double) DatabaseTask.getTotals().getValues().get("distCart");
    }
    
    /**
     * Returns the total distance traveled via boat
     * @return Total distance traveled
     */
    public static double getDistanceBoat() {
        return (Double) DatabaseTask.getTotals().getValues().get("distBoat");
    }
    
    /**
     * Returns the total distance traveled by flight
     * @return Total distance traveled
     */
    public static double getDistanceFlight() {
        return (Double) DatabaseTask.getTotals().getValues().get("distFlight");
    }
    
    /**
     * Returns the total distance swam
     * @return Total distance traveled
     */
    public static double getDistanceSwam() {
        return (Double) DatabaseTask.getTotals().getValues().get("distSwim");
    }
    
}
