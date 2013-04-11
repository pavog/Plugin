/*
 * AwardmentReward.java
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

/**
 * Represents a requirement that the player needs to fulfill in order to get an awardment
 * @author bitWolfy
 *
 */
public class AwardmentRequirement {
    
    RequirementType type;
    String contents;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new AwardmentRequirement based on the parameters provided
     * @param type Requirement type
     * @param contents Requirement contents
     */
    public AwardmentRequirement(RequirementType type, String contents) {
        this.type = type;
        this.contents = contents;
    }
    
    /**
     * Returns the requirement type
     * @return Requirement type
     */
    public RequirementType getType() {
        return type;
    }
    
    /**
     * Represents the different types of requirements
     * @author bitWolfy
     *
     */
    public enum RequirementType {
        
        BlockBreak,
        BlockPlace,
        ItemPickup,
        ItemDrop,
        ItemUse,
        ItemEnchant,
        ItemEat,
        ItemCraft,
        ItemBreak,
        PVPKill,
        PVPDeath,
        PVEKill,
        PVEDeath,
        NaturalDeath,
        Login,
        TotalPlaytime,
        TimesKicked,
        FishCaught,
        BedsEntered,
        PortalsEntered,
        WordsSaid,
        CommandsSent,
        KillStreak;
        
    }
    
}
