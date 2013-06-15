/*
 * HookManager.java
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

package com.wolvencraft.yasp;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import com.wolvencraft.yasp.events.plugin.HookInitEvent;
import com.wolvencraft.yasp.util.ExceptionHandler;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.hooks.AdminCmdHook;
import com.wolvencraft.yasp.util.hooks.BanHammerHook;
import com.wolvencraft.yasp.util.hooks.CitizensHook;
import com.wolvencraft.yasp.util.hooks.CommandBookHook;
import com.wolvencraft.yasp.util.hooks.FactionsHook;
import com.wolvencraft.yasp.util.hooks.JobsHook;
import com.wolvencraft.yasp.util.hooks.McMMOHook;
import com.wolvencraft.yasp.util.hooks.MobArenaHook;
import com.wolvencraft.yasp.util.hooks.PluginHook;
import com.wolvencraft.yasp.util.hooks.PvpArenaHook;
import com.wolvencraft.yasp.util.hooks.VanishHook;
import com.wolvencraft.yasp.util.hooks.VaultHook;
import com.wolvencraft.yasp.util.hooks.VotifierHook;
import com.wolvencraft.yasp.util.hooks.WorldGuardHook;

public class HookManager {
    
    private List<PluginHook> activeHooks;
    
    public HookManager() {
        activeHooks = new ArrayList<PluginHook>();
    }
    
    public void onEnable() {
        PluginManager plManager = Statistics.getInstance().getServer().getPluginManager();
        
        Message.log(
                "+-------- [ Hook Manager ] --------+",
                "|" + Message.centerString("Hook Manager starting up", 34) + "|",
                "|" + Message.centerString("", 34) + "|"
                );
        
        for(ApplicableHook hook : ApplicableHook.values()) {
            PluginHook hookObj;
            try { hookObj = hook.getHook().newInstance(); }
            catch (Throwable t) {
                ExceptionHandler.handle(t);
                continue;
            }
            
            if (plManager.getPlugin(hookObj.getPluginName()) == null) {
                Message.log("|" + Message.centerString(hookObj.getPluginName() + " is not found", 34) + "|");
            } else if (!hookObj.getModule().isEnabled()) {
                Message.log("|" + Message.centerString(hookObj.getPluginName() + " is disabled", 34) + "|");
            } else {
                HookInitEvent event = new HookInitEvent(hookObj.getModule());
                Bukkit.getServer().getPluginManager().callEvent(event);
                if(event.isCancelled()) {
                    Message.log("|" + Message.centerString(hookObj.getPluginName() + " is cancelled", 34) + "|");
                    continue;
                }

                Message.log("|" + Message.centerString(hookObj.getPluginName() + " has been enabled", 34) + "|");
                
                hookObj.onEnable();
                activeHooks.add(hookObj);
            }
        }
        
        Message.log("+----------------------------------+");
    }
    
    public void onDisable() {
        Message.log(
                "+-------- [ Hook Manager ] --------+",
                "|" + Message.centerString("Hook Manager shutting down", 34) + "|",
                "|" + Message.centerString("", 34) + "|"
                );
        
        for(PluginHook hook : activeHooks) {
            hook.onDisable();
            Message.log("|" + Message.centerString(hook.getPluginName() + " is shutting down", 34) + "|");
        }
        
        Message.log("+----------------------------------+");
    }
    
    @Getter(AccessLevel.PUBLIC)
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public enum ApplicableHook {
        
        ADMIN_CMD       (AdminCmdHook.class),
        BAN_HAMMER      (BanHammerHook.class),
        CITIZENS        (CitizensHook.class),
        COMMAND_BOOK    (CommandBookHook.class),
        FACTIONS        (FactionsHook.class),
        JOBS            (JobsHook.class),
        MCMMO           (McMMOHook.class),
        MOB_ARENA       (MobArenaHook.class),
        PVP_ARENA       (PvpArenaHook.class),
        VANISH          (VanishHook.class),
        VAULT           (VaultHook.class),
        VOTIFIER        (VotifierHook.class),
        WORLD_GUARD     (WorldGuardHook.class)
        ;
        
        @Getter(AccessLevel.PRIVATE) private Class<? extends PluginHook> hook;
        
    }
    
}
