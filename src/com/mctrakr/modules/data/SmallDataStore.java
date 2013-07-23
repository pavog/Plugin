/*
 * SmallDataStore.java
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

import java.util.ArrayList;
import java.util.List;

import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock.ModuleType;

public abstract class SmallDataStore<N extends NormalData, D extends DetailedData> extends DataStore {
    
    private N normalData;
    private List<D> detailedData;
    
    public SmallDataStore(OnlineSession session, ModuleType type) {
        super(session, type);
        
        normalData = null;
        detailedData = new ArrayList<D>();
    }
    
    /**
     * Returns the normal data value
     * @return Normal data value
     */
    public final N getNormalData() {
        return normalData;
    }
    
    /**
     * Sets the normal data value
     * @param normalData New normal data value
     */
    public final void setNormalData(N normalData) {
        this.normalData = normalData;
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
        
        normalData.pushData();
        
        for(D entry : getDetailedData()) {
            if(entry.pushData()) detailedData.remove(entry);
        }
    }
    
    @Override
    public final void dump() {
        normalData = null;
        
        for(D entry : getDetailedData()) {
            detailedData.remove(entry);
        }
    }
    
}
