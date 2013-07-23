/*
 * DeathsEventHandler.java
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

package com.mctrakr.modules.data.stats.deaths;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.mctrakr.cache.SessionCache;
import com.mctrakr.modules.data.stats.misc.MiscDataStore;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock.PrimaryType;
import com.mctrakr.settings.Constants.StatPerms;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class DeathsEventHandler {
    
    /**
     * Executed when a player dies
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class PlayerNaturalDeath implements Runnable {
        
        public Player player;
        public EntityDamageEvent lastDamageEvent;
        
        @Override
        public void run() {
            DamageCause cause = lastDamageEvent.getCause();
            
            if (lastDamageEvent instanceof EntityDamageByEntityEvent) return;
            
            if(!StatPerms.DeathOther.has(player)) return;
            OnlineSession playerSession = SessionCache.fetch(player);
            
            DeathsDataStore deaths = ((DeathsDataStore) playerSession.getDataStore(PrimaryType.Deaths));
            if(deaths != null) deaths.playerDied(player.getLocation(), cause);
            
            MiscDataStore misc = ((MiscDataStore) playerSession.getDataStore(PrimaryType.Misc));
            if(misc != null) misc.getNormalData().died();
            
            playerSession.getPlayerTotals().death();
        }
    }
    
}
