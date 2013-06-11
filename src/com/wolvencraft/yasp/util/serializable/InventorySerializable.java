/*
 * InventorySerializable.java
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

package com.wolvencraft.yasp.util.serializable;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.util.ExceptionHandler;
import com.wolvencraft.yasp.util.Util;
import com.wolvencraft.yasp.util.cache.MaterialCache;

/**
 * Provides means to serialize a <code>List&lt;ItemStack&gt;</code> into a Json array
 * @author bitWolfy
 *
 */
@SuppressWarnings("unused")
public class InventorySerializable {
    
    private String material_id;
    private double durability;
    private int amount;
    private List<EnchantmentsSerializable> enchantments;
    
    /**
     * <b>Default constructor</b>
     * @param ItemStack Stack of items
     */
    private InventorySerializable(ItemStack stack) {
        material_id = MaterialCache.parse(stack);
        short curDurability = stack.getDurability();
        short maxDurability = stack.getType().getMaxDurability();
        if(curDurability <= 0 || maxDurability <= 0) durability = 0;
        else {
            durability = (double)(maxDurability - curDurability) / maxDurability;
            durability = ((int)(100 * durability)) / 100.0;
        }
        amount = stack.getAmount();
        enchantments = EnchantmentsSerializable.serialize(stack.getEnchantments());
    }
    
    /**
     * Compresses a List into a single-line json array.<br />
     * Wraps around <code>Util.toJsonArray(List&lt;?&gt; source);</code><br />
     * Stores material, amount, and durability of an itemstack
     * @param inventoryRow inventory row to compress
     * @return String json array
     */
    public static String serialize(List<ItemStack> inventoryRow) {
        List<InventorySerializable> invRow = new ArrayList<InventorySerializable>();
        for(ItemStack stack : inventoryRow) {
            try {
                if(stack == null) { stack = new ItemStack(Material.AIR); }
                invRow.add(new InventorySerializable(stack));
            } catch (Throwable t) {
                ExceptionHandler.handle(t, true);
                continue;
            }
        }
        return Util.toJsonArray(invRow);
    }
}
