/*
 * PvpEventListener.java
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

package com.mctrakr.modules.data.stats.pvp;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.mctrakr.Statistics;
import com.mctrakr.modules.data.stats.pvp.PvpEventHandler.PlayerPvpDeath;
import com.mctrakr.managers.HandlerManager;
import com.mctrakr.settings.Constants.StatPerms;

public class PvpEventListener implements Listener {
    
    public PvpEventListener() {
        Statistics plugin = Statistics.getInstance();
        if(plugin == null) return;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(!HandlerManager.playerLookup(player, StatPerms.Death)) return;
        
        EntityDamageEvent lastDamageEvent = event.getEntity().getLastDamageCause();
        if (lastDamageEvent == null) return;
        if(!(lastDamageEvent instanceof EntityDamageByEntityEvent)) return;
        
        Entity killerEntity = ((EntityDamageByEntityEvent) lastDamageEvent).getDamager();
        if(!(killerEntity instanceof Player) ||
                (killerEntity instanceof Projectile
                        && !(((Projectile)(killerEntity)).getShooter() instanceof Player))) return;
        
        HandlerManager.runAsyncTask(new PlayerPvpDeath(player, (EntityDamageByEntityEvent) lastDamageEvent));
    }
    
}
