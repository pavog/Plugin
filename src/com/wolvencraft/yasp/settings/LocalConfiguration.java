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
import com.wolvencraft.yasp.util.ExceptionHandler;


/**
 * Represents the local configuration, stored in <i>config.yml</i>
 * @author bitWolfy
 *
 */
public enum LocalConfiguration {
    
    Debug           ("debug"),
    DBHost          ("database.host"),
    DBPort          ("database.port"),
    DBName          ("database.name"),
    DBUser          ("database.user"),
    DBPass          ("database.pass"),
    DBPrefix        ("database.prefix"),
    DBConnect       ("jdbc:mysql://" + DBHost.toString() + ":" + DBPort.toInteger() + "/" + DBName.toString(), true),
    LogPrefix       ("log-prefix"),
    ;
    
    private String node;
    private boolean fixedValue;
    private boolean cached;
    private Object cachedValue;
    
    /**
     * Default constructor
     * Used to pull the configuration node values from config.yml
     * @param node Configuration node
     */
    LocalConfiguration(String node) {
        this.node = node;
        this.fixedValue = false;
        this.cached = false;
        this.cachedValue = null;
        
        updateCache();
    }
    
    /**
     * Constructor
     * Used to permanently store a default value
     * @param value Value to store
     * @param fixedValue <b>true</b> to finalize the value
     */
    LocalConfiguration(Object value, boolean fixedValue) {
        if(fixedValue) {
            this.node = "";
            this.fixedValue = fixedValue;
            this.cached = false;
            this.cachedValue = value;
        } else {
            this.node = (String) value;
            this.fixedValue = fixedValue;
            this.cached = false;
            this.cachedValue = null;
            
            updateCache();
        }
    }
    
    /**
     * Returns the cached value of the node, and updates the cache if necessary
     * @return Node value
     */
    private Object getCachedValue() {
        if(!cached) updateCache();
        return cachedValue;
    }
    
    /**
     * Returns the value of the node
     * @return Node value
     */
    public Object getValue() {
        if(fixedValue) return cachedValue;
        else return getCachedValue();
    }
    
    /**
     * Returns the value of the node as a String
     * @return Node value
     */
    public String toString() {
        try { return (String) getValue(); }
        catch(Throwable t) {
            ExceptionHandler.handle(t);
            return "";
        }
    }
    
    /**
     * Returns the value of the node as a Boolean
     * @return Node value
     */
    public Boolean toBoolean() {
        try { return (Boolean) getValue(); }
        catch(Throwable t) {
            ExceptionHandler.handle(t);
            return false;
        }
    }
    
    /**
     * Returns the value of the node as an Integer
     * @return Node value
     */
    public Integer toInteger() {
        try { return (Integer) getValue(); }
        catch(Throwable t) {
            ExceptionHandler.handle(t);
            return 0;
        }
    }
    
    /**
     * Updates the cached node value
     */
    private void updateCache() {
        cachedValue = Statistics.getInstance().getConfig().get(node);
        cached = true;
    }
    
    /**
     * Clears the cache for all nodes
     */
    public static void clearCache() {
        for(LocalConfiguration configNode : LocalConfiguration.values()) {
            configNode.cached = false;
        }
    }
    
}
