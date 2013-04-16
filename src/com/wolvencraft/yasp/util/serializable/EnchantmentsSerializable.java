/*
 * EnchantmentsSerializable.java
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.craftbukkit.libs.com.google.gson.JsonArray;
import org.bukkit.enchantments.Enchantment;

import com.wolvencraft.yasp.util.Util;

/**
 * Provides means to serialize a <code>Map&lt;Enchantment, Integer&gt;</code> into a Json array
 * @author bitWolfy
 *
 */
@SuppressWarnings("unused")
public class EnchantmentsSerializable {
    
    private int enchantment_id;
    private int enchantment_level;
    
    /**
     * <b>Default constructor</b>
     * @param enchantment Enchantment type
     * @param level Enchantment level
     */
    private EnchantmentsSerializable(Enchantment enchantment, int level) {
        enchantment_id = enchantment.getId();
        enchantment_level = level;
    }
    
    /**
     * Compresses a List into a single-line json array.<br />
     * Wraps around <code>Util.toJsonArray(List&lt;?&gt; source);</code><br />
     * Stores material, amount, and durability of an itemstack
     * @param inventoryRow inventory row to compress
     * @return String json array
     */
    public static List<EnchantmentsSerializable> serialize(Map<Enchantment, Integer> enchantments) {
        List<EnchantmentsSerializable> enchList = new ArrayList<EnchantmentsSerializable>();
        Iterator<Entry<Enchantment, Integer>> it = enchantments.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Enchantment, Integer> enchantment = (Map.Entry<Enchantment, Integer>)it.next();
            enchList.add(new EnchantmentsSerializable(enchantment.getKey(), enchantment.getValue().intValue()));
        }
        return enchList;
    }
    
}
