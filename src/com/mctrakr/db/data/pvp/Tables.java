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

package com.mctrakr.db.data.pvp;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.mctrakr.db.DBTable;

public class Tables {
    
    private Tables() { }
    
    /**
     * Represents the <i>total_pvp_kills</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum PvpTotalsTable implements DBTable {
        TableName       ("total_pvp_kills"),
        MaterialId      ("material_id"),
        PlayerId        ("player_id"),
        VictimId        ("victim_id"),
        Times           ("times");
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>detailed_pvp_kills</i> table
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum PvpDetailsTable implements DBTable {
        TableName       ("detailed_pvp_kills"),
        MaterialId      ("material_id"),
        KillerId        ("player_id"),
        VictimId        ("victim_id"),
        Cause           ("cause"),
        World           ("world"),
        XCoord          ("x"),
        YCoord          ("y"),
        ZCoord          ("z"),
        Timestamp       ("time");
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }

}
