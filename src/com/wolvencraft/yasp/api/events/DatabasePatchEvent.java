/*
 * DatabasePatchEvent.java
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

/**
 * Called when the plugin is being patched by the database
 * @author bitWolfy
 *
 */
public class DatabasePatchEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String name;
    
    public DatabasePatchEvent(String name) {
        this.name = name;
    }
    
    /**
     * Returns the patch file name
     * @return Patch file name
     */
    public String getPatchFileName() {
        return name + ".sql";
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
