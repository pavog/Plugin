package com.wolvencraft.yasp.util.hooks;

import lombok.AccessLevel;
import lombok.Getter;

import com.wolvencraft.yasp.HookManager.ApplicableHook;
import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.util.ExceptionHandler;
import com.wolvencraft.yasp.util.PatchFetcher;

@Getter(AccessLevel.PUBLIC)
public abstract class PluginHook {
    
    protected ApplicableHook type;
    
    public PluginHook(ApplicableHook type) {
        this.type = type;
    }
    
    /**
     * Code that is to be executed when the hook is being enabled.<br />
     * This should include a database patch, if necessary
     */
    public void onEnable() {
        try {
            PatchFetcher.fetch(type.getPatch());
            Database.patchModule(false, type.getModule());
        } catch (Throwable t) {
            ExceptionHandler.handle(t);
        }
    }
    
    /**
     * Code that is to be executed when the hook is being disabled.<br />
     * This should include a cleanup routine.
     */
    public void onDisable() {
        // Do nothing
    }
    
}
