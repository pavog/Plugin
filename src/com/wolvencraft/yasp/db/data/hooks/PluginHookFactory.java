/*
 * PluginHookFactory.java
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

/**
 * Common interface for plugin hook factories.
 * @author bitWolfy
 *
 */
public interface PluginHookFactory {
    
    /**
     * Code that is to be executed when the hook is being enabled.<br />
     * This should include a database patch, if necessary
     */
    public void onEnable();
    
    /**
     * Code that is to be executed when the hook is being disabled.<br />
     * This should include a cleanup routine.
     */
    public void onDisable();
    
    /**
     * A common interface for plugin hook data stores.
     * @author bitWolfy
     *
     */
    public interface PluginHook {
        
        /**
         * Fetches the data from the remote database.<br />
         * Might not actually do anything if the plugin sends data in a log format.
         */
        public void fetchData();
        
        /**
         * Pushes the data to the remote database.
         * @return <b>true</b> if the data was sent successfully, <b>false</b> if an error occurred
         */
        public boolean pushData();
    }
}
