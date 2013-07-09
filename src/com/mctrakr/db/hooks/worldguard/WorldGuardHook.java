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

package com.mctrakr.db.hooks.worldguard;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Location;

import com.mctrakr.db.hooks.PluginHook;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardHook extends PluginHook {
    
    private static final String PLUGIN_NAME = "WorldGuard";
    
    private WorldGuardPlugin instance;
    
    public WorldGuardHook() {
        super(WorldGuardDataStore.lock, PLUGIN_NAME);
    }
    
    @Override
    protected void onEnable() {
        instance = (WorldGuardPlugin) plugin;
    }
    
    @Override
    protected void onDisable() {
        instance = null;
    }
    
    /**
     * Returns a Json array of regions at the specified location
     * @param loc Player location
     * @return Json array of regions
     */
    public String getRegions (Location loc) {
        Map<String, Integer> regions = new HashMap<String, Integer>();
        Iterator<ProtectedRegion> it = instance.getRegionManager(loc.getWorld()).getApplicableRegions(loc).iterator();
        while(it.hasNext()) {
            ProtectedRegion region = it.next();
            regions.put(region.getId(), region.getPriority());
            it.remove();
        }
        return SerializableRegion.serialize(regions);
    }
    
    /**
     * Returns a Json array of flags at the specified location
     * @param loc Player location
     * @return Json array of flags
     */
    public String getFlags (Location loc) {
        return SerializableFlag.serialize(instance.getRegionManager(loc.getWorld()).getApplicableRegions(loc));
    }
    
}
