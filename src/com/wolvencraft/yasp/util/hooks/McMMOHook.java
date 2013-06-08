package com.wolvencraft.yasp.util.hooks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.gmail.nossr50.mcMMO;
import com.wolvencraft.yasp.HookManager.ApplicableHook;

public class McMMOHook extends PluginHook {
    
    public McMMOHook() {
        super(ApplicableHook.MCMMO);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(type.getPluginName());
        
        if (plugin != null && plugin instanceof mcMMO) {
            type.getModule().setActive(true);
        }
    }
    
}
