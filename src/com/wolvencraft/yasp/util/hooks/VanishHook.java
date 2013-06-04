package com.wolvencraft.yasp.util.hooks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.kitteh.vanish.VanishPlugin;

import com.wolvencraft.yasp.HookManager.ApplicableHook;

public class VanishHook extends PluginHook {

    private VanishPlugin instance;
    
    public VanishHook() {
        super(ApplicableHook.VANISH);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(type.getPluginName());
        
        if (plugin != null && plugin instanceof VanishPlugin) {
            type.getModule().setActive(true);
            instance = (VanishPlugin) plugin;
        }
    }
    
    /**
     * Checks if the player is vanished or not
     * @param playerName Player to look up
     * @return <b>true</b> if the player is vanished, <b>false</b> otherwise
     */
    public boolean isVanished(String playerName) {
        return instance.getManager().isVanished(playerName);
    }
}
