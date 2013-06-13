/*
 * Module.java
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

package com.wolvencraft.yasp.settings;

import org.bukkit.Bukkit;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.tables.Normal.SettingsTable;

/**
 * Represents the different plugin modules
 * @author bitWolfy
 *
 */
public enum Module {
    
    YASPX       ("yaspx"),
    
    Server      ("server"),
    Blocks      ("blocks"),
    Items       ("items"),
    Deaths      ("deaths"),
    Inventory   ("inventory"),
    
    AdminCmd    ("admincmd", true),
    Awardments  ("awardments", true),
    BanHammer   ("banhammer", true),
    Citizens    ("citizens", true),
    CommandBook ("commandbook", true),
    Factions    ("factions", true),
    Jobs        ("jobs", true),
    McBans      ("mcbans", true),
    McMMO       ("mcmmo", true),
    MobArena    ("mobarena", true),
    PvpArena    ("pvparena", true),
    Vanish      ("vanishnopacket", true),
    Vault       ("vault", true),
    Votifier    ("votifier", true),
    WorldGuard  ("worldguard", true),
    
    Unknown     ("unknown")
    ;

    public final String KEY;
    private boolean isHook;
    
    private boolean refreshScheduled;
    
    private boolean enabled;
    private boolean active;
    
    private int version;
    
    Module(String key) {
        this.isHook = false;
        this.KEY = key;
        
        refresh();
        
        active = true;
        refreshScheduled = false;
    }
    
    Module(String key, boolean isHook) {
        this.isHook = isHook;
        this.KEY = key;
        
        refresh();
        
        if(!isHook) active = true;
        refreshScheduled = false;
    }

    /**
     * Returns the state of the module specified in the configuration.
     * @return <b>true</b> if the module is enabled, <b>false</b> if it is not
     */
    public boolean isEnabled() {
        if(refreshScheduled) refresh();
        return enabled;
    }

    /**
     * Checks if the module is actually active.
     * @return <b>true</b> if the module is both enabled and active, <b>false</b> otherwise
     */
    public boolean isActive() {
        return isEnabled() && active;
    }
    
    /**
     * Sets the active status of the module.
     * 
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Returns the version of the module.<br />
     * Only relevant if the module is a plugin hook.
     * @return Module version
     */
    public int getVersion() {
        if(refreshScheduled) refreshAsynchronously();
        return version;
    }
    
    /**
     * Sets the new version of the module.<br />
     * Updates the version in the database if the module is a hook
     * @param version New version
     */
    public void setVersion(int version) {
        if(refreshScheduled) refreshAsynchronously();
        this.version = version;
        if(!isHook) return;
        String versionKey = "version." + KEY;
        Query.table(SettingsTable.TableName)
             .value("value", version)
             .condition("key", versionKey)
             .update();
    }
    
    /**
     * Fetches the module variables from the database
     */
    private void refresh() {
        String stateKey = "";
        if(isHook) {
            stateKey = "hook." + KEY;
            
            String versionKey = "version." + KEY;
            QueryResult versionResult = Query.table(SettingsTable.TableName).column("value").condition("key", versionKey).select();
            if(versionResult == null) {
                Query.table(SettingsTable.TableName).value("key", versionKey).value("value", 0).insert();
                version = 0;
            } else {
                version = versionResult.asInt("value");
            }
        } else {
            stateKey = "module." + KEY;
            version = -1;
        }
        
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
    private void refreshAsynchronously() {
        if(!Statistics.getInstance().isEnabled()) return;
        Bukkit.getScheduler().runTaskAsynchronously(Statistics.getInstance(), new Runnable() {
            @Override
            public void run() { refresh(); }
        });
    }
    
    /**
     * Schedules all the modules to refresh
     */
    public static void clearCache() {
        for(Module module : Module.values()) module.refreshScheduled = true;
    }
    
}
