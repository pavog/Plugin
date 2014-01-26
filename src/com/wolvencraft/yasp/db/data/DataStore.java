/*
 * AdvancedDataStore.java
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

package com.wolvencraft.yasp.db.data;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;

import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.util.Message;

/**
 * Common interface for all data stores
 * @author bitWolfy
 *
 */
public abstract class DataStore<N extends NormalData, D extends DetailedData> {
    
    @Getter(AccessLevel.PUBLIC) private DataStoreType type;
    @Getter(AccessLevel.PUBLIC) protected OnlineSession session;
    
    protected List<N> normalData;
    protected List<D> detailedData;
    
    public DataStore(OnlineSession session, DataStoreType type) {
        this.session = session;
        this.type = type;
        this.normalData = new ArrayList<N>();
        this.detailedData = new ArrayList<D>();
    }
    
    /**
     * Returns the dynamic entries in the data store.<br />
     * Asynchronous method; changes to the returned List will not affect the data store.
     * @return Dynamic entries in the data store
     */
    public List<N> getNormalData() {
        return new ArrayList<N>(normalData);
    }
    
    /**
     * Returns the static entries in the data store.
     * @return Static entries in the data store
     */
    public List<D> getDetailedData() {
        return new ArrayList<D>(detailedData);
    }
    
    /**
     * Synchronizes the data from the data store to the database, then removes it from local storage<br />
     * If an entry was not synchronized, it will not be removed.
     */
    public void pushData() {
        for(N entry : getNormalData()) {
           try{
                 if(((NormalData) entry).pushData(session.getId())) normalData.remove(entry);
           } catch(NullPointerException e ){
                 Message.debug("NPE occurred while saving NormalData: "+ ((NormalData) entry));
                 normalData.remove(entry);
           }
        }   
        for(D entry : getDetailedData()) {
            try{
                 if(((DetailedData) entry).pushData(session.getId())) detailedData.remove(entry);
            } catch(NullPointerException e ){
                 Message.debug("NPE occurred while saving DetailedData: "+ ((DetailedData) entry));
                 detailedData.remove(entry);
            }
        }
    }
    
    /**
     * Clears the data store of all locally stored data.
     */
    public void dump() {
        for(N entry : getNormalData()) {
            normalData.remove(entry);
        }
        
        for(D entry : getDetailedData()) {
            detailedData.remove(entry);
        }
    }
    
    /**
     * Represents the data store type
     * @author bitWolfy
     *
     */
    public enum DataStoreType {
        Blocks,
        Items,
        Deaths,
        PVE,
        PVP,
        
        Hook_AdminCmd,
        Hook_BanHammer,
        Hook_CommandBook,
        Hook_Factions,
        Hook_Jail,
        Hook_McMMO,
        Hook_MobArena,
        Hook_PvpArena,
        Hook_Towny,
        Hook_Vanish,
        Hook_Vault,
        Hook_Votifier,
        Hook_WorldGuard;
    }

}
