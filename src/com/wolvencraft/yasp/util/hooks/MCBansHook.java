package com.wolvencraft.yasp.util.hooks;

import org.bukkit.plugin.Plugin;

import com.mcbans.firestar.mcbans.MCBans;
import com.wolvencraft.yasp.settings.Module;

public class MCBansHook extends PluginHook {
    
    public MCBansHook() {
        super(Module.McBans, "MCBans", "mcbans");
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        
        Plugin plugin = getPlugin();
        
        if (plugin != null && plugin instanceof MCBans) {
            module.setActive(true);
        }
    }
    
}
