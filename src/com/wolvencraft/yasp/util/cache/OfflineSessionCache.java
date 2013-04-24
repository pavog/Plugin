/* 
 * OfflineSessionCache.java
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

import java.util.ArrayList;
import java.util.List;

import com.wolvencraft.yasp.session.OfflineSession;
import com.wolvencraft.yasp.util.cache.CachedData.CachedDataProcess;

/**
 * Caches Offline sessions server-side
 * @author bitWolfy
 *
 */
public class OfflineSessionCache implements CachedDataProcess {

    private final long REFRESH_RATE_TICKS = (long)(24 * 3600 * 20);
    private static List<OfflineSession> players;
    
    /**
     * <b>Default constructor</b><br />
     * Creates an list of Offline sessions for storage
     */
    public OfflineSessionCache() {
        players = new ArrayList<OfflineSession>();
    }
    
    @Override
    public long getRefreshRate() {
        return REFRESH_RATE_TICKS;
    }
    
    @Override
    public void run() {
        List<OfflineSession> temp = new ArrayList<OfflineSession>();
        for(OfflineSession session : players) {
            temp.add(session);
        }
        
        for(OfflineSession session : temp) {
            if(!session.isOnline()) players.remove(session);
        }
    }
    
    /**
     * Fetches the OfflineSession from the cache
     * @param username Player name
     * @return Offline session
     */
    public static OfflineSession fetch(String username) {
        for(OfflineSession session : players) {
            if(session.getName().equals(username)) return session;
        }
        OfflineSession session = new OfflineSession(username);
        players.add(session);
        return session;
    }
}
