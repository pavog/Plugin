package com.wolvencraft.yasp.db.data.distance;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.wolvencraft.yasp.db.DBTable;

public class Tables {
    
    private Tables() { }
    
    /**
     * Represents the <i>distances</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum PlayerDistance implements DBTable {
        TableName       ("distances"),
        PlayerId        ("player_id"),
        Foot            ("foot"),
        Swim            ("swim"),
        Flight          ("flight"),
        Boat            ("boat"),
        Minecart        ("minecart"),
        Ride            ("ride");
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
}
