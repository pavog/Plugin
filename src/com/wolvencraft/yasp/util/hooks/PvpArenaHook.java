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

package com.wolvencraft.yasp.util.hooks;

import net.slipcor.pvparena.PVPArena;
import net.slipcor.pvparena.api.PVPArenaAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.wolvencraft.yasp.HookManager.ApplicableHook;
import com.wolvencraft.yasp.settings.Module;

/**
 * Quick-and-dirty MobArena hook
 * @author bitWolfy
 *
 */
public class PvpArenaHook extends PluginHook {
    
    /**
     * <b>Default constructor</b><br />
     * Connects to MobArena and sets up a plugin instance
     */
    public PvpArenaHook() {
        super(ApplicableHook.PVP_ARENA);
    }
    
    /**
     * Checks if a player is participating in any of the arenas
     * @param playerName Name of the player
     * @return <b>true</b> if a player is in the arena, <b>false</b> otherwise
     */
    public boolean isPlaying(String playerName) {
        Player player = Bukkit.getServer().getPlayerExact(playerName);
        if(player == null) return false;
        return (PVPArenaAPI.getArenaName(player) == null);
    }
    
    /**
     * Returns the name of the arena the player is currently in
     * @param playerName Name of the player 
     * @return <b>String</b> name of the arena, or <b>null</b> if the player is not participating
     */
    public String getArenaName(String playerName) {
        Player player = Bukkit.getServer().getPlayerExact(playerName);
        if(player == null) return null;
        return PVPArenaAPI.getArenaName(player);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(type.getPluginName());
        
        if (plugin != null && plugin instanceof PVPArena) {
            Module.PvpArena.setActive(true);
        }
    }
}
