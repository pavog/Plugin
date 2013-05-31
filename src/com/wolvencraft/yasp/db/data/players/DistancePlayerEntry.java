/*
 * DistancePlayerEntry.java
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

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.data.NormalData;
import com.wolvencraft.yasp.db.tables.Normal.DistancePlayersTable;
import com.wolvencraft.yasp.settings.LocalConfiguration;

/**
 * Represents the distances a player traveled.
 * Only one entry per player is allowed.
 * @author bitWolfy
 *
 */
public class DistancePlayerEntry extends NormalData {
    
    private double foot;
    private double swim;
    private double flight;
    private double boat;
    private double minecart;
    private double pig;
    
    /**
     * Default constructor. Takes in the Player object and pulls corresponding values from the remote database.<br />
     * If no data is found in the database, the default values are inserted.
     * @param playerId ID of the tracked player
     */
    public DistancePlayerEntry(int playerId) {
        foot = 0;
        swim = 0;
        flight = 0;
        boat = 0;
        minecart = 0;
        pig = 0;
        
        fetchData(playerId);
    }
    
    @Override
    public void fetchData(int playerId) {
        if(!LocalConfiguration.Standalone.asBoolean()) {
            clearData(playerId);
            return;
        }
        
        QueryResult result = Query.table(DistancePlayersTable.TableName)
                .column(DistancePlayersTable.Foot)
                .column(DistancePlayersTable.Swim)
                .column(DistancePlayersTable.Flight)
                .column(DistancePlayersTable.Boat)
                .column(DistancePlayersTable.Minecart)
                .column(DistancePlayersTable.Pig)
                .condition(DistancePlayersTable.PlayerId, playerId)
                .select();
        if(result == null) {
            Query.table(DistancePlayersTable.TableName)
                .value(DistancePlayersTable.PlayerId, playerId)
                .value(DistancePlayersTable.Foot, foot)
                .value(DistancePlayersTable.Swim, swim)
                .value(DistancePlayersTable.Flight, flight)
                .value(DistancePlayersTable.Boat, boat)
                .value(DistancePlayersTable.Minecart, minecart)
                .value(DistancePlayersTable.Pig, pig)
                .insert();
        } else {
            foot = result.asInt(DistancePlayersTable.Foot);
            swim = result.asInt(DistancePlayersTable.Swim);
            flight = result.asInt(DistancePlayersTable.Flight);
            boat = result.asInt(DistancePlayersTable.Boat);
            minecart = result.asInt(DistancePlayersTable.Minecart);
            pig = result.asInt(DistancePlayersTable.Pig);
        }
    }

    @Override
    public boolean pushData(int playerId) {
        boolean result = Query.table(DistancePlayersTable.TableName)
            .value(DistancePlayersTable.Foot, foot)
            .value(DistancePlayersTable.Swim, swim)
            .value(DistancePlayersTable.Flight, flight)
            .value(DistancePlayersTable.Boat, boat)
            .value(DistancePlayersTable.Minecart, minecart)
            .value(DistancePlayersTable.Pig, pig)
            .condition(DistancePlayersTable.PlayerId, playerId)
            .update(LocalConfiguration.Standalone.asBoolean());
        return result;
    }
    
    @Override
    public void clearData(int playerId) {
        foot = 0;
        swim = 0;
        flight = 0;
        boat = 0;
        minecart = 0;
        pig = 0;
    }
    
    /**
     * Increments the distance of the specified type by the amount
     * @param type Travel type
     * @param distance Distance travelled
     */
    public void addDistance(DistancePlayersTable type, double distance) {
        switch(type) {
            case Foot:
                foot += distance;
                break;
            case Swim:
                swim += distance;
                break;
            case Flight:
                flight  += distance;
                break;
            case Boat:
                boat += distance;
                break;
            case Minecart:
                minecart += distance;
                break;
            case Pig:
                pig += distance;
                break;
            default:
                break;
        }
    }
}
