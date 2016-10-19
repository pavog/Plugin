/*
 * Statistics Bukkit Plugin
 *
 * V2 Copyright (c) 2016 Paul <pavog> Vogel <http://www.paulvogel.me> and contributors.
 * V1 Copyright (c) 2016 bitWolfy <http://www.wolvencraft.com> and contributors.
 * Contributors are: Mario <MarioG1> Gallaun, Christian <Dazzl> Swan, Cory <Coryf88> Finnestad, Crimsonfoxy
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.wolvencraft.yasp.util.serializable;

import com.wolvencraft.yasp.util.Util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <b>Requires WorldGuardHook</b><br />
 * Stores the regional data in a Json array
 *
 * @author bitWolfy
 */
public class RegionsSerializable {

    String region_name;
    int priority;

    private RegionsSerializable(String regionName, int priority) {
        this.region_name = regionName;
        this.priority = priority;
    }

    /**
     * Compresses a Map into a single-line json array.<br />
     * Wraps around <code>Util.toJsonArray(List&lt;?&gt; source);</code><br />
     * Stores region name and priority
     *
     * @param regions Map of regions
     * @return String json array
     */
    public static String serialize(Map<String, Integer> regions) {
        List<RegionsSerializable> values = new LinkedList<RegionsSerializable>();
        Iterator<Entry<String, Integer>> it = regions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>) it.next();
            values.add(new RegionsSerializable(pairs.getKey(), pairs.getValue()));
            it.remove();
        }
        return Util.toJsonArray(values);
    }

}
