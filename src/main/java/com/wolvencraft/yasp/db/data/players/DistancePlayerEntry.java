/*
 * Statistics Bukkit Plugin
 *
 * V2 Copyright (c) 2016 Paul <pavog> Vogel <http://www.paulvogel.me> and contributors.
 * V1 Copyright (c) 2016 bitWolfy <http://www.wolvencraft.com> and contributors.
 * Contributors are: Mario <MarioG1> Gallaun, Christian <Dazzl> Swan, Cory <Coryf88> Finnestad, Crimsonfoxy
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.wolvencraft.yasp.db.data.players;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.data.NormalData;
import com.wolvencraft.yasp.db.tables.Normal.PlayerDistance;
import com.wolvencraft.yasp.settings.RemoteConfiguration;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Represents the distances a player traveled.
 * Only one entry per player is allowed.
 *
 * @author bitWolfy
 */
@Getter(AccessLevel.PUBLIC)
public class DistancePlayerEntry extends NormalData {

    private double foot;
    private double swim;
    private double flight;
    private double boat;
    private double minecart;
    private double ride;

    /**
     * Default constructor. Takes in the Player object and pulls corresponding values from the remote database.<br />
     * If no data is found in the database, the default values are inserted.
     *
     * @param playerId ID of the tracked player
     */
    public DistancePlayerEntry(int playerId) {
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
        if (RemoteConfiguration.MergedDataTracking.asBoolean()) {
            clearData(playerId);
            return;
        }

        QueryResult result = Query.table(PlayerDistance.TableName)
                .column(PlayerDistance.Foot)
                .column(PlayerDistance.Swim)
                .column(PlayerDistance.Flight)
                .column(PlayerDistance.Boat)
                .column(PlayerDistance.Minecart)
                .column(PlayerDistance.Ride)
                .condition(PlayerDistance.PlayerId, playerId)
                .select();
        if (result == null) {
            Query.table(PlayerDistance.TableName)
                    .value(PlayerDistance.PlayerId, playerId)
                    .value(PlayerDistance.Foot, foot)
                    .value(PlayerDistance.Swim, swim)
                    .value(PlayerDistance.Flight, flight)
                    .value(PlayerDistance.Boat, boat)
                    .value(PlayerDistance.Minecart, minecart)
                    .value(PlayerDistance.Ride, ride)
                    .insert();
        } else {
            foot = result.asInt(PlayerDistance.Foot);
            swim = result.asInt(PlayerDistance.Swim);
            flight = result.asInt(PlayerDistance.Flight);
            boat = result.asInt(PlayerDistance.Boat);
            minecart = result.asInt(PlayerDistance.Minecart);
            ride = result.asInt(PlayerDistance.Ride);
        }
    }

    @Override
    public boolean pushData(int playerId) {
        return Query.table(PlayerDistance.TableName)
                .value(PlayerDistance.Foot, foot)
                .value(PlayerDistance.Swim, swim)
                .value(PlayerDistance.Flight, flight)
                .value(PlayerDistance.Boat, boat)
                .value(PlayerDistance.Minecart, minecart)
                .value(PlayerDistance.Ride, ride)
                .condition(PlayerDistance.PlayerId, playerId)
                .update(RemoteConfiguration.MergedDataTracking.asBoolean());
    }

    @Override
    public void clearData(int playerId) {
        foot = 0;
        swim = 0;
        flight = 0;
        boat = 0;
        minecart = 0;
        ride = 0;
    }

    /**
     * Increments the distance of the specified type by the amount
     *
     * @param type     Travel type
     * @param distance Distance travelled
     */
    public void addDistance(PlayerDistance type, double distance) {
        switch (type) {
            case Foot:
                foot += distance;
                break;
            case Swim:
                swim += distance;
                break;
            case Flight:
                flight += distance;
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
