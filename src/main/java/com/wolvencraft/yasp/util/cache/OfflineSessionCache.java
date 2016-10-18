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

import com.wolvencraft.yasp.session.OfflineSession;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.cache.CachedData.CachedDataProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Caches Offline sessions server-side
 * @author bitWolfy
 *
 */
public class OfflineSessionCache implements CachedDataProcess {

    private static List<OfflineSession> sessions;
    private final long REFRESH_RATE_TICKS = (long) (24 * 3600 * 20);
    
    /**
     * <b>Default constructor</b><br />
     * Creates an list of Offline sessions for storage
     */
    public OfflineSessionCache() {
        sessions = new ArrayList<OfflineSession>();
    }
    
    /**
     * Fetches the OfflineSession from the cache
     * @param username Player name
     * @return Offline session
     */
    public static OfflineSession fetch(UUID uuid) {
        for(OfflineSession session : sessions) {
            if(session.getUUID().equals(uuid)) return session;
        }
        Message.debug("Creating a new offline Session.");
        OfflineSession session = new OfflineSession(uuid);
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

    @Override
    public long getRefreshRate() {
        return REFRESH_RATE_TICKS;
    }

    @Override
    public void run() {
        for (OfflineSession session : new ArrayList<OfflineSession>(sessions)) {
            if (!session.isOnline()) sessions.remove(session);
        }
    }
}
