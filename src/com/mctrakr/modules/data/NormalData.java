/*
 * NormalData.java
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

package com.mctrakr.modules.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.mctrakr.session.OnlineSession;

/**
 * Represents the "totals" of statistical data. These entries are changing every time their corresponding data type changes.<br />
 * No duplicate entries are allowed.
 * @author bitWolfy
 *
 */
@AllArgsConstructor(access=AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
public abstract class NormalData {
    
    protected OnlineSession session;
    
    /**
     * Performs a database operation to fetch the data from the remote database.<br />
     * If no data is found in the database, the default values are inserted.
     */
    public abstract void fetchData();
    
    /**
     * Performs a database operation to push the local data to the remote database.<br />
     * If no data is found in the database, the default values are inserted instead.
     * @return <b>true</b> if the insertion was successful, <b>false</b> otherwise
     */
    public abstract boolean pushData();
}
