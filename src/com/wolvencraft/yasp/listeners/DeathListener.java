/*
 * DeathListener.javas
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
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.listeners.handlers.DeathHandler.MonsterDeath;
import com.wolvencraft.yasp.listeners.handlers.DeathHandler.PlayerDeath;
import com.wolvencraft.yasp.listeners.handlers.HandlerManager;
import com.wolvencraft.yasp.settings.Constants.StatPerms;

/**
 * Listens to any entity deaths on the server and reports them to the plugin
 * @author bitWolfy
 *
 */
public class DeathListener implements Listener {
    
    public DeathListener(Statistics plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(!HandlerManager.playerLookup(player, StatPerms.Death)) return;
        
        HandlerManager.runAsyncTask(new PlayerDeath(player, event));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMonsterDeath(EntityDeathEvent event) {
        if(Statistics.isPaused()) return;

        HandlerManager.runAsyncTask(new MonsterDeath(event));
    }
}
