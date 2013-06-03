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
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.wolvencraft.yasp.db.tables.Normal.DistancePlayersTable;
import com.wolvencraft.yasp.db.tables.Normal.MiscInfoPlayersTable;
import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.settings.Constants.StatPerms;
import com.wolvencraft.yasp.settings.RemoteConfiguration;
import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.cache.OnlineSessionCache;

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
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
//        PlayerCache.add(event.getName());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Statistics.getServerStatistics().playerLogin();
        Player player = event.getPlayer();
        if(!StatPerms.Statistics.has(player)) return;
        OnlineSessionCache.fetch(player).login(player.getLocation());
        
        if(RemoteConfiguration.ShowWelcomeMessages.asBoolean()) {
            Message.send(
                player,
                RemoteConfiguration.WelcomeMessage.asString().replace("<PLAYER>", player.getPlayerListName())
            );
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(!StatPerms.Statistics.has(player)) return;
        OnlineSessionCache.fetch(player).logout(player.getLocation());
//        PlayerCache.remove(player.getName());
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if(Statistics.isPaused()) return;
        if(event instanceof PlayerTeleportEvent) return;
        
        Player player = event.getPlayer();
        if(!StatPerms.PlayerDistances.has(player)) return;
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
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerFish(PlayerFishEvent event) {
        if(Statistics.isPaused()) return;
        if(!event.getState().equals(State.CAUGHT_FISH)) return;
        Player player = event.getPlayer();
        if(!StatPerms.PlayerMisc.has(player)) return;
        OnlineSessionCache.fetch(player).addMiscValue(MiscInfoPlayersTable.FishCaught);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {
        if(Statistics.isPaused()) return;
        Player player = event.getPlayer();
        if(!StatPerms.PlayerMisc.has(player)) return;
        OnlineSessionCache.fetch(player).addMiscValue(MiscInfoPlayersTable.TimesKicked);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEggThrow(PlayerEggThrowEvent event) {
        if(Statistics.isPaused()) return;
        Player player = event.getPlayer();
        if(!StatPerms.PlayerMisc.has(player)) return;
        OnlineSessionCache.fetch(player).addMiscValue(MiscInfoPlayersTable.EggsThrown);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onArrowShoot(EntityShootBowEvent event) {
        if(Statistics.isPaused()) return;
        if(!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if(!StatPerms.PlayerMisc.has(player)) return;
        OnlineSessionCache.fetch(player).addMiscValue(MiscInfoPlayersTable.ArrowsShot);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent event) {
        if(Statistics.isPaused()) return;
        if(!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if(!StatPerms.PlayerMisc.has(player)) return;
        OnlineSessionCache.fetch(player).addMiscValue(MiscInfoPlayersTable.DamageTaken, event.getDamage());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBedEnter(PlayerBedEnterEvent event) {
        if(Statistics.isPaused()) return;
        Player player = event.getPlayer();
        if(!StatPerms.PlayerMisc.has(player)) return;
        OnlineSessionCache.fetch(player).addMiscValue(MiscInfoPlayersTable.BedsEntered);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPortalEnter(PlayerPortalEvent event) {
        if(Statistics.isPaused()) return;
        Player player = event.getPlayer();
        if(!StatPerms.PlayerMisc.has(player)) return;
        OnlineSessionCache.fetch(player).addMiscValue(MiscInfoPlayersTable.PortalsEntered);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChatMessage(AsyncPlayerChatEvent event) {
        if(Statistics.isPaused()) return;
        Player player = event.getPlayer();
        if(!StatPerms.PlayerMisc.has(player)) return;
        OnlineSessionCache.fetch(player).addMiscValue(MiscInfoPlayersTable.WordsSaid, event.getMessage().split(" ").length);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChatCommand(PlayerCommandPreprocessEvent event) {
        if(Statistics.isPaused()) return;
        Player player = event.getPlayer();
        if(!StatPerms.PlayerMisc.has(player)) return;
        OnlineSessionCache.fetch(player).addMiscValue(MiscInfoPlayersTable.CommandsSent);
    }
}
