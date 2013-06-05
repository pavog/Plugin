package com.wolvencraft.yasp.db.data.hooks.votifier;

import com.vexsoftware.votifier.model.Vote;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.data.DetailedData;
import com.wolvencraft.yasp.db.tables.Hook.DetailedVotifierTable;

public class DetailedVotifierEntry extends DetailedData {
    
    private String serviceName;
    private String timestamp;
    
    public DetailedVotifierEntry(Vote vote) {
        this.serviceName = vote.getServiceName();
        this.timestamp = vote.getTimeStamp();
    }
    
    @Override
    public boolean pushData(int playerId) {
        return Query.table(DetailedVotifierTable.TableName)
            .value(DetailedVotifierTable.PlayerId, playerId)
            .value(DetailedVotifierTable.ServiceName, serviceName)
            .value(DetailedVotifierTable.Timestamp, timestamp)
            .insert();
    }

}
