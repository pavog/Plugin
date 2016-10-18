/*
 * Miscellaenous.java
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

package com.wolvencraft.yasp.db.tables;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents any miscellaneous tables in the database.<br >
 * Stores table and column names, so that they can be safely used from the plugin.
 * @author bitWolfy
 *
 */
public class Miscellaneous {
    
    /**
     * Represents the <i>settings</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum SettingsTable implements DBTable {
        TableName       ("settings"),
        Key             ("key"),
        Value           ("value");
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>server_statistics</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum ServerStatsTable implements DBTable {
        TableName       ("server_statistics"),
        Key             ("key"),
        Value           ("value");
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>materials</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum MaterialsTable implements DBTable {
        TableName       ("materials"),
        MaterialId      ("material_id"),
        TpName          ("tp_name");
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>entities</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum EntitiesTable implements DBTable {
        TableName       ("entities"),
        EntityId        ("entity_id"),
        TpName          ("tp_name");
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
}
