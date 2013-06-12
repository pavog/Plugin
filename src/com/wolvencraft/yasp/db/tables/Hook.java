/*
 * Hook.java
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
 * Represents all plugin hook tables in the database.<br >
 * Stores table and column names, so that they can be safely used from the plugin
 * @author bitWolfy
 *
 */
public class Hook {
    
    /**
     * Represents the <i>hook_vault</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum VaultTable implements DBTable {
        TableName       ("vaults"),
        PlayerId        ("player_id"),
        GroupName       ("group"),
        Balance         ("balance");
        
        private String columnName;
    }
    
    /**
     * Represents the <i>hook_worldguard</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum WorldGuardTable implements DBTable {
        TableName       ("wg_regions"),
        PlayerId        ("player_id"),
        RegionName      ("regions"),
        RegionFlags     ("flags");
        
        private String columnName;
    }
    
    /**
     * Represents the <i>hook_factions</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum FactionsTable implements DBTable {
        TableName       ("hook_factions"),
        PlayerId        ("player_id"),
        FactionName     ("faction_name"),
        CurrentlyIn     ("current_position"),
        CurrentPower    ("current_power"),
        MaximumPower    ("max_power"),
        FactionRole     ("role"),
        Title           ("title");
        
        private String columnName;
    }
    
    /**
     * Represents the <i>hook_votifier_totals</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum VotifierTotalsTable implements DBTable {
        
        TableName       ("votifier_totals"),
        PlayerId        ("player_id"),
        ServiceName     ("service_name"),
        Votes           ("votes");
        
        private String columnName;
    }
    
    /**
     * Represents the <i>hook_votifier_detailed</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum DetailedVotifierTable implements DBTable {
        
        TableName       ("votifier_detailed"),
        PlayerId        ("player_id"),
        ServiceName     ("service_name"),
        Timestamp       ("time");
        
        private String columnName;
    }
    
}
