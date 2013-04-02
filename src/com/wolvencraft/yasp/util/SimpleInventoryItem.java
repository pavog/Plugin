/*
 * SimpleInventoryItem.java
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

package com.wolvencraft.yasp.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

/**
 * Simple class intended to temporarily store basic information about inventory items
 * @author bitWolfy
 *
 */
@SuppressWarnings("unused")
public class SimpleInventoryItem {
    
    private String material;
    private int amount;
    private int durability;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new SimpleInventoryItem based on an ItemStack provided
     * @param ItemStack stack
     */
    private SimpleInventoryItem(ItemStack stack) {
        this.durability = stack.getData().getData();
        this.material = Util.getBlockString(stack);
        this.amount = stack.getAmount();
    }
    
    /**
     * Compresses a List into a single-line json array.<br />
     * Wraps around <code>Util.toJsonArray(List&lt;?&gt; source);</code><br />
     * Stores material, amount, and durability of an itemstack
     * @param inventoryRow inventory row to compress
     * @return String json array
     */
    public static String toJsonArray(List<ItemStack> inventoryRow) {
        List<SimpleInventoryItem> invRow = new ArrayList<SimpleInventoryItem>();
        for(ItemStack stack : inventoryRow) {
            if(stack == null) { stack = new ItemStack(Material.AIR); }
            invRow.add(new SimpleInventoryItem(stack));
        }
        return Util.toJsonArray(invRow);
    }
}
