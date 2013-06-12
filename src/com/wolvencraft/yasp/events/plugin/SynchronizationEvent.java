/*
 * SynchronizationEvent.java
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

package com.wolvencraft.yasp.events.plugin;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import com.wolvencraft.yasp.events.StatisticsEvent;

/**
 * Called when the database synchronization is starting
 * @author bitWolfy
 *
 */
@Getter(AccessLevel.PUBLIC)
public class SynchronizationEvent extends StatisticsEvent implements Cancellable {
    
    private static final HandlerList handlers = new HandlerList();
    private int processId;
    @Setter(AccessLevel.PUBLIC)
    private boolean cancelled;
    
    public SynchronizationEvent(int processId) {
        this.processId = processId;
        this.cancelled = false;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
