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
