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

package com.wolvencraft.yasp.db.hooks.vault;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.wolvencraft.yasp.db.DBTable;

public class Tables {
    
    /**
     * Represents the <i>hook_vault</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum VaultTable implements DBTable {
        
        TableName       ("hook_vault"),
        PlayerId        ("player_id"),
        GroupName       ("group"),
        Balance         ("balance");
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
}
