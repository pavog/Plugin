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

import org.bukkit.entity.Player;
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
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.db.tables.Normal.PlayerData;
import com.wolvencraft.yasp.listeners.handlers.HandlerManager;
import com.wolvencraft.yasp.listeners.handlers.PlayerHandlers.PlayerMove;
import com.wolvencraft.yasp.listeners.handlers.PlayerHandlers.PlayerIncrementStat;
import com.wolvencraft.yasp.settings.Constants.StatPerms;

/**
 * Listens to miscellaneous player events on the server and reports them to the plugin.
 * @author bitWolfy
 *
 */
public class PlayerListener implements Listener {
    
    public PlayerListener(Statistics plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if(event instanceof PlayerTeleportEvent) return;
        
        Player player = event.getPlayer();
        if(!HandlerManager.playerLookup(player, StatPerms.PlayerDistances)) return;
        
        HandlerManager.runAsyncTask(new PlayerMove(player, event.getFrom(), event.getTo()));
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerFish(PlayerFishEvent event) {
        if(!event.getState().equals(State.CAUGHT_FISH)) return;
        
        Player player = event.getPlayer();
        if(!HandlerManager.playerLookup(player, StatPerms.PlayerMisc)) return;
        
        HandlerManager.runAsyncTask(new PlayerIncrementStat(player, PlayerData.FishCaught));
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        if(!HandlerManager.playerLookup(player, StatPerms.PlayerMisc)) return;
        
        HandlerManager.runAsyncTask(new PlayerIncrementStat(player, PlayerData.TimesKicked));
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEggThrow(PlayerEggThrowEvent event) {
        Player player = event.getPlayer();
        if(!HandlerManager.playerLookup(player, StatPerms.PlayerMisc)) return;

        HandlerManager.runAsyncTask(new PlayerIncrementStat(player, PlayerData.EggsThrown));
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onArrowShoot(EntityShootBowEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if(!HandlerManager.playerLookup(player, StatPerms.PlayerMisc)) return;

        HandlerManager.runAsyncTask(new PlayerIncrementStat(player, PlayerData.ArrowsShot));
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        
        Player player = (Player) event.getEntity();
        if(!HandlerManager.playerLookup(player, StatPerms.PlayerMisc)) return;

        HandlerManager.runAsyncTask(new PlayerIncrementStat(player, PlayerData.DamageTaken, event.getDamage()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBedEnter(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        if(!HandlerManager.playerLookup(player, StatPerms.PlayerMisc)) return;

        HandlerManager.runAsyncTask(new PlayerIncrementStat(player, PlayerData.BedsEntered));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPortalEnter(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        if(!HandlerManager.playerLookup(player, StatPerms.PlayerMisc)) return;

        HandlerManager.runAsyncTask(new PlayerIncrementStat(player, PlayerData.PortalsEntered));
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChatMessage(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(!HandlerManager.playerLookup(player, StatPerms.PlayerMisc)) return;

        HandlerManager.runAsyncTask(new PlayerIncrementStat(player, PlayerData.WordsSaid, event.getMessage().split(" ").length));
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChatCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if(!HandlerManager.playerLookup(player, StatPerms.PlayerMisc)) return;
        
        HandlerManager.runAsyncTask(new PlayerIncrementStat(player, PlayerData.CommandsSent));
    }
}
