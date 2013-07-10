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

package com.mctrakr.db.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.mctrakr.session.OnlineSession;

@Getter(AccessLevel.PUBLIC)
@AllArgsConstructor(access=AccessLevel.PUBLIC)
public abstract class DataStore {
    
    private OnlineSession session;
    private DataStoreType type;
    
    /**
     * Returns the configuration lock
     * @return Config lock, or <b>null</b> if there isn't one
     */
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
    
    /**
     * Generic data store type.
     * @author bitWolfy
     *
     */
    public static interface DataStoreType {
        
        /**
         * Returns the type alias
         * @return Data store alias
         */
        public String getAlias();
        
    }
    
    /**
     * Represents different types of built-in modules.
     * Exists for convenience purposes only
     * @author bitWolfy
     *
     */
    @Getter(AccessLevel.PUBLIC)
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public enum ModuleType implements DataStoreType {
        
        Blocks          ("blocks"),
        Deaths          ("deaths"),
        Distance        ("distance"),
        Inventory       ("inventory"),
        Items           ("items"),
        Misc            ("misc"),
        Player          ("player"),
        PVE             ("pve"),
        PVP             ("pvp"),
        ;
        
        private String alias;
    }
    
    /**
     * Represents different types of hook modules.
     * Exists for convenience purposes only
     * @author bitWolfy
     *
     */
    @Getter(AccessLevel.PUBLIC)
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public enum HookType implements DataStoreType {

        AdminCmd        ("admincmd"),
        BanHammer       ("banhammer"),
        CommandBook     ("commandbook"),
        Factions        ("factions"),
        Jail            ("jail"),
        McMMO           ("mcmmo"),
        MobArena        ("mobarena"),
        PvpArena        ("pvparena"),
        Towny           ("towny"),
        Vanish          ("vanish"),
        Vault           ("vault"),
        Votifier        ("votifier"),
        WorldGuard      ("worldguard"),
        ;
        
        private String alias;
    }

}
