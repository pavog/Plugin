/*
 * FactionsDataStore.java
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

package com.mctrakr.db.hooks.factions;

import com.mctrakr.db.data.ConfigLock;
import com.mctrakr.db.data.DataStore;
import com.mctrakr.db.data.DetailedData;
import com.mctrakr.session.OnlineSession;

/**
 * Hooks into Factions to track its statistics
 * @author bitWolfy
 *
 */
public class FactionsDataStore extends DataStore<FactionsPlayerStats, DetailedData> {
    
    public static ConfigLock lock = new ConfigLock(HookType.Factions, true);
    
    public FactionsDataStore(OnlineSession session) {
        super(session, HookType.Factions);
    }
    
    @Override
    public ConfigLock getLock() {
        return lock;
    }
    
}
