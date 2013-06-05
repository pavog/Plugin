package com.wolvencraft.yasp.util.hooks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.HookManager.ApplicableHook;
import com.wolvencraft.yasp.db.data.AdvancedDataStore.DataStoreType;
import com.wolvencraft.yasp.db.data.hooks.votifier.VotifierData;
import com.wolvencraft.yasp.util.cache.OnlineSessionCache;

public class VotifierHook extends PluginHook implements Listener {
    
    public VotifierHook() {
        super(ApplicableHook.VOTIFIER);
    }
    
    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, Statistics.getInstance());
    }
    
    @EventHandler(priority=EventPriority.MONITOR)
    public void onVotifierEvent(VotifierEvent event) {
        Vote vote = event.getVote();
        Player player = Bukkit.getPlayerExact(vote.getUsername());
        if(player == null) return;
        VotifierData dataStore = (VotifierData) OnlineSessionCache.fetch(player).getData(DataStoreType.Hook_Votifier);
        if(dataStore == null) return;
        dataStore.playerVoted(vote);
    }
    
}
