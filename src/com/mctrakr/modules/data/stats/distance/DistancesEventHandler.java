/*
 * DistancesEventHandler.java
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

package com.mctrakr.modules.data.stats.distance;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import com.mctrakr.cache.SessionCache;
import com.mctrakr.modules.data.stats.distance.Tables.DistancesTable;
import com.mctrakr.modules.data.stats.misc.MiscDataStore;
import com.mctrakr.modules.data.stats.misc.Tables.MiscInfoTable;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock.PrimaryType;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class DistancesEventHandler {
    
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
            OnlineSession session = SessionCache.fetch(player);
            
            Location playerLocation = player.getLocation();
            if(!playerLocation.getWorld().equals(event.getTo().getWorld())) return;
            double distance = playerLocation.distance(event.getTo());
            if(player.isInsideVehicle()) {
                EntityType vehicle = player.getVehicle().getType();
                distance = player.getVehicle().getLocation().distance(event.getTo());
                if(vehicle.equals(EntityType.MINECART)) {
                    addDistance(session, DistancesTable.Minecart, distance);
                } else if(vehicle.equals(EntityType.BOAT)) {
                    addDistance(session, DistancesTable.Boat, distance);
                } else if(vehicle.equals(EntityType.PIG) || vehicle.equals(EntityType.HORSE)) {
                    addDistance(session, DistancesTable.Ride, distance);
                }
            } else if (playerLocation.getBlock().getType().equals(Material.WATER) || playerLocation.getBlock().getType().equals(Material.STATIONARY_WATER)) {
                addDistance(session, DistancesTable.Swim, distance);
            } else if (player.isFlying()) {
                addDistance(session, DistancesTable.Flight, distance);
            } else {
                if(playerLocation.getY() < event.getTo().getY() && event.getTo().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR)) {
                    MiscDataStore misc = ((MiscDataStore) session.getDataStore(PrimaryType.Misc));
                    if(misc != null) misc.getNormalData().incrementStat(MiscInfoTable.TimesJumped);
                }
                addDistance(session, DistancesTable.Foot, distance);
            }
        }
        
        /**
         * Add distance of the specified type to the statistics
         * @param session Player session
         * @param type Travel type
         * @param distance Distance traveled
         */
        private static void addDistance(OnlineSession session, DistancesTable type, double distance) {
            DistancesDataStore distances = ((DistancesDataStore) session.getDataStore(PrimaryType.Distance));
            if(distances != null) distances.playerTravel(type, distance);
            session.getPlayerTotals().addDistance(type, distance);
        }
    }
}
