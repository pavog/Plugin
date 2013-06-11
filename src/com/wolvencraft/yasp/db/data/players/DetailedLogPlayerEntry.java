/*
 * DetailedLogPlayerEntry.java
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

package com.wolvencraft.yasp.db.data.players;

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.Location;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.data.DetailedData;
import com.wolvencraft.yasp.db.tables.Detailed.LogPlayers;
import com.wolvencraft.yasp.util.Util;

/**
 * Tracks player's login and logout locations
 * @author bitWolfy
 *
 */
@Getter(AccessLevel.PUBLIC)
public class DetailedLogPlayerEntry extends DetailedData {
    
    private long time;
    private boolean isLogin;
    private Location location;
     
    /**
     * <b>Default constructor</b><br />
     * Creates a new DetailedLogPlayersEntry object based on arguments provided
     * @param location Location of the event
     * @param isLogin <b>true</b> if the player has logged in, <b>false</b> if he logged off
     */
    public DetailedLogPlayerEntry(Location location, boolean isLogin) {
        time = Util.getTimestamp();
        this.isLogin = isLogin;
        this.location = location.clone();
    }
     
    @Override
    public boolean pushData(int playerId) {
        return Query.table(LogPlayers.TableName)
                .value(LogPlayers.PlayerId, playerId)
                .value(LogPlayers.Timestamp, time)
                .value(LogPlayers.IsLogin, isLogin)
                .value(LogPlayers.World, location.getWorld().getName())
                .value(LogPlayers.XCoord, location.getBlockX())
                .value(LogPlayers.YCoord, location.getBlockY())
                .value(LogPlayers.ZCoord, location.getBlockZ())
                .insert();
    }
 
}
