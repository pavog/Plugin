package com.wolvencraft.yasp.db.data;

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.tables.Miscellaneous.SettingsTable;
import com.wolvencraft.yasp.settings.RemoteConfiguration;

public class ConfigLock implements Runnable {
    
    private BukkitTask process;
    
    private String configKey;
    private boolean refreshScheduled;
    private boolean enabled;
    
    @Getter(AccessLevel.PUBLIC)
    private boolean versioned;
    private int version;
    
    /**
     * Default constructor
     * @param configKey Configuration key
     */
    public ConfigLock(String configKey) {
        this.configKey = configKey;
        this.versioned = false;
        
        enabled = false;
        version = 0;
        
        try { updateCacheAsynchronously(); }
        catch(Throwable t) { }
        
        refreshScheduled = false;
        
        process = Bukkit.getScheduler().runTaskTimerAsynchronously(Statistics.getInstance(), this, 0L, RemoteConfiguration.Ping.asLong());
    }
    
    /**
     * Constructor
     * @param configKey Configuration key
     * @param versioned Should the version be checked?
     */
    public ConfigLock(String configKey, boolean versioned) {
        this.configKey = configKey;
        this.versioned = versioned;
        
        enabled = false;
        version = 0;
        
        try { updateCache(); }
        catch(Throwable t) { }
        
        refreshScheduled = false;
        
        process = Bukkit.getScheduler().runTaskTimerAsynchronously(Statistics.getInstance(), this, 0L, RemoteConfiguration.Ping.asLong());
    }
    
    /**
     * Schedules all the modules to refresh
     */
    @Override
    public void run() {
        refreshScheduled = true;
    }

    /**
     * Returns the state of the module specified in the configuration.
     * @return <b>true</b> if the module is enabled, <b>false</b> if it is not
     */
    public boolean isEnabled() {
        if(refreshScheduled) updateCacheAsynchronously();
        return enabled;
    }

    /**
     * Returns the version of the module.<br />
     * Only relevant if the module is a plugin hook.
     * @return Module version
     */
    public int getVersion() {
        if(refreshScheduled) updateCacheAsynchronously();
        return version;
    }
    
    /**
     * Sets the new version of the module.<br />
     * Updates the version in the database if the module is a hook
     * @param version New version
     */
    public void setVersion(int version) {
        if(refreshScheduled) updateCacheAsynchronously();
        this.version = version;
        if(!versioned) return;
        String versionKey = "version." + configKey;
        Query.table(SettingsTable.TableName)
             .value("value", version)
             .condition("key", versionKey)
             .update();
    }
    
    /**
     * Fetches the module variables from the database
     */
    private void updateCache() {
        String stateKey = "module." + configKey;
        if(versioned) {
            
            String versionKey = "version." + configKey;
            QueryResult versionResult = Query.table(SettingsTable.TableName).column("value").condition("key", versionKey).select();
            if(versionResult == null) {
                Query.table(SettingsTable.TableName).value("key", versionKey).value("value", 0).insert();
                version = 0;
            } else {
                version = versionResult.asInt("value");
            }
        } else version = 0;
        
        QueryResult enabledResult = Query.table(SettingsTable.TableName).column("value").condition("key", stateKey).select();
        if(enabledResult == null) {
            Query.table(SettingsTable.TableName).value("key", stateKey).value("value", false).insert();
            enabled = true;
        } else {
            enabled = enabledResult.asBoolean("value");
        }
    }
    
    /**
     * Fetches the module variables from the database asynchronously
     */
    private void updateCacheAsynchronously() {
        if(!Statistics.getInstance().isEnabled()) return;
        Bukkit.getScheduler().runTaskAsynchronously(Statistics.getInstance(), new Runnable() {
            @Override
            public void run() { updateCache(); }
        });
    }
    
    @Override
    public void finalize() {
        process.cancel();
    }
    
}
