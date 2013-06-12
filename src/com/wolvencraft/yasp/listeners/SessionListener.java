/*
 * SessionListener.java
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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.listeners.handlers.HandlerManager;
import com.wolvencraft.yasp.listeners.handlers.SessionHandlers.PlayerLogin;
import com.wolvencraft.yasp.listeners.handlers.SessionHandlers.PlayerLogout;
import com.wolvencraft.yasp.settings.Constants.StatPerms;

public class SessionListener implements Listener {
    
    public SessionListener(Statistics plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(!HandlerManager.playerLookup(player, StatPerms.Statistics)) return;
        HandlerManager.runTask(new PlayerLogin(player));
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(!HandlerManager.playerLookup(player, StatPerms.Statistics)) return;
        HandlerManager.runTask(new PlayerLogout(player));
    }
    
}
