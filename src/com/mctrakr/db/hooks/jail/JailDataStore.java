/*
 * JailDataStore.java
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

package com.mctrakr.db.hooks.jail;

import com.mctrakr.db.data.DetailedData;
import com.mctrakr.db.data.SmallDataStore;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock;
import com.mctrakr.settings.ConfigLock.HookType;

public class JailDataStore extends SmallDataStore<JailPlayerStats, DetailedData> {
    
    public static ConfigLock lock = new ConfigLock(HookType.Jail);
    
    public JailDataStore(OnlineSession session) {
        super(session, HookType.Jail);
        setNormalData(new JailPlayerStats(session));
    }
    
    @Override
    public ConfigLock getLock() {
        return lock;
    }
}
