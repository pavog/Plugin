package com.wolvencraft.yasp.util.hooks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.commandbook.CommandBook;
import com.sk89q.commandbook.GodComponent;
import com.wolvencraft.yasp.HookManager.ApplicableHook;
import com.zachsthings.libcomponents.ComponentManager;
import com.zachsthings.libcomponents.bukkit.BukkitComponent;

public class CommandBookHook extends PluginHook {

    private CommandBook instance;
    private ComponentManager<BukkitComponent> componentManager;
    
    public CommandBookHook() {
        super(ApplicableHook.COMMAND_BOOK);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(type.getPluginName());
        
        if (plugin != null && plugin instanceof CommandBook) {
            type.getModule().setActive(true);
            instance = (CommandBook) plugin;
            componentManager = instance.getComponentManager();
        }
    }
    
    /**
     * Checks if the player is in the god mode
     * @param player Player to look up
     * @return <b>true</b> if the player has god mode on, <b>false</b> otherwise
     */
    public boolean isGodMode(Player player) {
        GodComponent component = ((GodComponent) componentManager.getComponent(GodComponent.class));
        return component.isEnabled() && component.hasGodMode(player);
    }
    
}
