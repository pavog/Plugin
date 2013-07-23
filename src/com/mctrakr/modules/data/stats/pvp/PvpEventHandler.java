/*
 * PvpEventHandler.java
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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.mctrakr.cache.SessionCache;
import com.mctrakr.modules.data.stats.misc.MiscDataStore;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock.PrimaryType;
import com.mctrakr.settings.Constants;
import com.mctrakr.settings.Constants.StatPerms;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class PvpEventHandler {
    
    /**
     * Executed when a player dies
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class PlayerPvpDeath implements Runnable {
        
        public Player player;
        public EntityDamageByEntityEvent lastDamageEvent;
        
        @Override
        public void run() {
            Entity killerEntity = ((EntityDamageByEntityEvent) lastDamageEvent).getDamager();

            if (killerEntity instanceof Projectile) {
                Projectile projectile = (Projectile) killerEntity;
                if (projectile.getShooter() instanceof Player) {
                    Player killer = (Player) projectile.getShooter();
                    if(!StatPerms.DeathPVP.has(killer) || !StatPerms.DeathPVP.has(player)) return;
                    killedPlayer(killer, player, Constants.ProjectileToItem.parse(projectile.getType()));
                } else return;
            } else if (killerEntity instanceof Player) {
                Player killer = (Player) killerEntity;
                if(!StatPerms.DeathPVP.has(killer) || !StatPerms.DeathPVP.has(player)) return;
                killedPlayer(killer, player, killer.getItemInHand());
            }
        }
    }
    
    /**
     * Registers the player death in the data store
     * @param victim Player who was killed 
     * @param weapon Weapon used by killer
     */
    private static void killedPlayer(Player killer, Player victim, ItemStack weapon) {
        OnlineSession killerSession = SessionCache.fetch(killer);
        if(killerSession == null) return;
        
        PvpDataStore pvpStore = ((PvpDataStore) killerSession.getDataStore(PrimaryType.PVP));
        if(pvpStore != null) pvpStore.playerKilledPlayer(victim, weapon);
        
        MiscDataStore misc = ((MiscDataStore) killerSession.getDataStore(PrimaryType.Misc));
        if(misc != null) misc.getNormalData().killed(victim);
        
        killerSession.getPlayerTotals().pvpKill();
        SessionCache.fetch(victim).getPlayerTotals().death();
    }
    
}
