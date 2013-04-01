/*
 * WorldGuardHookFactory.java
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

package com.wolvencraft.yasp.db.data.hooks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.wolvencraft.yasp.Settings;
import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.tables.Hook.WorldGuardTable;
import com.wolvencraft.yasp.exceptions.DatabaseConnectionException;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.Util;

/**
 * Hooks into WorldGuard to track its statistics
 * @author bitWolfy
 *
 */
public class WGHookFactory implements PluginHookFactory {
    
    private static WGHookFactory instance;
    private static WorldGuardPlugin worldGuard;
    
    public WGHookFactory() {
        Plugin plugin = Statistics.getInstance().getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) { worldGuard = null; }
        else {
            Message.log("WorldGuard hook enabled!");
            Settings.ActiveHooks.HookVault.setActive(true);
            worldGuard = (WorldGuardPlugin) plugin;
            instance = this;
        }
    }
    
    /**
     * Returns the hook instance
     * @return <b>WorldGuardHook</b> instance
     */
    public static WGHookFactory getInstance() {
        return instance;
    }
    
    @Override
    public void onEnable() {
        try { Database.getInstance().runCustomPatch("worldguard_v1"); }
        catch (DatabaseConnectionException ex) {
            Message.log(Level.SEVERE, ex.getMessage());
        }
    }

    @Override
    public void onDisable() {
        worldGuard = null;
    }
    
    /**
     * Represents the general player's region information
     * @author bitWolfy
     *
     */
    public class WGHookData implements PluginHook {
        
        /**
         * <b>Default constructor</b><br />
         * Creates a new normal table for the player
         * @param player Player object
         * @param playerId Player ID
         */
        public WGHookData(Player player, int playerId) {
            this.playerId = playerId;
            this.playerName = player.getName();
            this.regions = new ArrayList<String>();
            fetchData();
        }
        
        private int playerId;
        private String playerName;
        private List<String> regions;
        
        @Override
        public void fetchData() {
            Player player = Bukkit.getPlayerExact(playerName);
            if(player == null) return;
            
            regions.clear();
            ApplicableRegionSet set = worldGuard.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
            Iterator<ProtectedRegion> it = set.iterator();
            while(it.hasNext()) {
                ProtectedRegion region = it.next();
                regions.add(region.getId());
                it.remove();
            }
            
            if(Query.table(WorldGuardTable.TableName.toString())
                    .condition(WorldGuardTable.PlayerId.toString(), playerId)
                    .exists()) return;
            
            Query.table(WorldGuardTable.TableName.toString())
            .value(WorldGuardTable.PlayerId.toString(), playerId)
            .value(WorldGuardTable.RegionName.toString(), Util.toJsonArray(regions))
            .insert();
        }

        @Override
        public boolean pushData() {
            return Query.table(WorldGuardTable.TableName.toString())
                .value(WorldGuardTable.RegionName.toString(), Util.toJsonArray(regions))
                .condition(WorldGuardTable.PlayerId.toString(), playerId)
                .update();
        }
        
    }
    
}
