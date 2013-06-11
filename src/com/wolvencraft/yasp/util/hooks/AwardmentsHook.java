package com.wolvencraft.yasp.util.hooks;

import net.citizensnpcs.Citizens;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.wolvencraft.yasp.HookManager.ApplicableHook;

public class AwardmentsHook extends PluginHook {
    
    public AwardmentsHook() {
        super(ApplicableHook.AWARDMENTS);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(type.getPluginName());
        
        if (plugin != null && plugin instanceof Citizens) {
            type.getModule().setActive(true);
        }
    }
    
}
