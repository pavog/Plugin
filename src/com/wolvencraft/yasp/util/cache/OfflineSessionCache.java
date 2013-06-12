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
    private static List<OfflineSession> sessions;
    
    /**
     * <b>Default constructor</b><br />
     * Creates an list of Offline sessions for storage
     */
    public OfflineSessionCache() {
        sessions = new ArrayList<OfflineSession>();
    }
    
    @Override
    public long getRefreshRate() {
        return REFRESH_RATE_TICKS;
    }
    
    @Override
    public void run() {
        for(OfflineSession session : new ArrayList<OfflineSession>(sessions)) {
            if(!session.isOnline()) sessions.remove(session);
        }
    }
    
    /**
     * Fetches the OfflineSession from the cache
     * @param username Player name
     * @return Offline session
     */
    public static OfflineSession fetch(String username) {
        for(OfflineSession session : sessions) {
            if(session.getName().equals(username)) return session;
        }
        OfflineSession session = new OfflineSession(username);
        sessions.add(session);
        return session;
    }
    
    /**
     * Returns all stored sessions.
     * @return List of stored player sessions
     */
    public static List<OfflineSession> getSessions() {
        return new ArrayList<OfflineSession>(sessions);
    }
}
