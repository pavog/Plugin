/*
 * Detailed.java
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

/**
 * Represents any detailed-type tables in the database.<br >
 * Stores table and column names, so that they can be safely used from the plugin
 * @author bitWolfy
 *
 */
public class Detailed {
    
    /**
     * Represents the <i>detailed_destroyed_blocks</i> table
     * @author bitWolfy
     *
     */
    public enum DestroyedBlocks implements DBTable {
        TableName("detailed_destroyed_blocks"),
        EntryId("detailed_destroyed_blocks_id"),
        Material("material_id"),
        PlayerId("player_id"),
        World("world"),
        XCoord("x"),
        YCoord("y"),
        ZCoord("z"),
        Timestamp("time")
        ;
        
        DestroyedBlocks(String columnName) { this.columnName = columnName; }
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>detailed_placed_blocks</i> table
     * @author bitWolfy
     *
     */
    public enum PlacedBlocks implements DBTable {
        TableName("detailed_placed_blocks"),
        EntryId("detailed_placed_blocks_id"),
        Material("material_id"),
        PlayerId("player_id"),
        World("world"),
        XCoord("x"),
        YCoord("y"),
        ZCoord("z"),
        Timestamp("time");
        
        PlacedBlocks(String columnName) { this.columnName = columnName; }
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>detailed_dropped_items</i> table
     * @author bitWolfy
     *
     */
    public enum DroppedItems implements DBTable {
        TableName("detailed_dropped_items"),
        EntryId("detailed_dropped_items_id"),
        Material("material_id"),
        PlayerId("player_id"),
        World("world"),
        XCoord("x"),
        YCoord("y"),
        ZCoord("z"),
        Timestamp("time");
        
        DroppedItems(String columnName) { this.columnName = columnName; }
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>detailed_pickedup_items</i> table
     * @author bitWolfy
     *
     */
    public enum PickedupItems implements DBTable {

        TableName("detailed_pickedup_items"),
        
        EntryId("detailed_pickedup_items_id"),
        Material("material_id"),
        PlayerId("player_id"),
        World("world"),
        XCoord("x"),
        YCoord("y"),
        ZCoord("z"),
        Timestamp("time");
        
        PickedupItems(String columnName) { this.columnName = columnName; }
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>detailed_used_items</i> table
     * @author bitWolfy
     *
     */
    public enum UsedItems implements DBTable {
        TableName("detailed_used_items"),
        EntryId("detailed_used_items_id"),
        Material("material_id"),
        PlayerId("player_id"),
        World("world"),
        XCoord("x"),
        YCoord("y"),
        ZCoord("z"),
        Timestamp("time");
        
        UsedItems(String columnName) { this.columnName = columnName; }
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>detailed_log_players</i> table
     * @author bitWolfy
     *
     */
    public enum LogPlayers implements DBTable {
        TableName("detailed_log_players"),
        EntryId("detailed_log_players_id"),
        PlayerId("player_id"),
        Timestamp("time"),
        World("world"),
        XCoord("x"),
        YCoord("y"),
        ZCoord("z"),
        IsLogin("is_login");
        
        LogPlayers(String columnName) { this.columnName = columnName; }
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>detailed_death_players</i> table
     * @author bitWolfy
     *
     */
    public enum DeathPlayers implements DBTable {
        TableName("detailed_death_players"),
        EntryId("detailed_death_players_id"),
        PlayerId("player_id"),
        Cause("cause"),
        World("world"),
        XCoord("x"),
        YCoord("y"),
        ZCoord("z"),
        Timestamp("time");
        
        DeathPlayers (String columnName) { this.columnName = columnName; }
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>detailed_pve_kills</i> table
     * @author bitWolfy
     *
     */
    public enum PVEKills implements DBTable {
        TableName("detailed_pve_kills"),
        EntryId("detailed_pve_id"),
        Material("material_id"),
        CreatureId("entity_id"),
        PlayerId("player_id"),
        World("world"),
        XCoord("x"),
        YCoord("y"),
        ZCoord("z"),
        Timestamp("time"),
        PlayerKilled("player_killed");
        
        PVEKills (String columnName) { this.columnName = columnName; }
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>detailed_pvp_kills</i> table
     * @author bitWolfy
     *
     */
    public enum PVPKills implements DBTable {
        TableName("detailed_pvp_kills"),
        EntryId("detailed_pvp_id"),
        Material("material_id"),
        KillerId("player_id"),
        VictimId("victim_id"),
        Cause("cause"),
        World("world"),
        XCoord("x"),
        YCoord("y"),
        ZCoord("z"),
        Timestamp("time");
        
        PVPKills (String columnName) { this.columnName = columnName; }
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
}
