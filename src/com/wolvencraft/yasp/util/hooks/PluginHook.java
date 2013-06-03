package com.wolvencraft.yasp.util.hooks;

import lombok.AccessLevel;
import lombok.Getter;

import com.wolvencraft.yasp.HookManager.ApplicableHook;
import com.wolvencraft.yasp.util.Message;

@Getter(AccessLevel.PUBLIC)
public abstract class PluginHook {
    
    private ApplicableHook type;
    
    public PluginHook(ApplicableHook type) {
        this.type = type;
    }
    
    /**
     * Code that is to be executed when the hook is being enabled.<br />
     * This should include a database patch, if necessary
     */
    public void onEnable() {
        // Do nothing
    }
    
    /**
     * Code that is to be executed when the hook is being disabled.<br />
     * This should include a cleanup routine.
     */
    public void onDisable() {
        Message.log("| " + type.getPluginName() + " shutting down");
    }
    
}
