package com.wolvencraft.yasp.util.hooks;

import org.bukkit.plugin.Plugin;

import com.gmail.nossr50.mcMMO;
import com.wolvencraft.yasp.settings.Module;

public class McMMOHook extends PluginHook {
    
    public McMMOHook() {
        super(Module.McMMO, "mcMMO", "mcmmo");
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        
        Plugin plugin = getPlugin();
        
        if (plugin != null && plugin instanceof mcMMO) {
            module.setActive(true);
        }
    }
    
}
