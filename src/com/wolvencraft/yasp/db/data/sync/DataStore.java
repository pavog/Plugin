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

package com.wolvencraft.yasp.db.data.sync;

import java.util.List;

/**
 * Common interface for all data stores
 * @author bitWolfy
 *
 */
public interface DataStore {
    
    /**
     * Returns the type of the data store
     * @return Data store type
     */
    public StoreType getType();
    
    /**
     * Returns the dynamic entries in the data store.<br />
     * Asynchronous method; changes to the returned List will not affect the data store.
     * @return Dynamic entries in the data store
     */
    public List<NormalData> getNormalData();
    
    /**
     * Returns the static entries in the data store.
     * @return Static entries in the data store
     */
    public List<DetailedData> getDetailedData();
    
    /**
     * Synchronizes the data from the data store to the database, then removes it from local storage<br />
     * If an entry was not synchronized, it will not be removed.
     */
    public void sync();
    
    /**
     * Clears the data store of all locally stored data.
     */
    public void dump();
    
    /**
     * Represents the "totals" of statistical data. These entries are changing every time their corresponding data type changes.<br />
     * No duplicate entries are allowed.
     * @author bitWolfy
     *
     */
    public interface NormalData {
        
        /**
         * Performs a database operation to fetch the data from the remote database.<br />
         * If no data is found in the database, the default values are inserted.
         * @param playerId Player ID
         */
        public void fetchData(int playerId);
        
        /**
         * Performs a database operation to push the local data to the remote database.<br />
         * If no data is found in the database, the default values are inserted instead.
         * @param playerId Player ID
         * @return <b>true</b> if the insertion was successful, <b>false</b> otherwise
         */
        public boolean pushData(int playerId);
    }
    
    
    /**
     * Represents data stored in a log format. New data is appended to the end of the table. No existing data can be changed.<br />
     * Multiple instances of this type could (and should) exist.
     * @author bitWolfy
     *
     */
    public interface DetailedData {
        
        /**
         * Explicitly pushes data to the remote database.<br />
         * If the data holder is marked as <i>on hold</i>, skips the holder
         * @param playerId Player ID
         * @return <b>true</b> if the holder has been synchronized and can be removed, <b>false</b> if it is on hold
         */
        public boolean pushData(int playerId);
    }
    
    /**
     * Common interface for data store type enums
     * @author bitWolfy
     *
     */
    public interface StoreType {}
    
    /**
     * Represents the data store type
     * @author bitWolfy
     *
     */
    public enum DataStoreType implements StoreType {
        Blocks,
        Items,
        Deaths,
        PVE,
        PVP,
        Vault;
    }

}
