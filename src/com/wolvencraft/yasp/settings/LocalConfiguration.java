/*
 * LocalConfiguration.java
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

import com.wolvencraft.yasp.Statistics;


/**
 * Represents the local configuration, stored in <i>config.yml</i>
 * @author bitWolfy
 *
 */
public enum LocalConfiguration {
    Debug           ("debug", true),
    DBHost          ("database.host", true),
    DBPort          ("database.port", true),
    DBName          ("database.name", true),
    DBUser          ("database.user", true),
    DBPass          ("database.pass", true),
    DBPrefix        ("database.prefix", true),
    DBConnect       ("jdbc:mysql://" + DBHost.asString() + ":" + DBPort.asInteger() + "/" + DBName.asString()),
    LogPrefix       ("log-prefix", true),
    ;
    
    Object entry;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new LocalConfiguration with the specified value
     * @param entry Configuration value
     */
    LocalConfiguration(Object entry) {
        this.entry = entry;
    }
    
    /**
     * <b>Constructor</b><br />
     * Creates a new LocalConfiguration entry with the value that can be pulled from <code>config.yml</code>.
     * @param entry Configuration value or <code>config.yml</code> node
     * @param fromFile If <b>true</b>, configuration values will be pulled from file, otherwise, the <code>entry</code> will be used
     */
    LocalConfiguration(Object entry, boolean fromFile) {
        if(fromFile) this.entry = Statistics.getInstance().getConfig().get((String) entry);
        else this.entry = entry;
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
        return (String) entry;
    }
    
    /**
     * Returns the configuration value as a boolean
     * @return Configuration value
     */
    public boolean asBoolean() {
        try { return (Boolean) entry; }
        catch (Throwable t) { return false; }
    }
    
    /**
     * Returns the configuration value as an integer
     * @return Configuration value
     */
    public int asInteger() {
        try { return (Integer) entry; }
        catch (Throwable t) { return 0; }
    }
}
