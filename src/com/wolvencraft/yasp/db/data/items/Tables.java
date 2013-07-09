package com.wolvencraft.yasp.db.data.items;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.wolvencraft.yasp.db.DBTable;

public class Tables {
    
    private Tables() { }
    
    /**
     * Represents the <i>total_items</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum ItemTotals implements DBTable {
        TableName       ("total_items"),
        MaterialId      ("material_id"),
        PlayerId        ("player_id"),
        Dropped         ("dropped"),
        PickedUp        ("picked_up"),
        Used            ("used"),
        Crafted         ("crafted"),
        Smelted         ("smelted"),
        Broken          ("broken"),
        Enchanted       ("enchanted"),
        Repaired        ("repaired");
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>detailed_dropped_items</i> table
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum ItemsDropped implements DBTable {
        TableName       ("detailed_dropped_items"),
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
     * Represents the <i>detailed_pickedup_items</i> table
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum ItemsPickedUp implements DBTable {
        TableName       ("detailed_pickedup_items"),
        EntryId         ("detailed_pickedup_items_id"),
        Material        ("material_id"),
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
     * Represents the <i>detailed_used_items</i> table
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum ItemsConsumed implements DBTable {
        TableName       ("detailed_used_items"),
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
