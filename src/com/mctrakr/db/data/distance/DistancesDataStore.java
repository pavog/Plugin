/*
 * DistancesDataStore.java
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

package com.mctrakr.db.data.distance;

import com.mctrakr.db.data.DetailedData;
import com.mctrakr.db.data.LargeDataStore;
import com.mctrakr.db.data.distance.Tables.DistancesTable;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock;
import com.mctrakr.settings.ConfigLock.PrimaryType;

/**
 * Data store that handles player travel distances
 * @author bitWolfy
 *
 */
public class DistancesDataStore extends LargeDataStore<DistancesTotalStats, DetailedData> {
    
    public static ConfigLock lock = new ConfigLock(PrimaryType.Distance);
    
    public DistancesDataStore(OnlineSession session) {
        super(session, PrimaryType.Distance);
        addNormalDataEntry(new DistancesTotalStats(session.getId()));
    }
    
    @Override
    public ConfigLock getLock() {
        return lock;
    }
    
    /**
     * Increments the distance of the specified type by the amount
     * @param type Travel type
     * @param distance Distance travelled
     */
    public void playerTravel(DistancesTable type, double distance) {
        getNormalData().get(0).addDistance(type, distance);
    }
    
}
