package com.wolvencraft.yasp.db.data.blocks;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.wolvencraft.yasp.db.DBTable;

public class Tables {
    
    private Tables() { }
    
    /**
     * Represents the <i>total_blocks</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum BlockTotals implements DBTable {
        TableName       ("total_blocks"),
        MaterialId      ("material_id"),
        PlayerId        ("player_id"),
        Destroyed       ("destroyed"),
        Placed          ("placed");
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>detailed_destroyed_blocks</i> table
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum BlocksBroken implements DBTable {
        TableName       ("detailed_destroyed_blocks"),
        MaterialId      ("material_id"),
        PlayerId        ("player_id"),
        World           ("world"),
        XCoord          ("x"),
        YCoord          ("y"),
        ZCoord          ("z"),
        Timestamp       ("time");
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>detailed_placed_blocks</i> table
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum BlocksPlaced implements DBTable {
        TableName       ("detailed_placed_blocks"),
        MaterialId      ("material_id"),
        PlayerId        ("player_id"),
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
