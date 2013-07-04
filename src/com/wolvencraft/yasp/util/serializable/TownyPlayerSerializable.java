/*
 * TownyPlayerSerializable.java
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

import java.util.List;

import com.palmergames.bukkit.towny.object.Resident;
import com.wolvencraft.yasp.Statistics;

@SuppressWarnings("unused")
public class TownyPlayerSerializable {
    
    private List<String> town_ranks;
    private List<String> nation_ranks;
    
    private String title;
    private String surname;
    
    private int town_blocks_count;
    
    private TownyPlayerSerializable(Resident resident) {
        title = resident.getTitle();
        surname = resident.getSurname();
        
        town_ranks = resident.getTownRanks();
        nation_ranks = resident.getNationRanks();
        town_blocks_count = resident.getTownBlocks().size();
    }
    
    public static String serialize(Resident resident) {
        return Statistics.getGson().toJson(new TownyPlayerSerializable(resident));
    }
    
}
