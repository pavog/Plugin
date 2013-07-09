/*
 * DistanceData.java
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

package com.wolvencraft.yasp.db.data.distance;

import com.wolvencraft.yasp.db.data.DataStore;
import com.wolvencraft.yasp.db.data.DetailedData;
import com.wolvencraft.yasp.db.data.distance.Tables.PlayerDistance;
import com.wolvencraft.yasp.session.OnlineSession;

/**
 * Data store that handles player travel distances
 * @author bitWolfy
 *
 */
public class DistanceData extends DataStore<TotalDistanceStats, DetailedData> {
    
    public DistanceData(OnlineSession session) {
        super(session, Type.Distance);
        normalData.add(new TotalDistanceStats(session.getId()));
    }
    
    /**
     * Increments the distance of the specified type by the amount
     * @param type Travel type
     * @param distance Distance travelled
     */
    public void playerTravel(PlayerDistance type, double distance) {
        TotalDistanceStats stats = normalData.get(0);
        stats.addDistance(type, distance);
    }
    
}
