/*
 * DistancesTotalStats.java
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

package com.wolvencraft.yasp.db.data.distance;

import lombok.AccessLevel;
import lombok.Getter;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.data.NormalData;
import com.wolvencraft.yasp.db.data.distance.Tables.DistancesTable;

/**
 * Represents the distances a player traveled.
 * Only one entry per player is allowed.
 * @author bitWolfy
 *
 */
@Getter(AccessLevel.PUBLIC)
public class DistancesTotalStats extends NormalData {
    
    private double foot;
    private double swim;
    private double flight;
    private double boat;
    private double minecart;
    private double ride;
    
    /**
     * Default constructor. Takes in the Player object and pulls corresponding values from the remote database.<br />
     * If no data is found in the database, the default values are inserted.
     * @param playerId ID of the tracked player
     */
    public DistancesTotalStats(int playerId) {
        foot = 0;
        swim = 0;
        flight = 0;
        boat = 0;
        minecart = 0;
        ride = 0;
        
        fetchData(playerId);
    }
    
    @Override
    public void fetchData(int playerId) {
        QueryResult result = Query.table(DistancesTable.TableName)
                .column(DistancesTable.Foot)
                .column(DistancesTable.Swim)
                .column(DistancesTable.Flight)
                .column(DistancesTable.Boat)
                .column(DistancesTable.Minecart)
                .column(DistancesTable.Ride)
                .condition(DistancesTable.PlayerId, playerId)
                .select();
        if(result == null) {
            Query.table(DistancesTable.TableName)
                .value(DistancesTable.PlayerId, playerId)
                .value(DistancesTable.Foot, foot)
                .value(DistancesTable.Swim, swim)
                .value(DistancesTable.Flight, flight)
                .value(DistancesTable.Boat, boat)
                .value(DistancesTable.Minecart, minecart)
                .value(DistancesTable.Ride, ride)
                .insert();
        } else {
            foot = result.asInt(DistancesTable.Foot);
            swim = result.asInt(DistancesTable.Swim);
            flight = result.asInt(DistancesTable.Flight);
            boat = result.asInt(DistancesTable.Boat);
            minecart = result.asInt(DistancesTable.Minecart);
            ride = result.asInt(DistancesTable.Ride);
        }
    }

    @Override
    public boolean pushData(int playerId) {
        boolean result = Query.table(DistancesTable.TableName)
            .value(DistancesTable.Foot, foot)
            .value(DistancesTable.Swim, swim)
            .value(DistancesTable.Flight, flight)
            .value(DistancesTable.Boat, boat)
            .value(DistancesTable.Minecart, minecart)
            .value(DistancesTable.Ride, ride)
            .condition(DistancesTable.PlayerId, playerId)
            .update();
        fetchData(playerId);
        return result;
    }
    
    /**
     * Increments the distance of the specified type by the amount
     * @param type Travel type
     * @param distance Distance travelled
     */
    public void addDistance(DistancesTable type, double distance) {
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
            case Ride:
                ride += distance;
                break;
            default:
                break;
        }
    }
}
