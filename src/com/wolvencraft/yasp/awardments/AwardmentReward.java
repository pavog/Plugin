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

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.util.Util;

/**
 * Represents an award given to the player for achieving some kind of task
 * @author bitWolfy
 *
 */
public class AwardmentReward {
    
    RewardType type;
    String contents;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new AwardmentReward based on the arguments provided
     * @param type Reward type
     * @param contents Reward contents
     */
    public AwardmentReward(RewardType type, String contents) {
        this.type = type;
        this.contents = contents;
    }
    
    /**
     * Returns the reward type
     * @return Reward type
     */
    public RewardType getType() {
        return type;
    }
    
    /**
     * Returns the reward contents as an item stack
     * @return Item stack
     */
    public ItemStack toItemStack() {
        String[] parts = contents.split(":");
        if(parts.length == 1) return new ItemStack(Material.getMaterial(Integer.parseInt(parts[0])));
        return new ItemStack(Integer.parseInt(parts[0]), Byte.parseByte(parts[1]));
    }
    
    /**
     * Returns the reward contents as a colored message.
     * @return Message
     */
    public String toMessage() {
        return Util.parseChatColors(contents);
    }
    
    /**
     * Returns the reward contents as a permissions node
     * @return Permissions node
     */
    public String toPermission() {
        return contents;
    }
    
    /**
     * Represents the different types of rewards
     * @author bitWolfy
     *
     */
    public enum RewardType {
        
        Items,
        Permissions,
        Message;
        
    }
    
}
