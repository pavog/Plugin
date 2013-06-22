/*
 * RemoteConfiguration.java
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
import com.wolvencraft.yasp.db.tables.Miscellaneous.SettingsTable;

/**
 * Represents the configuration pulled from the database.<br />
 * No data is stored locally; all information is pulled from the database during runtime.
 * @author bitWolfy
 *
 */
public enum RemoteConfiguration {
    
    DatabaseVersion             ("version"),
    
    Ping                        ("ping"),
    LogDelay                    ("log_delay"),
    ShowWelcomeMessages         ("show_welcome_messages"),
    WelcomeMessage              ("welcome_message"),
    ShowFirstJoinMessages       ("show_first_join_message"),
    FirstJoinMessage            ("first_join_message"),
    
    VanishDisablesTracking      ("hook.vanish.no_tracking"),
    MergedDataTracking          ("merged_data_tracking"),
    ;
    
    String key;
    QueryResult entry;
    boolean refreshScheduled;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new RemoteConfiguration entry based on the specified key
     * @param key Entry key
     */
    RemoteConfiguration(String key) {
        refreshScheduled = false;
        this.key = key;
        updateCache();
    }
    
    /**
     * Returns the configuration value as String
     * @deprecated <code>asString();</code> should be used instead
     * @return Configuration value
     */
    @Override
    public String toString() {
        return asString();
    }
    
    /**
     * Returns the configuration value as String
     * @return Configuration value
     */
    public String asString() {
        if(refreshScheduled) updateCacheAsynchronously();
        try { return entry.asString("value"); }
        catch (Throwable t) { return ""; }
    }
    
    /**
     * Returns the configuration value as an integer
     * @return Configuration value
     */
    public int asInteger() { 
        if(refreshScheduled) updateCacheAsynchronously();
        try { return entry.asInt("value"); }
        catch (Throwable t) { com.wolvencraft.yasp.util.Message.log("Entry is null (" + t.getMessage() + ")"); return 0; }
    }
    
    /**
     * Returns the configuration value as a boolean
     * @return Configuration value
     */
    public boolean asBoolean() {
        if(refreshScheduled) updateCacheAsynchronously();
        try { return entry.asBoolean("value"); }
        catch (Throwable t) { return false; }
    }
    
    /**
     * Updates the configuration with the specified value
     * @param value New configuration value
     * @return <b>true</b> if the update was successful, <b>false</b> otherwise
     */
    public boolean update(Object value) {
        return Query.table(SettingsTable.TableName).value("value", value).condition("key", key).update();
    }
    
    /**
     * Fetches the configuration data from the database
     */
    private void updateCache() {
        entry = Query.table(SettingsTable.TableName)
                .column("value")
                .condition("key", key)
                .select();
        refreshScheduled = false;
    }
    
    /**
     * Fetches the configuration data from the database asynchronously
     */
    private void updateCacheAsynchronously() {
        if(!Statistics.getInstance().isEnabled()) return;
        Bukkit.getScheduler().runTaskAsynchronously(Statistics.getInstance(), new Runnable() {
            @Override
            public void run() { updateCache(); }
        });
    }
    
    /**
     * Signals the plugin to pull the stored value from the database next time it is called
     */
    public static void clearCache() {
        for(RemoteConfiguration configEntry : RemoteConfiguration.values()) {
            configEntry.refreshScheduled = true;
        }
    }
}
