/*
 * MobArenaHook.java
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

import org.bukkit.entity.Player;

import com.garbagemule.MobArena.MobArena;
import com.wolvencraft.yasp.settings.Module;

public class MobArenaHook extends PluginHook {
    
    private static MobArena instance;
    
    public MobArenaHook() {
        super(Module.MobArena, "MobArena");
    }
    
    @Override
    protected void onEnable() {
        instance = (MobArena) super.plugin;
    }
    
    @Override
    protected void onDisable() {
        instance = null;
    }
    
    /**
     * Checks if a player is participating in any of the arenas
     * @param player Player object
     * @return <b>true</b> if a player is in the arena, <b>false</b> otherwise
     */
    public static boolean isPlaying(Player player) {
        return instance.getArenaMaster().getAllPlayers().contains(player);
    }
    
    /**
     * Returns the name of the arena the player is currently in
     * @param player Player object
     * @return <b>String</b> name of the arena, or <b>null</b> if the player is not participating
     */
    public static String getArenaName(Player player) {
        return instance.getArenaMaster().getArenaWithPlayer(player).arenaName();
    }
}
