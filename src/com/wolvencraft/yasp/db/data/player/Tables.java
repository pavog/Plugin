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
    public enum PlayersTable implements DBTable {
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
    public enum PlayersLogTable implements DBTable {
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
