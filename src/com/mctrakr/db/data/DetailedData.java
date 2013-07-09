/*
 * DetailedData.java
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

package com.mctrakr.db.data;

/**
 * Represents data stored in a log format. New data is appended to the end of the table. No existing data can be changed.<br />
 * Multiple instances of this type could (and should) exist.
 * @author bitWolfy
 *
 */
public abstract class DetailedData {
    
    /**
     * Explicitly pushes data to the remote database.<br />
     * If the data holder is marked as <i>on hold</i>, skips the holder
     * @param playerId Player ID
     * @return <b>true</b> if the holder has been synchronized and can be removed, <b>false</b> if it is on hold
     */
    public abstract boolean pushData(int playerId);
}
