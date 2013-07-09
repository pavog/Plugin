package com.wolvencraft.yasp.db.data.pvp;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.wolvencraft.yasp.db.DBTable;

public class Tables {
    
    private Tables() { }
    
    /**
     * Represents the <i>total_pvp_kills</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum PVPTotals implements DBTable {
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
    public enum PlayerKillsPVP implements DBTable {
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
