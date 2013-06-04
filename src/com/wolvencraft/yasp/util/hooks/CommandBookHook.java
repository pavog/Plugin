/*
 * CommandBookHook.java
 * 
 * Statistics
 * Copyright (C) 2013 bitWolfy <http://www.wolvencraft.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

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

    private static CommandBook instance;
    private static ComponentManager<BukkitComponent> componentManager;
    
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
    
    @Override
    public void onDisable() {
        instance = null;
        componentManager = null;
    }
    
    /**
     * Checks if the player is in the god mode
     * @param player Player to look up
     * @return <b>true</b> if the player has god mode on, <b>false</b> otherwise
     */
    public static boolean isGodMode(Player player) {
        GodComponent component = ((GodComponent) componentManager.getComponent(GodComponent.class));
        return component.isEnabled() && component.hasGodMode(player);
    }
    
}
