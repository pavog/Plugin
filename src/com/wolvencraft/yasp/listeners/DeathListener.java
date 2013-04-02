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
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.Player;
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

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.util.Util;

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
            if(!Util.isTracked(victim)) return;
            if (lastDamageEvent instanceof EntityDamageByEntityEvent) {            // + Player killed by entity
                Entity killerEntity = ((EntityDamageByEntityEvent) lastDamageEvent).getDamager();

                if (killerEntity instanceof Arrow) {                            // | + Victim was shot
                        Arrow arrow = (Arrow) killerEntity;
                        if (arrow.getShooter() instanceof Player) {                // | | + Player shot Player
                            Player killer = (Player) arrow.getShooter();
                            if(!Util.isTracked(victim, "death.pvp") || !Util.isTracked(killer, "death.pvp")) return;
                            OnlineSession session = DataCollector.get(killer);
                            session.killedPlayer(victim, new ItemStack(Material.ARROW));
                        } else if (arrow.getShooter() instanceof Creature) {    // | | + Creature shot Player
                            if(!Util.isTracked(victim, "death.pve")) return;
                            Creature killer = (Creature) arrow.getShooter();
                            OnlineSession session = DataCollector.get(victim);
                            session.killedByCreature(killer, new ItemStack(Material.ARROW));
                        }
                } else if (killerEntity instanceof Player) {                    // | + Player killed Player
                    Player killer = (Player) killerEntity;
                    if(!Util.isTracked(victim, "death.pvp") || !Util.isTracked(killer, "death.pvp")) return;
                    OnlineSession session = DataCollector.get(killer);
                    session.killedPlayer(victim, killer.getItemInHand());
                } else if (killerEntity instanceof Explosive) {                    // | + Player exploded
                    if(!Util.isTracked(victim, "death.other")) return;
                    OnlineSession session = DataCollector.get(victim);
                    session.killedByEnvironment(victim.getLocation(), cause);
                } else if (killerEntity instanceof Creature) {                    // | + Creature killed Player
                    if(!Util.isTracked(victim, "death.pve")) return;
                    Creature killer = (Creature) killerEntity;
                    OnlineSession session = DataCollector.get(victim);
                    session.killedByCreature(killer, new ItemStack(Material.AIR));
                } else if (killerEntity instanceof Slime) {                        // | + Slime killed player
                    if(!Util.isTracked(victim, "death.pve")) return;
                    Creature killer = (Creature) killerEntity;
                    //TODO Check if the Slime kill behavior is the same as the one with Creature
                    OnlineSession session = DataCollector.get(victim);
                    session.killedByCreature(killer, new ItemStack(Material.AIR));
                } else {                                                        // | + Player died
                    if(!Util.isTracked(victim, "death.other")) return;
                    OnlineSession session = DataCollector.get(victim);
                    session.killedByEnvironment(victim.getLocation(), cause);
                }
            } else if (lastDamageEvent instanceof EntityDamageByBlockEvent) {    // + Player killed by blocks
                if(!Util.isTracked(victim, "death.other")) return;
                OnlineSession session = DataCollector.get(victim);
                session.killedByEnvironment(victim.getLocation(), cause);
            } else {                                                            // + Player died
                if(!Util.isTracked(victim, "death.other")) return;
                OnlineSession session = DataCollector.get(victim);
                session.killedByEnvironment(victim.getLocation(), cause);
            }
        } else {
            if (!(lastDamageEvent instanceof EntityDamageByEntityEvent)) return;
            
            Entity killerEntity = ((EntityDamageByEntityEvent) lastDamageEvent).getDamager();
            
            if (killerEntity instanceof Arrow) {                                // + Player shot an entity
                Arrow arrow = (Arrow) killerEntity;
                if (!(arrow.getShooter() instanceof Player)) return;
                Player killer = (Player) arrow.getShooter();
                if(!Util.isTracked(killer, "death.pve")) return;
                if (victimEntity instanceof Creature) {                            // | + Player shot Creature
                    Creature victim = (Creature) victimEntity;
                    DataCollector.get(killer).killedCreature(victim, new ItemStack(Material.ARROW));
                } else if (victimEntity instanceof Slime) {                        // | + Player shot Slime
                    Creature victim = (Creature) victimEntity;
                    DataCollector.get(killer).killedCreature(victim, new ItemStack(Material.ARROW));
                }
            } else if (killerEntity instanceof Player) {                        // + Player killed an entity
                Player killer = (Player) killerEntity;
                if(!Util.isTracked(killer, "death.pve")) return;
                if (victimEntity instanceof Creature) {                            // | + Player killed Creature
                    Creature victim = (Creature) victimEntity;
                    DataCollector.get(killer).killedCreature(victim, killer.getItemInHand());
                } else if (victimEntity instanceof Slime) {                        // | + Player killed Slime
                    Creature victim = (Creature) victimEntity;
                    DataCollector.get(killer).killedCreature(victim, killer.getItemInHand());
                }
            }
        }
    }
}
