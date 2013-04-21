/*
 * SynchronizationPreProcessEvent.java
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

package com.wolvencraft.yasp.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.wolvencraft.yasp.Statistics;

/**
 * Called when the plugin being synchronizing data to the database.<br />
 * The synchronization might never actually occur.
 * @author bitWolfy
 *
 */
public class SynchronizationPreProcessEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private int processId;
    
    public SynchronizationPreProcessEvent(int processId) {
        this.processId = processId;
    }
    
    /**
     * Returns the synchronization process ID
     * @return Process ID
     */
    public int getProcessId() {
        return processId;
    }
    
    /**
     * Checks if the database synchronization is cancelled
     * @return <b>true</b> if the synchronization was cancelled, <b>false</b> otherwise
     */
    public boolean isCancelled() {
        return Statistics.getPaused();
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
}
