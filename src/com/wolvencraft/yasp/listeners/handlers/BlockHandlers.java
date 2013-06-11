package com.wolvencraft.yasp.listeners.handlers;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.util.cache.OnlineSessionCache;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

public class BlockHandlers {
    
    /**
     * Executed when a player breaks a block
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class BlockBreak implements Runnable {
        
        private Player player;
        private Block block;
        
        @Override
        public void run() {
            OnlineSessionCache.fetch(player).blockBreak(block.getState());
        }
        
    }
    
    /**
     * Executed when a player places a block
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class BlockPlace implements Runnable {

        private Player player;
        private Block block;
        
        @Override
        public void run() {
            OnlineSessionCache.fetch(player).blockPlace(block.getState());
        }
        
    }
    
}
