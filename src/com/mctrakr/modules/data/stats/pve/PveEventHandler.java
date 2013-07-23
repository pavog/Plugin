/*
 * PveEventHandler.java
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

package com.mctrakr.modules.data.stats.pve;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.mctrakr.cache.SessionCache;
import com.mctrakr.modules.data.stats.misc.MiscDataStore;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.Constants;
import com.mctrakr.settings.ConfigLock.PrimaryType;
import com.mctrakr.settings.Constants.StatPerms;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class PveEventHandler {
    
    /**
     * Executed when a player dies
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class PlayerPveDeath implements Runnable {
        
        public Player player;
        public EntityDamageByEntityEvent lastDamageEvent;
        
        @Override
        public void run() {
            Entity killerEntity = ((EntityDamageByEntityEvent) lastDamageEvent).getDamager();
            
            if (killerEntity instanceof Projectile) {
                Projectile projectile = (Projectile) killerEntity;
                if (projectile.getShooter() instanceof Creature) {
                    Entity killer = (Entity) projectile.getShooter();
                    killedByCreature(player, killer, Constants.ProjectileToItem.parse(projectile.getType()));
                } else return;
           } else if (killerEntity instanceof Creature
                   || killerEntity instanceof Slime
                   || killerEntity instanceof EnderDragon) {
                killedByCreature(player, killerEntity, new ItemStack(Material.AIR));
            }
        }
    }
    
    /**
     * Executed when a monster dies
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class MonsterDeath implements Runnable {
        
        private EntityDeathEvent event;
        
        @Override
        public void run() {
            Entity victim = event.getEntity();
            EntityDamageEvent lastDamageEvent = victim.getLastDamageCause();
            if (lastDamageEvent == null) return;
            
            if (!(lastDamageEvent instanceof EntityDamageByEntityEvent)) return;
            
            Entity killerEntity = ((EntityDamageByEntityEvent) lastDamageEvent).getDamager();
            
            if (killerEntity instanceof Projectile) {
                Projectile projectile = (Projectile) killerEntity;
                if (!(projectile.getShooter() instanceof Player)) return;
                Player killer = (Player) projectile.getShooter();
                if(!StatPerms.DeathPVE.has(killer)) return;
                if (victim instanceof Creature
                        || victim instanceof Slime
                        || victim instanceof EnderDragon) {
                    // + Player shot Creature
                    killedCreature(killer, victim, Constants.ProjectileToItem.parse(projectile.getType()));
                }
            } else if (killerEntity instanceof Player) {
                // Player killed an entity
                Player killer = (Player) killerEntity;
                if(!StatPerms.DeathPVE.has(killer)) return;
                if (victim instanceof Creature
                        || victim instanceof Slime
                        || victim instanceof EnderDragon) {
                    killedCreature(killer, victim, killer.getItemInHand());
                }
            }
        }
    }
    
    /**
     * Registers the creature death in the data store
     * @param victim Creature killed
     * @param weapon Weapon used by killer
     */
    private static void killedCreature(Player killer, Entity victim, ItemStack weapon) {
        OnlineSession killerSession = SessionCache.fetch(killer);
        if(killerSession == null) return;
        PveDataStore pveStore = ((PveDataStore) killerSession.getDataStore(PrimaryType.PVE));
        if(pveStore != null) pveStore.playerKilledCreature(victim, weapon);
        killerSession.getPlayerTotals().pveKill();
    }
    
    /**
     * Registers the player death in the data store
     * @param killer Creature that killed the player
     * @param weapon Weapon used by killer
     */
    private static void killedByCreature(Player victim, Entity killer, ItemStack weapon) {
        OnlineSession victimSession = SessionCache.fetch(victim);
        if(victimSession == null) return;
        
        PveDataStore pveStore = ((PveDataStore) victimSession.getDataStore(PrimaryType.PVE));
        if(pveStore != null) pveStore.creatureKilledPlayer(killer, weapon);
        
        MiscDataStore misc = ((MiscDataStore) victimSession.getDataStore(PrimaryType.Misc));
        if(misc != null) misc.getNormalData().died();
        
        victimSession.getPlayerTotals().death();
    }
}
