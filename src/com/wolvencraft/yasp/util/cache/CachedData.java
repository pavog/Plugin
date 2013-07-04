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

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.managers.CacheManager.Type;

/**
 * Cached data handler
 * @author bitWolfy
 *
 */
public abstract class CachedData implements Runnable {
    
    public final long REFRESH_RATE_TICKS;
    public final Type TYPE;
    private BukkitTask process;
    
    public CachedData(Type type, long refreshRate) {
        TYPE = type;
        REFRESH_RATE_TICKS = refreshRate;
    }
    
    @Override
    public final void run() { clearCache(); }
    
    /**
     * Starts up the task associated with the data cache
     * @return <b>true</b> if the task was started, <b>false</b> otherwise
     */
    public boolean startTask() {
        if(process != null) return false;
        process = Bukkit.getScheduler().runTaskTimerAsynchronously(Statistics.getInstance(), this, 60 * 20L, REFRESH_RATE_TICKS);
        return true;
    }
    
    /**
     * Stops up the task associated with the data cache
     * @return <b>true</b> if the task was stopped, <b>false</b> otherwise
     */
    public boolean stopTask() {
        if(process == null) return false;
        process.cancel();
        process = null;
        return true;
    }
    
    /**
     * Clears the data cache of existing entries
     */
    public abstract void clearCache();
    
}
