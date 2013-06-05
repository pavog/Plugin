package com.wolvencraft.yasp.db.data.hooks.votifier;

import com.vexsoftware.votifier.model.Vote;
import com.wolvencraft.yasp.db.data.AdvancedDataStore;

public class VotifierData extends AdvancedDataStore<TotalVotifierEntry, DetailedVotifierEntry> {
    
    public VotifierData(int playerId) {
        super(playerId, DataStoreType.Hook_Vanish);
    }
    
    public void playerVoted(Vote vote) {
        TotalVotifierEntry entry = null;
        for(TotalVotifierEntry testEntry : normalData) {
            if(testEntry.getServiceName().equals(vote.getServiceName())) {
                entry = testEntry;
                break;
            }
        }
        
        if(entry == null) {
            entry = new TotalVotifierEntry(playerId, vote);
            normalData.add(entry);
        }
        
        entry.addVote();
        detailedData.add(new DetailedVotifierEntry(vote));
    }
    
}
