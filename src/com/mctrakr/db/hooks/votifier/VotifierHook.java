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

package com.mctrakr.db.hooks.votifier;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.mctrakr.Statistics;
import com.mctrakr.db.hooks.PluginHook;
import com.mctrakr.util.cache.SessionCache;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

public class VotifierHook extends PluginHook implements Listener {
    
    private static final String PLUGIN_NAME = "Votifier";
    
    public VotifierHook() {
        super(VotifierDataStore.lock, PLUGIN_NAME);
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
        VotifierDataStore dataStore = (VotifierDataStore) SessionCache.fetch(player).getDataStore("votifier");
        if(dataStore == null) return;
        dataStore.playerVoted(vote);
    }
    
}
