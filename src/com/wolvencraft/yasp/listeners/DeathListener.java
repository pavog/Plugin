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

import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.Settings;
import com.wolvencraft.yasp.Settings.StatPerms;
import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.util.tasks.DatabaseTask;

/**
 * Listens to any entity deaths on the server and reports them to the plugin
 * @author bitWolfy
 *
 */
public class DeathListener implements Listener {
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new instance of the Listener and registers it with the PluginManager
     * @param plugin StatsPlugin instance
     */
    public DeathListener(Statistics plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        if(Statistics.getPaused()) return;
        Entity victimEntity = event.getEntity();
        EntityDamageEvent lastDamageEvent = victimEntity.getLastDamageCause();
        
        if (lastDamageEvent == null) return;
        DamageCause cause = lastDamageEvent.getCause();

        if (victimEntity instanceof Player) {
            Player victim = (Player) victimEntity;
            if(!StatPerms.Death.has(victim)) return;
            if (lastDamageEvent instanceof EntityDamageByEntityEvent) {            // + Player killed by entity
                Entity killerEntity = ((EntityDamageByEntityEvent) lastDamageEvent).getDamager();

                if (killerEntity instanceof Projectile) {                            // | + Victim was shot
                    Projectile projectile = (Projectile) killerEntity;
                    if (projectile.getShooter() instanceof Player) {                // | | + Player shot Player
                        Player killer = (Player) projectile.getShooter();
                        if(!StatPerms.DeathPVP.has(killer) || !StatPerms.DeathPVP.has(victim)) return;
                        OnlineSession session = DatabaseTask.getSession(killer);
                        session.killedPlayer(victim, Settings.ProjectileToItem.parse(projectile.getType()));
                    } else if (projectile.getShooter() instanceof Creature) {    // | | + Creature shot Player
                        if(!StatPerms.DeathPVE.has(victim)) return;
                        Entity killer = (Entity) projectile.getShooter();
                        OnlineSession session = DatabaseTask.getSession(victim);
                        session.killedByCreature(killer, Settings.ProjectileToItem.parse(projectile.getType()));
                    }
                } else if (killerEntity instanceof Player) {                    // | + Player killed Player
                    Player killer = (Player) killerEntity;
                    if(!StatPerms.DeathPVP.has(killer) || !StatPerms.DeathPVP.has(victim)) return;
                    OnlineSession session = DatabaseTask.getSession(killer);
                    session.killedPlayer(victim, killer.getItemInHand());
                } else if (killerEntity instanceof Explosive) {                    // | + Player exploded
                    if(!StatPerms.DeathOther.has(victim)) return;
                    OnlineSession session = DatabaseTask.getSession(victim);
                    session.killedByEnvironment(victim.getLocation(), cause);
                } else if (killerEntity instanceof Creature) {                    // | + Creature killed Player
                    if(!StatPerms.DeathPVE.has(victim)) return;
                    OnlineSession session = DatabaseTask.getSession(victim);
                    session.killedByCreature(killerEntity, new ItemStack(Material.AIR));
                } else if (killerEntity instanceof Slime) {                        // | + Slime killed player
                    if(!StatPerms.DeathPVE.has(victim)) return;
                    OnlineSession session = DatabaseTask.getSession(victim);
                    session.killedByCreature(killerEntity, new ItemStack(Material.AIR));
                } else if (killerEntity instanceof EnderDragon) {                        // | + Ender Dragon killed player
                    if(!StatPerms.DeathPVE.has(victim)) return;
                    OnlineSession session = DatabaseTask.getSession(victim);
                    session.killedByCreature(killerEntity, new ItemStack(Material.AIR));
                } else {                                                        // | + Player died
                    if(!StatPerms.DeathOther.has(victim)) return;
                    OnlineSession session = DatabaseTask.getSession(victim);
                    session.killedByEnvironment(victim.getLocation(), cause);
                }
            } else if (lastDamageEvent instanceof EntityDamageByBlockEvent) {    // + Player killed by blocks
                if(!StatPerms.DeathOther.has(victim)) return;
                OnlineSession session = DatabaseTask.getSession(victim);
                session.killedByEnvironment(victim.getLocation(), cause);
            } else {                                                            // + Player died
                if(!StatPerms.DeathOther.has(victim)) return;
                OnlineSession session = DatabaseTask.getSession(victim);
                session.killedByEnvironment(victim.getLocation(), cause);
            }
        } else {
            if (!(lastDamageEvent instanceof EntityDamageByEntityEvent)) return;
            
            Entity killerEntity = ((EntityDamageByEntityEvent) lastDamageEvent).getDamager();
            
            if (killerEntity instanceof Projectile) {                                // + Player shot an entity
                Projectile projectile = (Projectile) killerEntity;
                if (!(projectile.getShooter() instanceof Player)) return;
                Player killer = (Player) projectile.getShooter();
                if(!StatPerms.DeathPVE.has(killer)) return;
                if (victimEntity instanceof Creature) {                            // | + Player shot Creature
                    DatabaseTask.getSession(killer).killedCreature(victimEntity, Settings.ProjectileToItem.parse(projectile.getType()));
                } else if (victimEntity instanceof Slime) {                        // | + Player shot Slime
                    DatabaseTask.getSession(killer).killedCreature(victimEntity, Settings.ProjectileToItem.parse(projectile.getType()));
                }
            } else if (killerEntity instanceof Player) {                        // + Player killed an entity
                Player killer = (Player) killerEntity;
                if(!StatPerms.DeathPVE.has(killer)) return;
                if (victimEntity instanceof Creature) {                            // | + Player killed Creature
                    DatabaseTask.getSession(killer).killedCreature(victimEntity, killer.getItemInHand());
                } else if (victimEntity instanceof Slime) {                        // | + Player killed Slime
                    DatabaseTask.getSession(killer).killedCreature(victimEntity, killer.getItemInHand());
                } else if (victimEntity instanceof EnderDragon) {                  // | + Player killed an EnderDragon
                    DatabaseTask.getSession(killer).killedCreature(victimEntity, killer.getItemInHand());
                }
            }
        }
    }
}
