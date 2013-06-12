package com.wolvencraft.yasp.util.hooks;

import net.citizensnpcs.Citizens;

import org.bukkit.plugin.Plugin;

import com.wolvencraft.yasp.settings.Module;

public class CitizensHook extends PluginHook {
    
    public CitizensHook() {
        super(Module.Citizens, "Citizens", "citizens");
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        
        Plugin plugin = getPlugin();
        
        if (plugin != null && plugin instanceof Citizens) {
            module.setActive(true);
        }
    }
    
}
