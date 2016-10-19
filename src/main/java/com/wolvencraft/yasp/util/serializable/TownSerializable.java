/*
 * TownSerializable.java
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

import com.palmergames.bukkit.towny.object.Town;
import com.wolvencraft.yasp.Statistics;

@SuppressWarnings("unused")
public class TownSerializable {

    private String name;
    private String tag;
    private String mayor;
    private int residents_count;
    private double taxes;
    private int town_blocks_count;
    private String board;

    private TownSerializable(Town town) {
        name = town.getName();
        tag = town.getTag();
        mayor = town.getMayor().getName();
        residents_count = town.getNumResidents();
        taxes = town.getTaxes();
        town_blocks_count = town.getTotalBlocks();
        board = town.getTownBoard();
    }

    public static String serialize(Town town) {
        return Statistics.getGson().toJson(new TownSerializable(town));
    }

}
