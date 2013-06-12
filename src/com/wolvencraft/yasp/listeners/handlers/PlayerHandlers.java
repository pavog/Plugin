/*
 * PlayersHandler.java
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

package com.wolvencraft.yasp.listeners.handlers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.player.PlayerMoveEvent;

import com.wolvencraft.yasp.db.tables.Normal.DistancePlayersTable;
import com.wolvencraft.yasp.db.tables.Normal.MiscInfoPlayersTable;
import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.util.cache.OnlineSessionCache;

public class PlayerHandlers {
    
    /**
     * Executed when a player moves
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class PlayerMove implements Runnable {
        
        private PlayerMoveEvent event;
        
        @Override
        public void run() {
            Player player = event.getPlayer();
            
            Location playerLocation = player.getLocation();
            if(!playerLocation.getWorld().equals(event.getTo().getWorld())) return;
            double distance = playerLocation.distance(event.getTo());
            if(player.isInsideVehicle()) {
                Vehicle vehicle = (Vehicle) player.getVehicle();
                distance = vehicle.getLocation().distance(event.getTo());
                if(vehicle.getType().equals(EntityType.MINECART)) {
                    OnlineSessionCache.fetch(player).addDistance(DistancePlayersTable.Minecart, distance);
                } else if(vehicle.getType().equals(EntityType.BOAT)) {
                    OnlineSessionCache.fetch(player).addDistance(DistancePlayersTable.Boat, distance);
                } else if(vehicle.getType().equals(EntityType.PIG)) {
                    OnlineSessionCache.fetch(player).addDistance(DistancePlayersTable.Pig, distance);
                }
            } else if (playerLocation.getBlock().getType().equals(Material.WATER) || playerLocation.getBlock().getType().equals(Material.STATIONARY_WATER)) {
                OnlineSessionCache.fetch(player).addDistance(DistancePlayersTable.Swim, distance);
            } else if (player.isFlying()) {
                OnlineSessionCache.fetch(player).addDistance(DistancePlayersTable.Flight, distance);
            } else {
                OnlineSession session = OnlineSessionCache.fetch(player);
                if(playerLocation.getY() < event.getTo().getY() && event.getTo().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR))
                    session.addMiscValue(MiscInfoPlayersTable.TimesJumped);
                OnlineSessionCache.fetch(player).addDistance(DistancePlayersTable.Foot, distance);
            }
        }
    }
    
    /**
     * Executed when a player has catches a fish
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class PlayerFish implements Runnable {
        
        private Player player;
        
        @Override
        public void run() {
            OnlineSessionCache.fetch(player).addMiscValue(MiscInfoPlayersTable.FishCaught);
        }
    }
    
    /**
     * Executed when a player is kicked
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class PlayerKicked implements Runnable {
        
        private Player player;
        
        @Override
        public void run() {
            OnlineSessionCache.fetch(player).addMiscValue(MiscInfoPlayersTable.TimesKicked);
        }
    }
    
    /**
     * Executed when a player throws an egg
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class PlayerEggThrow implements Runnable {
        
        private Player player;
        
        @Override
        public void run() {
            OnlineSessionCache.fetch(player).addMiscValue(MiscInfoPlayersTable.EggsThrown);
        }
    }
    
    /**
     * Executed when a player shoots an error
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class PlayerArrowShoot implements Runnable {
        
        private Player player;
        
        @Override
        public void run() {
            OnlineSessionCache.fetch(player).addMiscValue(MiscInfoPlayersTable.ArrowsShot);
        }
    }
    
    /**
     * Executed when a player takes damage
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class PlayerDamage implements Runnable {
        
        private Player player;
        private int damage;
        
        @Override
        public void run() {
            OnlineSessionCache.fetch(player).addMiscValue(MiscInfoPlayersTable.DamageTaken, damage);
        }
    }
    
    /**
     * Executed when a player enters a bed
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class PlayerBedEnter implements Runnable {
        
        private Player player;
        
        @Override
        public void run() {
            OnlineSessionCache.fetch(player).addMiscValue(MiscInfoPlayersTable.BedsEntered);
        }
    }
    
    /**
     * Executed when a player enters a portal
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class PlayerPortalEnter implements Runnable {
        
        private Player player;
        
        @Override
        public void run() {
            OnlineSessionCache.fetch(player).addMiscValue(MiscInfoPlayersTable.PortalsEntered);
        }
    }
    
    /**
     * Executed when a player sends a message in chat
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class PlayerChatMessage implements Runnable {
        
        private Player player;
        private String message;
        
        @Override
        public void run() {
            OnlineSessionCache.fetch(player).addMiscValue(MiscInfoPlayersTable.WordsSaid, message.split(" ").length);
        }
    }
    
    /**
     * Executed when a player sends a command
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class PlayerCommandSend implements Runnable {
        
        private Player player;
        
        @Override
        public void run() {
            OnlineSessionCache.fetch(player).addMiscValue(MiscInfoPlayersTable.CommandsSent);
        }
    }
    
}
