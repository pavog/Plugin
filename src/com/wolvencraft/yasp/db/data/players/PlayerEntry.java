/*
 * PlayersEntry.java
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

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.data.NormalData;
import com.wolvencraft.yasp.db.tables.Normal.PlayerStats;
import com.wolvencraft.yasp.util.Util;

/**
 * Represents the Player data that is being tracked.<br />
 * Each entry must have a unique player name.
 * @author bitWolfy
 *
 */
public class PlayerEntry extends NormalData {
    
    private final String username;
    private long lastSync;
    
    @Getter(AccessLevel.PUBLIC) private long longestSession;
    @Getter(AccessLevel.PUBLIC) private long currentSession;
    
    @Getter(AccessLevel.PUBLIC) private long totalPlaytime;
    
    public PlayerEntry (int playerId, Player player) {
        username = player.getName();
        lastSync = Util.getTimestamp();
        
        currentSession = 0;
        longestSession = 0;
        
        long firstLogin = lastSync;
        int logins = 0;
        
        QueryResult result = Query.table(PlayerStats.TableName)
            .column(PlayerStats.Logins)
            .column(PlayerStats.FirstLogin)
            .column(PlayerStats.Playtime)
            .column(PlayerStats.LongestSession)
            .condition(PlayerStats.PlayerId, playerId)
            .select();
        
        if(result == null) {
            Query.table(PlayerStats.TableName)
                 .value(PlayerStats.Name, username)
                 .insert();
        } else {
            firstLogin = result.asLong(PlayerStats.FirstLogin);
            if(firstLogin == -1) { firstLogin = lastSync; }
            logins = result.asInt(PlayerStats.Logins);
            this.totalPlaytime = result.asLong(PlayerStats.Playtime);
            longestSession = result.asLong(PlayerStats.LongestSession);
        }
        
        Query.table(PlayerStats.TableName)
            .value(PlayerStats.LoginTime, lastSync)
            .value(PlayerStats.FirstLogin, firstLogin)
            .value(PlayerStats.Logins, ++logins)
            .condition(PlayerStats.PlayerId, playerId)
            .update();
    }
    
    @Override
    @Deprecated
    public void fetchData(int playerId) { }
    
    @Override
    @Deprecated
    public void clearData(int playerId) { }
    
    @Override
    public boolean pushData(int playerId) {
        currentSession += Util.getTimestamp() - lastSync;
        if(longestSession > currentSession) longestSession = currentSession;
        totalPlaytime += Util.getTimestamp() - lastSync;
        lastSync = Util.getTimestamp();
        
        return Query.table(PlayerStats.TableName)
            .value(PlayerStats.Playtime, totalPlaytime)
            .value(PlayerStats.LongestSession, longestSession)
            .condition(PlayerStats.PlayerId, playerId)
            .update();
    }
    
}
