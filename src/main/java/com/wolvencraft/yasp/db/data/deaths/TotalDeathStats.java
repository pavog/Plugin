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

package com.wolvencraft.yasp.db.data.deaths;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.data.NormalData;
import com.wolvencraft.yasp.db.tables.Normal.DeathTotals;
import com.wolvencraft.yasp.settings.RemoteConfiguration;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Represents the total number of times a player died of a particular cause.<br />
 * Each entry must have a unique player and a unique death cause.
 *
 * @author bitWolfy
 */
@Getter(AccessLevel.PUBLIC)
public class TotalDeathStats extends NormalData {

    private DamageCause cause;
    private int times;

    public TotalDeathStats(int playerId, DamageCause cause) {
        this.cause = cause;
        times = 0;

        fetchData(playerId);
    }

    @Override
    public void fetchData(int playerId) {
        if (RemoteConfiguration.MergedDataTracking.asBoolean()) {
            clearData(playerId);
            return;
        }

        QueryResult result = Query.table(DeathTotals.TableName)
                .column(DeathTotals.Times)
                .condition(DeathTotals.PlayerId, playerId)
                .condition(DeathTotals.Cause, cause.name())
                .select();

        if (result == null) {
            Query.table(DeathTotals.TableName)
                    .value(DeathTotals.PlayerId, playerId)
                    .value(DeathTotals.Cause, cause.name())
                    .value(DeathTotals.Times, times)
                    .insert();
        } else {
            times = result.asInt(DeathTotals.Times);
        }
    }

    @Override
    public boolean pushData(int playerId) {
        boolean result = Query.table(DeathTotals.TableName)
                .value(DeathTotals.Times, times)
                .condition(DeathTotals.PlayerId, playerId)
                .condition(DeathTotals.Cause, cause.name())
                .update(RemoteConfiguration.MergedDataTracking.asBoolean());
        fetchData(playerId);
        return result;
    }

    @Override
    public void clearData(int playerId) {
        times = 0;
    }

    /**
     * Increments the number of times a player died from the specified cause.
     */
    public void addTimes() {
        times++;
    }

}
