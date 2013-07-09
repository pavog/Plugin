package com.wolvencraft.yasp.db.data.player;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.wolvencraft.yasp.db.DBTable;

public class Tables {
    
    private Tables() { }
    
    /**
     * Represents the <i>players</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum PlayerStats implements DBTable {
        TableName       ("players"),
        PlayerId        ("player_id"),
        Name            ("name"),
        Online          ("online"),
        LoginTime       ("login_time"),
        Playtime        ("playtime"),
        FirstLogin      ("first_login"),
        LongestSession  ("longest_session"),
        Logins          ("logins");
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>detailed_log_players</i> table
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum PlayerLog implements DBTable {
        TableName       ("detailed_log_players"),
        PlayerId        ("player_id"),
        Timestamp       ("time"),
        World           ("world"),
        XCoord          ("x"),
        YCoord          ("y"),
        ZCoord          ("z"),
        IsLogin         ("is_login");
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }

}
