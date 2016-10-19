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

import com.wolvencraft.yasp.db.tables.Normal.PlayerData;
import com.wolvencraft.yasp.db.tables.Normal.PlayerDistance;
import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.util.cache.OnlineSessionCache;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class PlayerHandlers {

    /**
     * Executed when a player moves
     *
     * @author bitWolfy
     */
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class PlayerMove implements Runnable {

        private Player player;
        private Location from;
        private Location to;

        @Override
        public void run() {
            OnlineSession session = OnlineSessionCache.fetch(player);
            //Skip data tracking if not all players data is read from database
            if (session.isReady()) {
                if (!from.getWorld().equals(to.getWorld())) return;
                double distance = from.distance(to);
                if (player.isInsideVehicle()) {
                    EntityType vehicle = player.getVehicle().getType();
                    if (vehicle.equals(EntityType.MINECART)) {
                        session.addDistance(PlayerDistance.Minecart, distance);
                    } else if (vehicle.equals(EntityType.BOAT)) {
                        session.addDistance(PlayerDistance.Boat, distance);
                    } else if (vehicle.equals(EntityType.PIG) || vehicle.equals(EntityType.HORSE)) {
                        session.addDistance(PlayerDistance.Ride, distance);
                    }
                } else if (from.getBlock().getType().equals(Material.WATER) || from.getBlock().getType().equals(Material.STATIONARY_WATER)) {
                    session.addDistance(PlayerDistance.Swim, distance);
                } else if (player.isFlying()) {
                    session.addDistance(PlayerDistance.Flight, distance);
                } else {
                    if (from.getY() < to.getY() && to.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR))
                        session.getPlayersData().getMiscData().incrementStat(PlayerData.TimesJumped);
                    session.addDistance(PlayerDistance.Foot, distance);
                }
            }
        }
    }

    /**
     * Executed when a player's stat has to be incremented asynchronously
     *
     * @author bitWolfy
     */
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
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
            OnlineSession session = OnlineSessionCache.fetch(player);
            if (session.isReady()) {
                session
                        .getPlayersData()
                        .getMiscData()
                        .incrementStat(stat, value);
            }
        }
    }
}
