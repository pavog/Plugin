/*
 * SessionRemoveEvent.java
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

package com.wolvencraft.yasp.events.session;

import com.wolvencraft.yasp.events.StatisticsEvent;
import com.wolvencraft.yasp.session.OfflineSession;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Called when the player session expires.<br />
 * This usually means that the player is offline
 *
 * @author bitWolfy
 */
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SessionRemoveEvent extends StatisticsEvent {

    private static final HandlerList handlers = new HandlerList();
    private UUID uuid;

    public OfflineSession getSession() {
        return new OfflineSession(uuid);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
