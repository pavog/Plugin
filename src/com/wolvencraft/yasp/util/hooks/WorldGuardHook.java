package com.wolvencraft.yasp.util.hooks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.wolvencraft.yasp.Settings;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.serializable.FlagsSerializable;
import com.wolvencraft.yasp.util.serializable.RegionsSerializable;

public class WorldGuardHook {
    
    private static WorldGuardPlugin instance;
    
    public WorldGuardHook() {
        
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        
        if (plugin != null && plugin instanceof WorldGuardPlugin) {
            instance = (WorldGuardPlugin) plugin;
            Message.log("Vault hook enabled!");
            Settings.ActiveHooks.HookWorldGuard.setActive(true);
        }
        
    }
    
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
    
    public static String getFlags (Location loc) {
        return FlagsSerializable.serialize(instance.getRegionManager(loc.getWorld()).getApplicableRegions(loc));
    }
    
}
