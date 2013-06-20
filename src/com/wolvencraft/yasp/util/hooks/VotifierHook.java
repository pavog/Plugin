/*
 * VotifierHook.java
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

package com.wolvencraft.yasp.util.hooks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.db.data.DataStore.DataStoreType;
import com.wolvencraft.yasp.db.data.hooks.votifier.VotifierData;
import com.wolvencraft.yasp.settings.Module;
import com.wolvencraft.yasp.util.cache.OnlineSessionCache;

public class VotifierHook extends PluginHook implements Listener {
    
    public VotifierHook() {
        super(Module.Votifier, "Votifier");
    }
    
    @Override
    protected void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, Statistics.getInstance());
    }
    
    @EventHandler(priority=EventPriority.MONITOR)
    public void onVotifierEvent(VotifierEvent event) {
        Vote vote = event.getVote();
        Player player = Bukkit.getPlayerExact(vote.getUsername());
        if(player == null) return;
        VotifierData dataStore = (VotifierData) OnlineSessionCache.fetch(player).getDataStore(DataStoreType.Hook_Votifier);
        if(dataStore == null) return;
        dataStore.playerVoted(vote);
    }
    
}
