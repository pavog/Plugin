/*
 * TotalItemsEntry.java
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

package com.wolvencraft.yasp.db.data.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.data.NormalData;
import com.wolvencraft.yasp.db.tables.Normal.ItemTotals;
import com.wolvencraft.yasp.settings.Constants.ItemsWithMetadata;
import com.wolvencraft.yasp.settings.RemoteConfiguration;
import com.wolvencraft.yasp.util.cache.MaterialCache;

/**
 * Represents the total number of items player dropped and picked up.<br />
 * Each entry must have a unique player - material ID combination.
 * @author bitWolfy
 *
 */
public class TotalItemStats extends NormalData {
    
    private ItemStack stack;
    private int dropped;
    private int pickedUp;
    private int used;
    private int crafted;
    private int broken;
    private int smelted;
    private int enchanted;
    private int repaired;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new TotalItemsEntry based on the data provided
     * @param stack Item stack
     */
    public TotalItemStats(int playerId, ItemStack stack) {
        this.stack = stack.clone();
        this.stack.setAmount(1);
        
        dropped = 0;
        pickedUp = 0;
        used = 0;
        crafted = 0;
        broken = 0;
        smelted = 0;
        enchanted = 0;
        repaired = 0;
        
        fetchData(playerId);
    }
    
    @Override
    public void fetchData(int playerId) {
        if(RemoteConfiguration.MergedDataTracking.asBoolean()) {
            clearData(playerId);
            return;
        }
        
        QueryResult result = Query.table(ItemTotals.TableName)
                .column(ItemTotals.Dropped)
                .column(ItemTotals.PickedUp)
                .column(ItemTotals.Used)
                .column(ItemTotals.Crafted)
                .column(ItemTotals.Broken)
                .column(ItemTotals.Smelted)
                .column(ItemTotals.Enchanted)
                .column(ItemTotals.Repaired)
                .condition(ItemTotals.PlayerId, playerId)
                .condition(ItemTotals.MaterialId, MaterialCache.parse(stack))
                .select();
        
        if(result == null) {
            Query.table(ItemTotals.TableName)
                .value(ItemTotals.PlayerId, playerId)
                .value(ItemTotals.MaterialId, MaterialCache.parse(stack))
                .value(ItemTotals.Dropped, dropped)
                .value(ItemTotals.PickedUp, pickedUp)
                .value(ItemTotals.Used, used)
                .value(ItemTotals.Crafted, crafted)
                .value(ItemTotals.Broken, broken)
                .value(ItemTotals.Smelted, smelted)
                .value(ItemTotals.Enchanted, enchanted)
                .value(ItemTotals.Repaired, repaired)
                .insert();
        } else {
            dropped = result.asInt(ItemTotals.Dropped);
            pickedUp = result.asInt(ItemTotals.PickedUp);
            used = result.asInt(ItemTotals.Used);
            crafted = result.asInt(ItemTotals.Crafted);
            broken = result.asInt(ItemTotals.Broken);
            smelted = result.asInt(ItemTotals.Smelted);
            enchanted = result.asInt(ItemTotals.Enchanted);
            repaired = result.asInt(ItemTotals.Repaired);
        }
    }

    @Override
    public boolean pushData(int playerId) {
        boolean result = Query.table(ItemTotals.TableName)
                .value(ItemTotals.Dropped, dropped)
                .value(ItemTotals.PickedUp, pickedUp)
                .value(ItemTotals.Used, used)
                .value(ItemTotals.Crafted, crafted)
                .value(ItemTotals.Broken, broken)
                .value(ItemTotals.Smelted, smelted)
                .value(ItemTotals.Enchanted, enchanted)
                .value(ItemTotals.Repaired, repaired)
                .condition(ItemTotals.PlayerId, playerId)
                .condition(ItemTotals.MaterialId, MaterialCache.parse(stack))
                .update(RemoteConfiguration.MergedDataTracking.asBoolean());
        fetchData(playerId);
        return result;
    }
    
    @Override
    public void clearData(int playerId) {
        dropped = 0;
        pickedUp = 0;
        used = 0;
        crafted = 0;
        broken = 0;
        smelted = 0;
        enchanted = 0;
        repaired = 0;
    }
    
    /**
     * Checks if the ItemStack corresponds to this entry 
     * @param stack ItemStack to check
     * @return b>true</b> if the data matches, <b>false</b> otherwise.
     */
    public boolean equals(ItemStack stack) {
        if(stack.getType().equals(Material.POTION)) {
            return stack.getType().equals(this.stack.getType()) && stack.getDurability() == this.stack.getDurability();
        } else if(ItemsWithMetadata.contains(stack.getTypeId())) {
            return stack.getType().equals(this.stack.getType()) && stack.getData().getData() == this.stack.getData().getData();
        } else {
            return stack.getType().equals(this.stack.getType());
        }
    }
    
    /**
     * Increments the number of items dropped
     * @param amount Number of items
     */
    public void addDropped(int amount) {
        dropped += amount;
    }
    
    /**
     * Increments the number of items picked up
     * @param amount Number of items
     */
    public void addPickedUp(int amount) {
        pickedUp += amount;
    }
    
    /**
     * Increments the number of items used.<br />
     * Currently only tracks food consumption
     * @param amount Number of items
     */
    public void addUsed(int amount) {
        used += amount;
    }
    
    /**
     * Increments the number of items crafted
     * @param amount Number of items
     */
    public void addCrafted(int amount) {
        crafted += amount;
    }
    
    /**
     * Increments the number of tools broken
     * @param amount Number of items
     */
    public void addBroken(int amount) {
        broken += amount;
    }
    
    /**
     * Increments the number of items smelted
     * @param amount Number of items
     */
    public void addSmelted(int amount) {
        smelted += amount;
    }
    
    /**
     * Increments the number of items enchanted
     * @param amount Number of items
     */
    public void addEnchanted(int amount) {
        enchanted += amount;
    }
    
    /**
     * Increments the number of items repaired
     * @param amount Number of items
     */
    public void addRepaired(int amount) {
        
    }
}
