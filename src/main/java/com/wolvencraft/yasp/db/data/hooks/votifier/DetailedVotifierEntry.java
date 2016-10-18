/*
 * DetailedVotifierEntry.java
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
