/*
 * TotalVotifierEntry.java
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

package com.wolvencraft.yasp.db.data.hooks.votifier;

import lombok.AccessLevel;
import lombok.Getter;

import com.vexsoftware.votifier.model.Vote;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.data.NormalData;
import com.wolvencraft.yasp.db.tables.Hook.VotifierTotalsTable;
import com.wolvencraft.yasp.settings.RemoteConfiguration;

@Getter(AccessLevel.PUBLIC) 
public class TotalVotifierEntry extends NormalData {
    
    private String serviceName;
    private int votes;
    
    public TotalVotifierEntry(int playerId, Vote vote) {
        this.serviceName = vote.getServiceName();
        this.votes = 0;
        
        fetchData(playerId);
    }
    
    @Override
    public void fetchData(int playerId) {
        if(RemoteConfiguration.MergedDataTracking.asBoolean()) {
            clearData(playerId);
            return;
        }
        
        QueryResult result = Query.table(VotifierTotalsTable.TableName)
                .column(VotifierTotalsTable.Votes)
                .condition(VotifierTotalsTable.PlayerId, playerId)
                .condition(VotifierTotalsTable.ServiceName, serviceName)
                .select();
        if(result == null) {
            Query.table(VotifierTotalsTable.TableName)
                .value(VotifierTotalsTable.PlayerId, playerId)
                .value(VotifierTotalsTable.ServiceName, serviceName)
                .value(VotifierTotalsTable.Votes, votes)
                .insert();
        } else {
            votes = result.asInt(VotifierTotalsTable.Votes);
        }
    }

    @Override
    public boolean pushData(int playerId) {
        return Query.table(VotifierTotalsTable.TableName)
                .value(VotifierTotalsTable.PlayerId, playerId)
                .value(VotifierTotalsTable.ServiceName, serviceName)
                .value(VotifierTotalsTable.Votes, votes)
                .insert();
    }

    @Override
    public void clearData(int playerId) {
        votes = 0;
    }
    
    public void addVote() {
        votes++;
    }

}
