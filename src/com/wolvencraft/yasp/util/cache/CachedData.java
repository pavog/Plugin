/* 
 * CachedData.java
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

package com.wolvencraft.yasp.util.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.util.Message;

/**
 * Handles all the processes that refresh the plugin cache
 * @author bitWolfy
 *
 */
public class CachedData {
    
    private static Map<CachedDataType, Integer> tasks = new HashMap<CachedDataType, Integer>();
    
    /**
     * Starts the specified process, as long as it is not running
     * @param type Process to start
     * @return <b>true</b> if the process has been started, <b>false</b> if an error occurred
     */
    public static boolean startProcess(CachedDataType type) {
        if(tasks.containsKey(type)) return false;
        int processId = Bukkit.getScheduler().runTaskTimerAsynchronously(
                Statistics.getInstance(),
                type.process,
                0,
                type.process.getRefreshRate()
        ).getTaskId();
        tasks.put(type, processId);
        return true;
    }
    
    /**
     * Stops the specified process (if it is running)
     * @param type Process to stop
     * @return <b>true</b> if the process has been stopped, <b>false</b> if an error occurred
     */
    public static boolean stopProcess(CachedDataType type) {
        if(!tasks.containsKey(type)) return false;
        Bukkit.getScheduler().cancelTask(tasks.get(type));
        return true;
    }
    
    /**
     * Starts all processes, as long as they have not been started yet
     * @return <b>true</b> if all processes have been started, <b>false</b> if an error occurred
     */
    public static boolean startAll() {
        boolean result = true;
        for(CachedDataType type : CachedDataType.values()) {
            if(tasks.containsKey(type)) continue;
            result = result && startProcess(type);
        }
        return result;
    }
    
    /**
     * Stops all processes, as long as they are running
     * @return <b>true</b> if all processes have been stopped, <b>false</b> if an error occurred
     */
    public static boolean stopAll() {
        boolean result = true;
        for(CachedDataType type : CachedDataType.values()) {
            if(tasks.containsKey(type)) continue;
            result = result && stopProcess(type);
        }
        return result;
    }
    
    /**
     * Public interface for all cached data processes
     * @author bitWolfy
     *
     */
    public interface CachedDataProcess extends Runnable {
        
        /**
         * Returns the cache refresh rate.
         * @return Cache refresh rate (in ticks)
         */
        public long getRefreshRate();
        
    }
    
    /**
     * Denotes different cached data types
     * @author bitWolfy
     *
     */
    public enum CachedDataType {
        Entity(EntityCache.class),
        Material(MaterialCache.class),
        OfflineSession(OfflineSessionCache.class),
        OnlineSession(OnlineSessionCache.class);
        
        private CachedDataProcess process;
        
        CachedDataType(Class<?> process) {
            try { this.process = (CachedDataProcess) process.newInstance(); }
            catch (InstantiationException e)    { Message.log(Level.SEVERE, "Error while instantiating a cache process! (" + process.getSimpleName() + " InstantiationException)"); return; }
            catch (IllegalAccessException e)    { Message.log(Level.SEVERE, "Error while instantiating a cache process! (" + process.getSimpleName() + " IllegalAccessException)"); return; }
            catch (Exception e)                 { Message.log(Level.SEVERE, "Error while instantiating a cache process! (" + process.getSimpleName() + " " + e.getMessage() + ")"); return; }
        }
    }
    
}

