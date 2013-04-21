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

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
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
public class FactionsHook {
    
    /**
     * <b>Default constructor</b><br />
     * Connects to MobArena and sets up a plugin instance
     */
    public FactionsHook() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Factions");
        
        if (plugin != null && plugin instanceof Factions) {
            Message.log("Factions hook enabled!");
            Settings.ActiveHook.HookFactions.setActive(true);
        }
    }
    
    /**
     * Returns the name of the player's faction
     * @param playerName Name of the player
     * @return Name of the faction
     */
    public String getCurrentFaction(String playerName) {
        Player player = Bukkit.getServer().getPlayerExact(playerName);
        if(player == null) return null;
        FPlayer fplayer = FPlayers.i.get(player);
        return fplayer.getFaction().getId();
    }
    
    /**
     * Returns the faction that owns the land the player is currently standing.
     * @param playerName Name of the player
     * @return Name of the faction
     */
    public String getCurrentLocation(String playerName) {
        Player player = Bukkit.getServer().getPlayerExact(playerName);
        if(player == null) return null;
        FPlayer fplayer = FPlayers.i.get(player);
        Faction factionAt = Board.getFactionAt(fplayer.getLastStoodAt());
        if(factionAt == null) return "none";
        return factionAt.getId();
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
    public void onDisable() { }
}
