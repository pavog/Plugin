/*
 * PluginHook.java
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

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.db.PatchManager;
import com.wolvencraft.yasp.settings.Module;
import com.wolvencraft.yasp.util.ExceptionHandler;

@Getter(AccessLevel.PUBLIC)
public abstract class PluginHook {
    
    protected Module module;
    protected String patchExtension;
    protected String pluginName;
    
    protected Plugin plugin;
    
    public PluginHook(Module module, String pluginName) {
        this.module = module;
        this.pluginName = pluginName;
        this.patchExtension = pluginName.toLowerCase().replace(" ", "");
    }
    
    public PluginHook(Module module, String pluginName, String patchExtension) {
        this.module = module;
        this.pluginName = pluginName;
        this.patchExtension = patchExtension;
    }
    
    /**
     * Executes the hook enabling routine
     * @return <b>true</b> if the plugin is present and was enabled, <b>false</b> otherwise
     */
    public final boolean enable() {
        plugin = Bukkit.getServer().getPluginManager().getPlugin(pluginName);
        
        if (plugin == null) return false;
        module.setActive(true);
        
        try {
            PatchManager.fetch(patchExtension);
            Database.patchModule(false, module);
        } catch (Throwable t) {
            ExceptionHandler.handle(t);
            module.setActive(false);
            return false;
        }
        
        onEnable();
        return true;
    }
    
    /**
     * Executes the hook disabling routine
     */
    public final void disable() {
        onDisable();
        plugin = null;
    }
    
    /**
     * Extra code to be executed after the hook is enabled
     */
    protected void onEnable() { }
    
     /**
     * Extra code to be executed before the hook is disabled
     */
    protected void onDisable() { }
}
