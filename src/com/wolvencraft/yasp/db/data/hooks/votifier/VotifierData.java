/*
 * VotifierData.java
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

import com.vexsoftware.votifier.model.Vote;
import com.wolvencraft.yasp.db.data.DataStore;
import com.wolvencraft.yasp.session.OnlineSession;

public class VotifierData extends DataStore<TotalVotifierEntry, DetailedVotifierEntry> {
    
    public VotifierData(OnlineSession session) {
        super(session, DataStoreType.Hook_Vanish);
    }
    
    public void playerVoted(Vote vote) {
        TotalVotifierEntry entry = null;
        for(TotalVotifierEntry testEntry : getNormalData()) {
            if(testEntry.getServiceName().equals(vote.getServiceName())) {
                entry = testEntry;
                break;
            }
        }
        
        if(entry == null) {
            entry = new TotalVotifierEntry(session.getId(), vote);
            normalData.add(entry);
        }
        
        entry.addVote();
        detailedData.add(new DetailedVotifierEntry(vote));
    }
    
}
