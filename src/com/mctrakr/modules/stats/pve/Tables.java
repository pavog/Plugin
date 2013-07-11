/*
 * Tables.java
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

package com.mctrakr.modules.stats.pve;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.mctrakr.database.DBTable;

public class Tables {
    
    private Tables() { }
    
    /**
     * Represents the <i>total_pve_kills</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum PveTotalsTable implements DBTable {
        TableName       ("total_pve_kills"),
        MaterialId      ("material_id"),
        CreatureId      ("entity_id"),
        PlayerId        ("player_id"),
        PlayerKilled    ("player_killed"),
        CreatureKilled  ("creature_killed");
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>detailed_pve_kills</i> table
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum PveDetailsTable implements DBTable {
        TableName       ("detailed_pve_kills"),
        MaterialId      ("material_id"),
        CreatureId      ("entity_id"),
        PlayerId        ("player_id"),
        World           ("world"),
        XCoord          ("x"),
        YCoord          ("y"),
        ZCoord          ("z"),
        Timestamp       ("time"),
        PlayerKilled    ("player_killed");
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }

}
