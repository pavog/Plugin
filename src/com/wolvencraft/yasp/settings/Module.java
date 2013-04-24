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

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.tables.Normal.SettingsTable;

/**
 * Represents the different plugin modules
 * @author bitWolfy
 *
 */
public enum Module {
    
    Server("server"),
    Blocks("blocks"),
    Items("items"),
    Deaths("deaths"),
    Inventory("inventory"),
    
    Vault("vault", true),
    McMMO("mcmmo", true),
    Jobs("jobs", true),
    WorldGuard("worldguard", true),
    MobArena("mobarena", true),
    PvpArena("pvparena", true),
    Factions("factions", true);

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
        
        refreshScheduled = false;
    }
    
    Module(String key, boolean isHook) {
        this.isHook = isHook;
        this.KEY = key;
        
        refresh();
        
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
        return enabled && active;
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
        if(refreshScheduled) refresh();
        return version;
    }
    
    /**
     * Sets the new version of the module.<br />
     * Updates the version in the database if the module is a hook
     * @param version New version
     */
    public void setVersion(int version) {
        if(refreshScheduled) refresh();
        this.version = version;
        if(!isHook) return;
        String versionKey = "version." + KEY;
        Query.table(SettingsTable.TableName)
             .value("value", version)
             .condition("key", versionKey)
             .update();
    }
    
    private void refresh() {
        String stateKey = "";
        if(isHook) {
            stateKey = "hook." + KEY;
            
            String versionKey = "version." + KEY;
            if(Query.table(SettingsTable.TableName).condition("key", versionKey).exists()) {
                try { version = Query.table(SettingsTable.TableName).column("value").condition("key", versionKey).select().asInt("value"); }
                catch (Throwable t) { enabled = true; }
            } else {
                Query.table(SettingsTable.TableName).value("key", versionKey).value("value", 0).insert();
                enabled = true;
            }
        } else {
            stateKey = "module." + KEY;
            version = -1;
        }
        
        if(Query.table(SettingsTable.TableName).condition("key", stateKey).exists()) {
            try { enabled = Query.table(SettingsTable.TableName).column("value").condition("key", stateKey).select().asBoolean("value"); }
            catch (Throwable t) { enabled = true; }
        } else {
            Query.table(SettingsTable.TableName).value("key", stateKey).value("value", true).insert();
            enabled = true;
        }
    }
    
    /**
     * Schedules all the modules to refresh
     */
    public static void clearCache() {
        for(Module module : Module.values()) module.refreshScheduled = true;
    }
    
}
