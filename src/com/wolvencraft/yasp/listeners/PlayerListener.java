/*
 * PlayerListener.java
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

package com.wolvencraft.yasp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.db.tables.Normal.DistancePlayersTable;
import com.wolvencraft.yasp.db.tables.Normal.MiscInfoPlayersTable;
import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.Settings;
import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.util.ConfirmationTimer;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.Util;

/**
 * Listens to miscellaneous player events on the server and reports them to the plugin.
 * @author bitWolfy
 *
 */
public class PlayerListener implements Listener {
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new instance of the Listener and registers it with the PluginManager
     * @param plugin StatsPlugin instance
     */
    public PlayerListener(Statistics plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(Statistics.getPaused()) return;
        DataCollector.getStats().playerLogin();
        Player player = event.getPlayer();
        if(!Util.isTracked(player)) return;
        OnlineSession session = DataCollector.get(player);
        session.login(player.getLocation());
        
        long delay = Settings.RemoteConfiguration.LogDelay.asInteger();
        if(delay != 0 && session.getPlaytime() < delay) {
            session.setConfirmed(false);
            Bukkit.getScheduler().runTaskLater(Statistics.getInstance(), new ConfirmationTimer(session), delay * 20L);
            return;
        }
        
        if(Settings.RemoteConfiguration.ShowWelcomeMessages.asBoolean()) {
            Message.send(
                player,
                Settings.RemoteConfiguration.WelcomeMessage.asString().replace("<PLAYER>", player.getPlayerListName())
            );
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(Statistics.getPaused()) return;
        Player player = event.getPlayer();
        if(!Util.isTracked(player)) return;
        DataCollector.get(player).logout(player.getLocation());
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if(Statistics.getPaused()) return;
        Player player = event.getPlayer();
        if(!Util.isTracked(player, "move")) return;
        Location playerLocation = player.getLocation();
        if(!playerLocation.getWorld().equals(event.getTo().getWorld())) return;
        double distance = playerLocation.distance(event.getTo());
        if(player.isInsideVehicle()) {
            Vehicle vehicle = (Vehicle) player.getVehicle();
            if(vehicle.getType().equals(EntityType.MINECART)) {
                DataCollector.get(player).addDistance(DistancePlayersTable.Minecart, distance);
            } else if(vehicle.getType().equals(EntityType.BOAT)) {
                DataCollector.get(player).addDistance(DistancePlayersTable.Boat, distance);
            } else if(vehicle.getType().equals(EntityType.PIG)) {
                DataCollector.get(player).addDistance(DistancePlayersTable.Pig, distance);
            }
        } else if (playerLocation.getBlock().getType().equals(Material.WATER) || playerLocation.getBlock().getType().equals(Material.STATIONARY_WATER)) {
            DataCollector.get(player).addDistance(DistancePlayersTable.Swimmed, distance);
        } else if (player.isFlying()) {
            DataCollector.get(player).addDistance(DistancePlayersTable.Flight, distance);
        } else {
            OnlineSession session = DataCollector.get(player);
            if(playerLocation.getY() < event.getTo().getY() && event.getTo().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR))
                session.addMiscValue(MiscInfoPlayersTable.TimesJumped);
            DataCollector.get(player).addDistance(DistancePlayersTable.Foot, distance);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerFish(PlayerFishEvent event) {
        if(Statistics.getPaused()) return;
        Player player = event.getPlayer();
        if(!Util.isTracked(player, "misc.fish")) return;
        DataCollector.get(player).addMiscValue(MiscInfoPlayersTable.FishCaught);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {
        if(Statistics.getPaused()) return;
        Player player = event.getPlayer();
        if(!Util.isTracked(player, "misc.kick")) return;
        DataCollector.get(player).addMiscValue(MiscInfoPlayersTable.TimesKicked);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEggThrow(PlayerEggThrowEvent event) {
        if(Statistics.getPaused()) return;
        Player player = event.getPlayer();
        if(!Util.isTracked(player, "misc.eggThrow")) return;
        DataCollector.get(player).addMiscValue(MiscInfoPlayersTable.EggsThrown);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onArrowShoot(EntityShootBowEvent event) {
        if(Statistics.getPaused()) return;
        if(!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if(!Util.isTracked(player, "misc.arrowShoot")) return;
        DataCollector.get(player).addMiscValue(MiscInfoPlayersTable.ArrowsShot);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent event) {
        if(Statistics.getPaused()) return;
        if(!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if(!Util.isTracked(player, "misc.takeDamage")) return;
        DataCollector.get(player).addMiscValue(MiscInfoPlayersTable.DamageTaken, event.getDamage());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBedEnter(PlayerBedEnterEvent event) {
        if(Statistics.getPaused()) return;
        Player player = event.getPlayer();
        if(!Util.isTracked(player, "misc.bedEnter")) return;
        DataCollector.get(player).addMiscValue(MiscInfoPlayersTable.BedsEntered);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPortalEnter(PlayerPortalEvent event) {
        if(Statistics.getPaused()) return;
        Player player = event.getPlayer();
        if(!Util.isTracked(player, "misc.portalEnter")) return;
        DataCollector.get(player).addMiscValue(MiscInfoPlayersTable.PortalsEntered);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChatMessage(AsyncPlayerChatEvent event) {
        if(Statistics.getPaused()) return;
        Player player = event.getPlayer();
        if(!Util.isTracked(player, "misc.chat")) return;
        DataCollector.get(player).addMiscValue(MiscInfoPlayersTable.WordsSaid, event.getMessage().split(" ").length);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChatCommand(PlayerCommandPreprocessEvent event) {
        if(Statistics.getPaused()) return;
        Player player = event.getPlayer();
        if(!Util.isTracked(player, "misc.command")) return;
        DataCollector.get(player).addMiscValue(MiscInfoPlayersTable.CommandsSent);
    }
}
