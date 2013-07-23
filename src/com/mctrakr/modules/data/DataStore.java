/*
 * DataStore.java
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

package com.mctrakr.modules.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.mctrakr.modules.Module;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock;
import com.mctrakr.settings.ConfigLock.ModuleType;

@Getter(AccessLevel.PUBLIC)
@AllArgsConstructor(access=AccessLevel.PUBLIC)
public abstract class DataStore implements Module {
    
    protected OnlineSession session;
    private ModuleType type;
    
    @Override
    public ConfigLock getLock() {
        return null;
    }
    
    /**
     * Synchronizes the data from the data store to the database, then removes it from local storage<br />
     * If an entry was not synchronized, it will not be removed.
     */
    public abstract void pushData();
    
    /**
     * Clears the data store of all locally stored data.
     */
    public abstract void dump();
    
}
