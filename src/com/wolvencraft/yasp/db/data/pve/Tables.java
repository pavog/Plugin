package com.wolvencraft.yasp.db.data.pve;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.wolvencraft.yasp.db.DBTable;

public class Tables {
    
    private Tables() { }
    
    /**
     * Represents the <i>total_pve_kills</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum PVETotals implements DBTable {
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
    public enum PlayerKillsPVE implements DBTable {
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
