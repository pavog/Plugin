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

import com.wolvencraft.yasp.db.tables.Normal.PlayerDistance;
import com.wolvencraft.yasp.db.tables.Normal.PlayerData;
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
                    OnlineSessionCache.fetch(player).addDistance(PlayerDistance.Minecart, distance);
                } else if(vehicle.getType().equals(EntityType.BOAT)) {
                    OnlineSessionCache.fetch(player).addDistance(PlayerDistance.Boat, distance);
                } else if(vehicle.getType().equals(EntityType.PIG)) {
                    OnlineSessionCache.fetch(player).addDistance(PlayerDistance.Pig, distance);
                }
            } else if (playerLocation.getBlock().getType().equals(Material.WATER) || playerLocation.getBlock().getType().equals(Material.STATIONARY_WATER)) {
                OnlineSessionCache.fetch(player).addDistance(PlayerDistance.Swim, distance);
            } else if (player.isFlying()) {
                OnlineSessionCache.fetch(player).addDistance(PlayerDistance.Flight, distance);
            } else {
                OnlineSession session = OnlineSessionCache.fetch(player);
                if(playerLocation.getY() < event.getTo().getY() && event.getTo().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR))
                    session.getPlayersData().getMiscData().incrementStat(PlayerData.TimesJumped);
                OnlineSessionCache.fetch(player).addDistance(PlayerDistance.Foot, distance);
            }
        }
    }

    /**
     * Executed when a player's stat has to be incremented asynchronously
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class PlayerIncrementStat implements Runnable {

        private Player player;
        private PlayerData stat;
        private double value;
        
        public PlayerIncrementStat(Player player, PlayerData stat) {
            this.player = player;
            this.stat = stat;
            this.value = 1;
        }
        
        @Override
        public void run() {
            OnlineSessionCache
                .fetch(player)
                .getPlayersData()
                .getMiscData()
                .incrementStat(stat, value);
        }
    }
}
