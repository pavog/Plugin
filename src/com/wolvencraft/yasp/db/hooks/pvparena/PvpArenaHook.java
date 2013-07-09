/*
 * PvpArenaHook.java
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

package com.wolvencraft.yasp.db.hooks.pvparena;

import net.slipcor.pvparena.api.PVPArenaAPI;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.hooks.PluginHook;

public class PvpArenaHook extends PluginHook {
    
    private static final String PLUGIN_NAME = "PvpArena";
    
    public PvpArenaHook() {
        super(PvpArenaDataStore.lock, PLUGIN_NAME);
    }
    
    /**
     * Checks if a player is participating in any of the arenas
     * @param player Player object
     * @return <b>true</b> if a player is in the arena, <b>false</b> otherwise
     */
    public boolean isPlaying(Player player) {
        return (PVPArenaAPI.getArenaName(player) == null);
    }
    
    /**
     * Returns the name of the arena the player is currently in
     * @param player Player object
     * @return <b>String</b> name of the arena, or <b>null</b> if the player is not participating
     */
    public String getArenaName(Player player) {
        return PVPArenaAPI.getArenaName(player);
    }
}
