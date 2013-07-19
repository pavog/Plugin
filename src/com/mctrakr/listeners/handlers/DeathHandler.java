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

package com.mctrakr.listeners.handlers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.mctrakr.cache.SessionCache;
import com.mctrakr.modules.stats.deaths.DeathsDataStore;
import com.mctrakr.modules.stats.misc.MiscDataStore;
import com.mctrakr.modules.stats.pve.PveDataStore;
import com.mctrakr.modules.stats.pvp.PvpDataStore;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.Constants;
import com.mctrakr.settings.ConfigLock.PrimaryType;
import com.mctrakr.settings.Constants.StatPerms;

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
                        killedPlayer(SessionCache.fetch(killer), player, Constants.ProjectileToItem.parse(projectile.getType()));
                    } else if (projectile.getShooter() instanceof Creature) {
                // | + Player was shot by a monster
                        if(!StatPerms.DeathPVE.has(player)) return;
                        Entity killer = (Entity) projectile.getShooter();
                        killedByCreature(SessionCache.fetch(player), killer, Constants.ProjectileToItem.parse(projectile.getType()));
                    }
                } else if (killerEntity instanceof Player) {
                // + Player killed Player
                    Player killer = (Player) killerEntity;
                    if(!StatPerms.DeathPVP.has(killer) || !StatPerms.DeathPVP.has(player)) return;
                    killedPlayer(SessionCache.fetch(killer), player, killer.getItemInHand());
                } else if (killerEntity instanceof Creature
                        || killerEntity instanceof Slime
                        || killerEntity instanceof EnderDragon) {
                // + Creature killed Player
                    if(!StatPerms.DeathPVE.has(player)) return;
                    killedByCreature(SessionCache.fetch(player), killerEntity, new ItemStack(Material.AIR));
                } else {
                // + Player died
                    if(!StatPerms.DeathOther.has(player)) return;
                    killedByEnvironment(SessionCache.fetch(player), player.getLocation(), cause);
                }
            } else {
                // Player killed by other means
                if(!StatPerms.DeathOther.has(player)) return;
                killedByEnvironment(SessionCache.fetch(player), player.getLocation(), cause);
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
                    killedCreature(SessionCache.fetch(killer), victim, Constants.ProjectileToItem.parse(projectile.getType()));
                } else if (victim instanceof Slime) {
                    // + Player shot Slime
                    killedCreature(SessionCache.fetch(killer), victim, Constants.ProjectileToItem.parse(projectile.getType()));
                } else if (victim instanceof EnderDragon) {
                    // + Player shot EnderDragon
                    killedCreature(SessionCache.fetch(killer), victim, Constants.ProjectileToItem.parse(projectile.getType()));
                }
            } else if (killerEntity instanceof Player) {
                // Player killed an entity
                Player killer = (Player) killerEntity;
                if(!StatPerms.DeathPVE.has(killer)) return;
                if (victim instanceof Creature) {
                    // + Player killed Creature
                    killedCreature(SessionCache.fetch(killer), victim, killer.getItemInHand());
                } else if (victim instanceof Slime) {
                    // + Player killed Slime
                    killedCreature(SessionCache.fetch(killer), victim, killer.getItemInHand());
                } else if (victim instanceof EnderDragon) {
                    // + Player killed an EnderDragon
                    killedCreature(SessionCache.fetch(killer), victim, killer.getItemInHand());
                }
            }
        }
    }
    
    /**
     * Registers the player death in the data store
     * @param victim Player who was killed 
     * @param weapon Weapon used by killer
     */
    private static void killedPlayer(OnlineSession killer, Player victim, ItemStack weapon) {
        ((PvpDataStore) killer.getDataStore(PrimaryType.PVP)).playerKilledPlayer(victim, weapon);
        ((MiscDataStore) killer.getDataStore(PrimaryType.Misc)).getNormalData().killed(victim);
        killer.getPlayerTotals().pvpKill();
        SessionCache.fetch(victim).getPlayerTotals().death();
    }
    
    /**
     * Registers the creature death in the data store
     * @param victim Creature killed
     * @param weapon Weapon used by killer
     */
    private static void killedCreature(OnlineSession killer, Entity victim, ItemStack weapon) {
        ((PveDataStore) killer.getDataStore(PrimaryType.PVE)).playerKilledCreature(victim, weapon);
        killer.getPlayerTotals().pveKill();
    }
    
    /**
     * Registers the player death in the data store
     * @param killer Creature that killed the player
     * @param weapon Weapon used by killer
     */
    private static void killedByCreature(OnlineSession session, Entity killer, ItemStack weapon) {
        ((PveDataStore) session.getDataStore(PrimaryType.PVE)).creatureKilledPlayer(killer, weapon);
        died(session);
    }
    
    /**
     * Runs when the session owner was killed by the environment
     * @param location Location of the death
     * @param cause Death cause
     */
    private static void killedByEnvironment(OnlineSession session, Location location, DamageCause cause) {
        ((DeathsDataStore) session.getDataStore(PrimaryType.Deaths)).playerDied(location, cause);
        died(session);
    }
    
    /**
     * Runs when the player dies (any cause).<br />
     * This method is for internal use; you do not need to run it from listener
     */
    private static void died(OnlineSession session) {
        ((MiscDataStore) session.getDataStore(PrimaryType.Misc)).getNormalData().died();
        session.getPlayerTotals().death();
    }
    
}
