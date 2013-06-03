package com.wolvencraft.yasp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.PluginManager;

import com.wolvencraft.yasp.settings.Module;
import com.wolvencraft.yasp.util.ExceptionHandler;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.hooks.*;

import lombok.AccessLevel;
import lombok.Getter;

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
            if (plManager.getPlugin(hook.getPluginName()) == null) {
                Message.log("|" + Message.centerString(hook.getPluginName() + " is not found", 34) + "|");
            } else if (!hook.getModule().isEnabled()) {
                Message.log("|" + Message.centerString(hook.getPluginName() + " is disabled", 34) + "|");
            } else {
                Message.log("|" + Message.centerString(hook.getPluginName() + " has been enabled", 34) + "|");
                try {
                    PluginHook hookObj = hook.getHook().newInstance();
                    hookObj.onEnable();
                    activeHooks.add(hookObj);
                } catch (Throwable t) {
                    ExceptionHandler.handle(t);
                    continue;
                }
            }
        }
        
        Message.log("+----------------------------------+");
    }
    
    public void onDisable() {
        Message.log(
                "+-------- [ Hook Manager ] --------+",
                "| Hook Manager shutting down"
                );
        
        for(PluginHook hook : activeHooks) {
            hook.onDisable();
        }
        
        Message.log("+----------------------------------+");
    }
    
    @Getter(AccessLevel.PUBLIC)
    public enum ApplicableHook {
        
        FACTIONS        (FactionsHook.class, Module.Factions, "Factions"),
        MOB_ARENA       (MobArenaHook.class, Module.MobArena, "MobArena"),
        PVP_ARENA       (PvpArenaHook.class, Module.PvpArena, "PVPArena"),
        VAULT           (VaultHook.class, Module.Vault, "Vault"),
        WORLD_GUARD     (WorldGuardHook.class, Module.WorldGuard, "WorldGuard")
        ;
        
        @Getter(AccessLevel.PRIVATE) private Class<? extends PluginHook> hook;
        private Module module;
        private String pluginName;
        
        ApplicableHook(Class<? extends PluginHook> hook, Module module, String pluginName) {
            this.hook = hook;
            this.module = module;
            this.pluginName = pluginName;
        }
        
    }
    
}
