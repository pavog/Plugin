/*
 * NationSerializable.java
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

import com.palmergames.bukkit.towny.object.Nation;
import com.wolvencraft.yasp.Statistics;

@SuppressWarnings("unused")
public class NationSerializable {
    
    private String name;
    private String tag;
    private String capital;
    
    private int num_towns;
    private int num_residents;
    private double taxes;
    
    private NationSerializable(Nation nation) {
        name = nation.getName();
        tag = nation.getTag();
        capital = nation.getCapital().getName();
        
        num_towns = nation.getNumTowns();
        num_residents = nation.getNumResidents();
        taxes = nation.getTaxes();
    }
    
    public static String serialize(Nation nation) {
        return Statistics.getGson().toJson(new NationSerializable(nation));
    }
    
}
