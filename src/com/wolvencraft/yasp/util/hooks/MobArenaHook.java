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

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.garbagemule.MobArena.MobArena;
import com.wolvencraft.yasp.Settings;
import com.wolvencraft.yasp.Settings.LocalConfiguration;
import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.exceptions.DatabaseConnectionException;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.PatchFetcher;
import com.wolvencraft.yasp.util.PatchFetcher.PatchType;

/**
 * Quick-and-dirty MobArena hook
 * @author bitWolfy
 *
 */
public class MobArenaHook {
    
    private MobArena instance;
    
    /**
     * <b>Default constructor</b><br />
     * Connects to MobArena and sets up a plugin instance
     */
    public MobArenaHook() {

        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("MobArena");
        
        if (plugin != null && plugin instanceof MobArena) {
            instance = (MobArena) plugin;
            Message.log("MobArena hook enabled!");
            Settings.ActiveHooks.HookMobArena.setActive(true);
        }
    }
    
    /**
     * Checks if a player is participating in any of the arenas
     * @param playerName Name of the player
     * @return <b>true</b> if a player is in the arena, <b>false</b> otherwise
     */
    public boolean isPlaying(String playerName) {
        Player player = Bukkit.getServer().getPlayerExact(playerName);
        if(player == null) return false;
        return instance.getArenaMaster().getAllPlayers().contains(player);
    }
    
    /**
     * Returns the name of the arena the player is currently in
     * @param playerName Name of the player 
     * @return <b>String</b> name of the arena, or <b>null</b> if the player is not participating
     */
    public String getArenaName(String playerName) {
        Player player = Bukkit.getServer().getPlayerExact(playerName);
        if(player == null) return "";
        return instance.getArenaMaster().getArenaWithPlayer(player).arenaName();
    }
    
    /**
     * Code that is to be executed when the hook is being enabled.<br />
     * This should include a database patch, if necessary
     */
    public void onEnable() {
        try {
            PatchFetcher.fetch(PatchType.MobArena);
            Database.executePatch("1.mobarena");
        } catch (DatabaseConnectionException ex) {
            Message.log(Level.SEVERE, ex.getMessage());
            if(LocalConfiguration.Debug.asBoolean()) ex.printStackTrace();
        }
    }
    
    /**
     * Code that is to be executed when the hook is being disabled.<br />
     * This should include a cleanup routine.
     */
    public void onDisable() {
        instance = null;
    }
}
