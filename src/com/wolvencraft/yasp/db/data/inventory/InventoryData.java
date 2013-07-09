/*
 * InventoryData.java
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

package com.wolvencraft.yasp.db.data.inventory;

import com.wolvencraft.yasp.db.data.ConfigLock;
import com.wolvencraft.yasp.db.data.DataStore;
import com.wolvencraft.yasp.db.data.DetailedData;
import com.wolvencraft.yasp.session.OnlineSession;

/**
 * Data store that handles player inventory
 * @author bitWolfy
 *
 */
public class InventoryData extends DataStore<TotalInventoryStats, DetailedData> {
    
    public static ConfigLock lock = new ConfigLock(Type.Distance.getAlias());
    
    public InventoryData(OnlineSession session) {
        super(session, Type.Inventory);
        getNormalData().add(new TotalInventoryStats(session.getId(), session.getBukkitPlayer()));
    }
    
    @Override
    public boolean onDataSync() {
        return lock.isEnabled();
    }
    
}
