/*
 * DeathsTotalStats.java
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

package com.mctrakr.modules.stats.deaths;

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.mctrakr.database.Query;
import com.mctrakr.database.Query.QueryResult;
import com.mctrakr.modules.DataStore.NormalData;
import com.mctrakr.modules.stats.deaths.Tables.TotalDeathsTable;
import com.mctrakr.session.OnlineSession;

/**
 * Represents the total number of times a player died of a particular cause.<br />
 * Each entry must have a unique player and a unique death cause.
 * @author bitWolfy
 *
 */
@Getter(AccessLevel.PUBLIC) 
public class DeathsTotalStats extends NormalData {
    
    private DamageCause cause;
    private int times;
    
    public DeathsTotalStats(OnlineSession session, DamageCause cause) {
        super(session);
        this.cause = cause;
        times = 0;
        
        fetchData();
    }
    
    @Override
    public void fetchData() {
        QueryResult result = Query.table(TotalDeathsTable.TableName)
                .column(TotalDeathsTable.Times)
                .condition(TotalDeathsTable.PlayerId, session.getId())
                .condition(TotalDeathsTable.Cause, cause.name())
                .select();
        
        if(result == null) {
            Query.table(TotalDeathsTable.TableName)
                .value(TotalDeathsTable.PlayerId, session.getId())
                .value(TotalDeathsTable.Cause, cause.name())
                .value(TotalDeathsTable.Times, times)
                .insert();
        } else {
            times = result.asInt(TotalDeathsTable.Times);
        }
    }

    @Override
    public boolean pushData() {
        boolean result = Query.table(TotalDeathsTable.TableName)
                .value(TotalDeathsTable.Times, times)
                .condition(TotalDeathsTable.PlayerId, session.getId())
                .condition(TotalDeathsTable.Cause, cause.name())
                .update();
        return result;
    }
    
    /**
     * Increments the number of times a player died from the specified cause.
     */
    public void addTimes() {
        times++;
    }
    
}
