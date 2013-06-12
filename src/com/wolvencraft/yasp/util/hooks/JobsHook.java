package com.wolvencraft.yasp.util.hooks;

import me.zford.jobs.Jobs;

import org.bukkit.plugin.Plugin;

import com.wolvencraft.yasp.settings.Module;

public class JobsHook extends PluginHook {
    
    public JobsHook() {
        super(Module.Jobs, "Jobs", "jobs");
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        
        Plugin plugin = getPlugin();
        
        if (plugin != null && plugin instanceof Jobs) {
            module.setActive(true);
        }
    }
    
}
