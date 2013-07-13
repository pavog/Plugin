/*
 * LargeDataStore.java
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

package com.mctrakr.modules;

import java.util.ArrayList;
import java.util.List;

import com.mctrakr.modules.DataStore.DetailedData;
import com.mctrakr.modules.DataStore.NormalData;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock.ModuleType;

public abstract class LargeDataStore<N extends NormalData, D extends DetailedData> extends DataStore {
    
    private List<N> normalData;
    private List<D> detailedData;
    
    public LargeDataStore(OnlineSession session, ModuleType type) {
        super(session, type);
        
        normalData = new ArrayList<N>();
        detailedData = new ArrayList<D>();
    }
    
    /**
     * Returns the dynamic entries in the data store.<br />
     * Asynchronous method; changes to the returned List will not affect the data store.
     * @return Dynamic entries in the data store
     */
    public final List<N> getNormalData() {
        return new ArrayList<N>(normalData);
    }
    
    /**
     * Adds a new entry to the normal datastore
     * @param entry Entry to add
     */
    public final void addNormalDataEntry(N entry) {
        normalData.add(entry);
    }
    
    /**
     * Returns the detailed entries in the data store.
     * @return Detailed entries in the data store
     */
    public final List<D> getDetailedData() {
        return new ArrayList<D>(detailedData);
    }
    
    /**
     * Adds a new entry to the detailed datastore
     * @param entry Entry to add
     */
    public final void addDetailedDataEntry(D entry) {
        detailedData.add(entry);
    }
    
    @Override
    public final void pushData() {
        if(getLock() != null && !getLock().isEnabled()) return;
        
        for(N entry : getNormalData()) {
            entry.pushData();
        }
        
        for(D entry : getDetailedData()) {
            if(entry.pushData()) detailedData.remove(entry);
        }
    }
    
    @Override
    public final void dump() {
        for(N entry : getNormalData()) {
            normalData.remove(entry);
        }
        
        for(D entry : getDetailedData()) {
            detailedData.remove(entry);
        }
    }
    
}
