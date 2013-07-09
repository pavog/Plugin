/*
 * MobArenaDataStore.java
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

package com.mctrakr.db.hooks.mobarena;

import com.mctrakr.db.data.ConfigLock;
import com.mctrakr.db.data.DataStore;
import com.mctrakr.db.data.DetailedData;
import com.mctrakr.session.OnlineSession;

public class MobArenaDataStore extends DataStore<MobArenaPlayerStats, DetailedData> {
    
    public static ConfigLock lock = new ConfigLock(HookType.MobArena, true);
    
    public MobArenaDataStore(OnlineSession session) {
        super(session, HookType.MobArena);
    }
    
    @Override
    public ConfigLock getLock() {
        return lock;
    }
}
