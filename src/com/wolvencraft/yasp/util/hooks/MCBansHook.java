package com.wolvencraft.yasp.util.hooks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.mcbans.firestar.mcbans.MCBans;
import com.wolvencraft.yasp.HookManager.ApplicableHook;

public class MCBansHook extends PluginHook {
    
    public MCBansHook() {
        super(ApplicableHook.MCBANS);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(type.getPluginName());
        
        if (plugin != null && plugin instanceof MCBans) {
            type.getModule().setActive(true);
        }
    }
    
}
