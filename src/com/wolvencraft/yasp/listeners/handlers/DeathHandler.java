/*
 * DeathHandler.java
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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Ambient;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.settings.Constants;
import com.wolvencraft.yasp.settings.Constants.StatPerms;
import com.wolvencraft.yasp.util.cache.OnlineSessionCache;

public class DeathHandler {
    
    /**
     * Executed when a player dies
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class PlayerDeath implements Runnable {
        
        public Player player;
        public PlayerDeathEvent event;
        
        @Override
        public void run() {
            EntityDamageEvent lastDamageEvent = player.getLastDamageCause();
            
            if (lastDamageEvent == null) return;
            DamageCause cause = lastDamageEvent.getCause();
            
            if (lastDamageEvent instanceof EntityDamageByEntityEvent) {
                // Player killed by entity
                Entity killerEntity = ((EntityDamageByEntityEvent) lastDamageEvent).getDamager();

                if (killerEntity instanceof Projectile) {
                // + Player was shot
                    Projectile projectile = (Projectile) killerEntity;
                    if (projectile.getShooter() instanceof Player) {
                // | + Player shot by Player
                        Player killer = (Player) projectile.getShooter();
                        if(!StatPerms.DeathPVP.has(killer) || !StatPerms.DeathPVP.has(player)) return;
                        OnlineSession session = OnlineSessionCache.fetch(killer);
                        session.killedPlayer(player, Constants.ProjectileToItem.parse(projectile.getType()));
                    } else if (projectile.getShooter() instanceof Creature) {
                // | + Player was shot by a monster
                        if(!StatPerms.DeathPVE.has(player)) return;
                        Entity killer = (Entity) projectile.getShooter();
                        OnlineSession session = OnlineSessionCache.fetch(player);
                        session.killedByCreature(killer, Constants.ProjectileToItem.parse(projectile.getType()));
                    }
                } else if (killerEntity instanceof Player) {
                // + Player killed Player
                    Player killer = (Player) killerEntity;
                    if(!StatPerms.DeathPVP.has(killer) || !StatPerms.DeathPVP.has(player)) return;
                    OnlineSession session = OnlineSessionCache.fetch(killer);
                    session.killedPlayer(player, killer.getItemInHand());
                } else if (killerEntity instanceof Creature) {
                // + Creature killed Player
                    if(!StatPerms.DeathPVE.has(player)) return;
                    OnlineSession session = OnlineSessionCache.fetch(player);
                    session.killedByCreature(killerEntity, new ItemStack(Material.AIR));
                } else if (killerEntity instanceof Slime) {
                // + Slime killed player
                    if(!StatPerms.DeathPVE.has(player)) return;
                    OnlineSession session = OnlineSessionCache.fetch(player);
                    session.killedByCreature(killerEntity, new ItemStack(Material.AIR));
                } else if (killerEntity instanceof EnderDragon) {
                // + Ender Dragon killed player
                    if(!StatPerms.DeathPVE.has(player)) return;
                    OnlineSession session = OnlineSessionCache.fetch(player);
                    session.killedByCreature(killerEntity, new ItemStack(Material.AIR));
                } else {
                // + Player died
                    if(!StatPerms.DeathOther.has(player)) return;
                    OnlineSession session = OnlineSessionCache.fetch(player);
                    session.killedByEnvironment(player.getLocation(), cause);
                }
            } else {
                // Player killed by other means
                if(!StatPerms.DeathOther.has(player)) return;
                OnlineSession session = OnlineSessionCache.fetch(player);
                session.killedByEnvironment(player.getLocation(), cause);
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
                // Entity was shot
                Projectile projectile = (Projectile) killerEntity;
                if (!(projectile.getShooter() instanceof Player)) return;
                Player killer = (Player) projectile.getShooter();
                if(!StatPerms.DeathPVE.has(killer)) return;
                if (victim instanceof Creature) {
                    // + Player shot Creature
                    OnlineSessionCache.fetch(killer).killedCreature(victim, Constants.ProjectileToItem.parse(projectile.getType()));
                } else if (victim instanceof Slime) {
                    // + Player shot Slime
                    OnlineSessionCache.fetch(killer).killedCreature(victim, Constants.ProjectileToItem.parse(projectile.getType()));
                } else if (victim instanceof EnderDragon) {
                    // + Player shot EnderDragon
                    OnlineSessionCache.fetch(killer).killedCreature(victim, Constants.ProjectileToItem.parse(projectile.getType()));
                } else if (victim instanceof Ambient) {
                    // + Player shot Bat
                    OnlineSessionCache.fetch(killer).killedCreature(victim, Constants.ProjectileToItem.parse(projectile.getType()));
                }
            } else if (killerEntity instanceof Player) {
                // Player killed an entity
                Player killer = (Player) killerEntity;
                if(!StatPerms.DeathPVE.has(killer)) return;
                if (victim instanceof Creature) {
                    // + Player killed Creature
                    OnlineSessionCache.fetch(killer).killedCreature(victim, killer.getItemInHand());
                } else if (victim instanceof Slime) {
                    // + Player killed Slime
                    OnlineSessionCache.fetch(killer).killedCreature(victim, killer.getItemInHand());
                } else if (victim instanceof EnderDragon) {
                    // + Player killed an EnderDragon
                    OnlineSessionCache.fetch(killer).killedCreature(victim, killer.getItemInHand());
                } else if (victim instanceof Ambient) {
                    // + Player killed an Bat
                    OnlineSessionCache.fetch(killer).killedCreature(victim, killer.getItemInHand());
                }
            }
        }
    }
    
}
