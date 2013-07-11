/* 
 * CachedManager.java
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

package com.mctrakr.managers;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;

import com.google.common.collect.Lists;
import com.mctrakr.cache.CachedData;
import com.mctrakr.cache.EntityCache;
import com.mctrakr.cache.MaterialCache;
import com.mctrakr.cache.SessionCache;
import com.mctrakr.util.ExceptionHandler;

/**
 * Handles different data caches
 * @author bitWolfy
 *
 */
public class CacheManager {
    
    private static List<CachedData> cache = Lists.newArrayList();
    
    public CacheManager() {
        cache.clear();
        for(Type type : Type.values()) cache.add(type.getInstance());
    }
    
    /**
     * Starts up all stopped tasks
     * @return <b>true</b> if all tasks were started up successfully, <b>false</b> otherwise
     */
    public static boolean startAll() {
        boolean result = true;
        for(CachedData cacheType : cache)
            result = result && cacheType.startTask();
        return result;
    }
    
    /**
     * Starts up all started tasks
     * @return <b>true</b> if all tasks were stopped up successfully, <b>false</b> otherwise
     */
    public static boolean stopAll() {
        boolean result = true;
        for(CachedData cacheType : cache)
            result = result && cacheType.stopTask();
        return result;
    }
    
    /**
     * Clears all the existing data cache
     */
    public static void clearCache() {
        for(CachedData cacheType : cache) cacheType.clearCache();
    }
    
    @Getter(AccessLevel.PUBLIC)
    public enum Type {
        
        ENTITY          (EntityCache.class),
        MATERIAL        (MaterialCache.class),
        SESSION         (SessionCache.class),
        ;
        
        private Class<? extends CachedData> processClass;
        
        Type(Class<? extends CachedData> processClass) {
            this.processClass = processClass;
        }
        
        public CachedData getInstance() {
            CachedData process;
            try { process = (CachedData) processClass.newInstance(); }
            catch (Throwable t) { ExceptionHandler.handle(t); return null; }
            return process;
        }
        
    }
    
}
