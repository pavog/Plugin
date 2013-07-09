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

package com.mctrakr.listeners.handlers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import com.mctrakr.db.data.DataStore.ModuleType;
import com.mctrakr.db.data.distance.Tables.DistancesTable;
import com.mctrakr.db.data.misc.MiscDataStore;
import com.mctrakr.db.data.misc.Tables.MiscInfoTable;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.util.cache.SessionCache;

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
                EntityType vehicle = player.getVehicle().getType();
                distance = player.getVehicle().getLocation().distance(event.getTo());
                if(vehicle.equals(EntityType.MINECART)) {
                    SessionCache.fetch(player).addDistance(DistancesTable.Minecart, distance);
                } else if(vehicle.equals(EntityType.BOAT)) {
                    SessionCache.fetch(player).addDistance(DistancesTable.Boat, distance);
                } else if(vehicle.equals(EntityType.PIG) || vehicle.equals(EntityType.HORSE)) {
                    SessionCache.fetch(player).addDistance(DistancesTable.Ride, distance);
                }
            } else if (playerLocation.getBlock().getType().equals(Material.WATER) || playerLocation.getBlock().getType().equals(Material.STATIONARY_WATER)) {
                SessionCache.fetch(player).addDistance(DistancesTable.Swim, distance);
            } else if (player.isFlying()) {
                SessionCache.fetch(player).addDistance(DistancesTable.Flight, distance);
            } else {
                OnlineSession session = SessionCache.fetch(player);
                if(playerLocation.getY() < event.getTo().getY() && event.getTo().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR))
                    ((MiscDataStore) session.getDataStore(ModuleType.Misc)).get().incrementStat(MiscInfoTable.TimesJumped);
                SessionCache.fetch(player).addDistance(DistancesTable.Foot, distance);
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
        private MiscInfoTable stat;
        private double value;
        
        public PlayerIncrementStat(Player player, MiscInfoTable stat) {
            this.player = player;
            this.stat = stat;
            this.value = 1;
        }
        
        @Override
        public void run() {
            ((MiscDataStore) SessionCache.fetch(player).getDataStore(ModuleType.Misc)).get().incrementStat(stat, value);
        }
    }
}
