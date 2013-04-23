/*
 * WorldGuardHook.java
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.exceptions.DatabaseConnectionException;
import com.wolvencraft.yasp.settings.LocalConfiguration;
import com.wolvencraft.yasp.settings.Module;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.PatchFetcher;
import com.wolvencraft.yasp.util.PatchFetcher.PatchType;
import com.wolvencraft.yasp.util.serializable.FlagsSerializable;
import com.wolvencraft.yasp.util.serializable.RegionsSerializable;

/**
 * Simplistic WorldGuard hook
 * @author bitWolfy
 *
 */
public class WorldGuardHook {
    
    private static WorldGuardPlugin instance;
    
    /**
     * <b>Default constructor</b><br />
     * Connects to WorldGuard and sets up a plugin instance
     */
    public WorldGuardHook() {
        
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        
        if (plugin != null && plugin instanceof WorldGuardPlugin) {
            instance = (WorldGuardPlugin) plugin;
            Message.log("WorldGuard hook enabled!");
            Module.WorldGuard.setActive(true);
        }
        
    }
    
    /**
     * Returns a Json array of regions at the specified location
     * @param loc Player location
     * @return Json array of regions
     */
    public static String getRegions (Location loc) {
        Map<String, Integer> regions = new HashMap<String, Integer>();
        Iterator<ProtectedRegion> it = instance.getRegionManager(loc.getWorld()).getApplicableRegions(loc).iterator();
        while(it.hasNext()) {
            ProtectedRegion region = it.next();
            regions.put(region.getId(), region.getPriority());
            it.remove();
        }
        return RegionsSerializable.serialize(regions);
    }
    
    /**
     * Returns a Json array of flags at the specified location
     * @param loc Player location
     * @return Json array of flags
     */
    public static String getFlags (Location loc) {
        return FlagsSerializable.serialize(instance.getRegionManager(loc.getWorld()).getApplicableRegions(loc));
    }
    
    /**
     * Code that is to be executed when the hook is being enabled.<br />
     * This should include a database patch, if necessary
     */
    public void onEnable() {
        try {
            PatchFetcher.fetch(PatchType.WorldGuard);
            Database.executePatch("1.wg");
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
