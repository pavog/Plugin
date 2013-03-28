/*
 * PluginHook.java
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

package com.wolvencraft.yasp.db.data.hooks;

import java.util.Map;

import org.bukkit.entity.Player;

/**
 * Common interface for plugin hook data stores
 * @author bitWolfy
 *
 */
public interface _PluginHook {
    
    /**
     * Patches the database to the latest version of the corresponding plugin
     * @return <b>true</b> if the database was brought to date, <b>false</b> if an error occurred
     */
    public boolean patch();
    
    /**
     * Makes sure that there are no loose ends. Breaks the connection to the parent plugin if need be.
     */
    public void cleanup();
    
    public interface PluginHookEntry {
        
        /**
         * Fetches the data from the remote database.<br />
         * Might not actually do anything if the plugin sends data in a log format.
         * @param player Player object
         */
        public void fetchData(Player player);
        
        /**
         * Pushes the data to the remote database.
         * @return <b>true</b> if the data was sent successfully, <b>false</b> if an error occurred
         */
        public boolean pushData();
        

        /**
         * Returns the data values of the DataHolder in a Map form
         * @return <b>Map</b> of column names and their corresponding values
         */
        public Map<String, Object> getValues();
    }
}
