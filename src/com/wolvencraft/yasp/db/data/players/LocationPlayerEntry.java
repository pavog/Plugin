/*
 * LocationPlayerEntry.java
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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.data.NormalData;
import com.wolvencraft.yasp.db.tables.Normal.PlayersLocations;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PUBLIC)
public class LocationPlayerEntry extends NormalData {
    
    String playerName;
    
    public LocationPlayerEntry(int playerId, String playerName) {
        this.playerName = playerName;
        
        fetchData(playerId);
    }

    @Override
    public void fetchData(int playerId) {
        Player player = Bukkit.getPlayerExact(playerName);
        if(player == null) return;
        
        Location location = player.getLocation();
        
        if(!Query.table(PlayersLocations.TableName)
                .column(PlayersLocations.PlayerId)
                .condition(PlayersLocations.PlayerId, playerId)
                .exists())
            
            Query.table(PlayersLocations.TableName)
                .value(PlayersLocations.PlayerId, playerId)
                .value(PlayersLocations.World, location.getWorld().getName())
                .value(PlayersLocations.XCoord, location.getBlockX())
                .value(PlayersLocations.YCoord, location.getBlockY())
                .value(PlayersLocations.ZCoord, location.getBlockZ())
                .value(PlayersLocations.Biome, location.getWorld().getBiome(location.getBlockX(), location.getBlockZ()).name())
                .value(PlayersLocations.Humidity, location.getWorld().getHumidity(location.getBlockX(), location.getBlockZ()))
                .insert();
    }

    @Override
    public boolean pushData(int playerId) {
        Player player = Bukkit.getPlayerExact(playerName);
        if(player == null) return false;
        
        Location location = player.getLocation();
        
        return Query.table(PlayersLocations.TableName)
                .value(PlayersLocations.World, location.getWorld().getName())
                .value(PlayersLocations.XCoord, location.getBlockX())
                .value(PlayersLocations.YCoord, location.getBlockY())
                .value(PlayersLocations.ZCoord, location.getBlockZ())
                .value(PlayersLocations.Biome, location.getWorld().getBiome(location.getBlockX(), location.getBlockZ()).name())
                .value(PlayersLocations.Humidity, location.getWorld().getHumidity(location.getBlockX(), location.getBlockZ()))
                .condition(PlayersLocations.PlayerId, playerId)
                .update();
    }

    @Override
    @Deprecated
    public void clearData(int playerId) { }
    
}
