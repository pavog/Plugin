package com.wolvencraft.yasp.util.hooks;

import me.zford.jobs.Jobs;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.wolvencraft.yasp.HookManager.ApplicableHook;

public class JobsHook extends PluginHook {
    
    public JobsHook() {
        super(ApplicableHook.JOBS);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(type.getPluginName());
        
        if (plugin != null && plugin instanceof Jobs) {
            type.getModule().setActive(true);
        }
    }
    
}
