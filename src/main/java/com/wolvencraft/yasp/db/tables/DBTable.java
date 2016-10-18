/*
 * DBTable.java
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
 * Common interface for database tables.<br />
 * Table enums have to implement this method to be used in queries.
 * @author bitWolfy
 *
 */
public interface DBTable {
    
    /**
     * Returns the name of the column
     * @return Column name
     */
    public String getColumnName();
    
    /**
     * Equivalent to <code>getColumnName();</code>.
     * Required due to some gimmicks in the Query code.
     * @return
     */
    public String toString();
    
}
