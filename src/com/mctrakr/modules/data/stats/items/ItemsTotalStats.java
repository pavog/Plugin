/*
 * ItemsTotalStats.java
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

package com.mctrakr.modules.data.stats.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.mctrakr.cache.MaterialCache;
import com.mctrakr.database.Query;
import com.mctrakr.database.Query.QueryResult;
import com.mctrakr.modules.data.NormalData;
import com.mctrakr.modules.data.stats.items.Tables.TotalItemsTable;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.Constants.ItemsWithMetadata;

/**
 * Represents the total number of items player dropped and picked up.<br />
 * Each entry must have a unique player - material ID combination.
 * @author bitWolfy
 *
 */
public class ItemsTotalStats extends NormalData {
    
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
    public ItemsTotalStats(OnlineSession session, ItemStack stack) {
        super(session);
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
        
        fetchData();
    }
    
    @Override
    public void fetchData() {
        QueryResult result = Query.table(TotalItemsTable.TableName)
                .column(TotalItemsTable.Dropped)
                .column(TotalItemsTable.PickedUp)
                .column(TotalItemsTable.Used)
                .column(TotalItemsTable.Crafted)
                .column(TotalItemsTable.Broken)
                .column(TotalItemsTable.Smelted)
                .column(TotalItemsTable.Enchanted)
                .column(TotalItemsTable.Repaired)
                .condition(TotalItemsTable.PlayerId, session.getId())
                .condition(TotalItemsTable.MaterialId, MaterialCache.parse(stack))
                .select();
        
        if(result == null) {
            Query.table(TotalItemsTable.TableName)
                .value(TotalItemsTable.PlayerId, session.getId())
                .value(TotalItemsTable.MaterialId, MaterialCache.parse(stack))
                .value(TotalItemsTable.Dropped, dropped)
                .value(TotalItemsTable.PickedUp, pickedUp)
                .value(TotalItemsTable.Used, used)
                .value(TotalItemsTable.Crafted, crafted)
                .value(TotalItemsTable.Broken, broken)
                .value(TotalItemsTable.Smelted, smelted)
                .value(TotalItemsTable.Enchanted, enchanted)
                .value(TotalItemsTable.Repaired, repaired)
                .insert();
        } else {
            dropped = result.asInt(TotalItemsTable.Dropped);
            pickedUp = result.asInt(TotalItemsTable.PickedUp);
            used = result.asInt(TotalItemsTable.Used);
            crafted = result.asInt(TotalItemsTable.Crafted);
            broken = result.asInt(TotalItemsTable.Broken);
            smelted = result.asInt(TotalItemsTable.Smelted);
            enchanted = result.asInt(TotalItemsTable.Enchanted);
            repaired = result.asInt(TotalItemsTable.Repaired);
        }
    }

    @Override
    public boolean pushData() {
        boolean result = Query.table(TotalItemsTable.TableName)
                .value(TotalItemsTable.Dropped, dropped)
                .value(TotalItemsTable.PickedUp, pickedUp)
                .value(TotalItemsTable.Used, used)
                .value(TotalItemsTable.Crafted, crafted)
                .value(TotalItemsTable.Broken, broken)
                .value(TotalItemsTable.Smelted, smelted)
                .value(TotalItemsTable.Enchanted, enchanted)
                .value(TotalItemsTable.Repaired, repaired)
                .condition(TotalItemsTable.PlayerId, session.getId())
                .condition(TotalItemsTable.MaterialId, MaterialCache.parse(stack))
                .update();
        fetchData();
        return result;
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
