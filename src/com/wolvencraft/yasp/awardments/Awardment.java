/*
 * Awardment.java
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

package com.wolvencraft.yasp.awardments;

import java.util.List;

import com.wolvencraft.yasp.Statistics;

/**
 * Represents an individual Awardment
 * @author bitWolfy
 *
 */
public class Awardment {
    
    public String name;
    public List<AwardmentReward> rewards;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new Awardment based on the arguments provided
     * @param name Awardment name
     * @param rewardArray Json array of rewards
     */
    public Awardment(String name, String rewardArray) {
        this.name = name;
        Statistics.getGson().fromJson(rewardArray, AwardmentReward.class);
    }
    
    /**
     * Returns the name of the awardment
     * @return Awardment name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the list of rewards for achieving a goal
     * @return List of rewards
     */
    public List<AwardmentReward> getRewards() {
        return rewards;
    }
    
    /**
     * Checks if the specified player has been given a reward already
     * @param playerName Player name
     * @return <b>true</b> if the player has already been awarded, <b>false</b> otherwise
     */
    public boolean wasAwarded(String playerName) {
        
        return false;
    }
    
}
