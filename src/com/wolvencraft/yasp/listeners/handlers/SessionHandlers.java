package com.wolvencraft.yasp.listeners.handlers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.util.cache.OnlineSessionCache;

public class SessionHandlers {
    
    /**
     * Executed on player login
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class PlayerLogin implements Runnable {
        
        private Player player;
        
        @Override
        public void run() {
            Statistics.getServerStatistics().playerLogin();
            OnlineSessionCache.fetch(player).login(player.getLocation());
        }
    }
    
    /**
     * Executed on player logout
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class PlayerLogout implements Runnable {
        
        private Player player;
        
        @Override
        public void run() {
            OnlineSessionCache.fetch(player).logout(player.getLocation());
        }
    }
    
}
