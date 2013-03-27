/*
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

import com.wolvencraft.yasp.AsyncDataCollector;
import com.wolvencraft.yasp.LocalSession;
import com.wolvencraft.yasp.StatsPlugin;
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
	public DeathListener(StatsPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent event) {
		if(StatsPlugin.getPaused()) return;
		Entity victimEntity = event.getEntity();
		EntityDamageEvent lastDamageEvent = victimEntity.getLastDamageCause();
		
		if (lastDamageEvent == null) return;
		DamageCause cause = lastDamageEvent.getCause();

		if (victimEntity instanceof Player) {
			Player victim = (Player) victimEntity;
			if(Util.isExempt(victim)) return;
			if (lastDamageEvent instanceof EntityDamageByEntityEvent) {			// + Player killed by entity
				Entity killerEntity = ((EntityDamageByEntityEvent) lastDamageEvent).getDamager();

				if (killerEntity instanceof Arrow) {							// | + Victim was shot
						Arrow arrow = (Arrow) killerEntity;
						if (arrow.getShooter() instanceof Player) {				// | | + Player shot Player
							Player killer = (Player) arrow.getShooter();
							if(Util.isExempt(victim, "death.pvp") || Util.isExempt(killer, "death.pvp")) return;
							LocalSession session = AsyncDataCollector.get(killer);
							session.PVP().playerKilledPlayer(victim, new ItemStack(Material.ARROW));
							session.player().misc().playerKilled(victim);
						} else if (arrow.getShooter() instanceof Creature) {	// | | + Creature shot Player
							if(Util.isExempt(victim, "death.pve")) return;
							Creature killer = (Creature) arrow.getShooter();
							LocalSession session = AsyncDataCollector.get(victim);
							session.PVE().creatureKilledPlayer(killer, new ItemStack(Material.ARROW));
							session.player().misc().died();
						}
				} else if (killerEntity instanceof Player) {					// | + Player killed Player
					Player killer = (Player) killerEntity;
					if(Util.isExempt(victim, "death.pvp") || Util.isExempt(killer, "death.pvp")) return;
					LocalSession session = AsyncDataCollector.get(killer);
					session.PVP().playerKilledPlayer(victim, killer.getItemInHand());
					session.player().misc().playerKilled(victim);
				} else if (killerEntity instanceof Explosive) {					// | + Player exploded
					if(Util.isExempt(victim, "death.other")) return;
					LocalSession session = AsyncDataCollector.get(victim);
					session.deaths().playerDied(victim.getLocation(), cause);
					session.player().misc().died();
				} else if (killerEntity instanceof Creature) {					// | + Creature killed Player
					if(Util.isExempt(victim, "death.pve")) return;
					Creature killer = (Creature) killerEntity;
					LocalSession session = AsyncDataCollector.get(victim);
					session.PVE().creatureKilledPlayer(killer, new ItemStack(Material.AIR));
					session.player().misc().died();
				} else if (killerEntity instanceof Slime) {						// | + Slime killed player
					if(Util.isExempt(victim, "death.pve")) return;
					Creature killer = (Creature) killerEntity;
					//TODO Check if the Slime kill behavior is the same as the one with Creature
					LocalSession session = AsyncDataCollector.get(victim);
					session.PVE().creatureKilledPlayer(killer, new ItemStack(Material.AIR));
					session.player().misc().died();
				} else {														// | + Player died
					if(Util.isExempt(victim, "death.other")) return;
					LocalSession session = AsyncDataCollector.get(victim);
					session.deaths().playerDied(victim.getLocation(), cause);
					session.player().misc().died();
				}
			} else if (lastDamageEvent instanceof EntityDamageByBlockEvent) {	// + Player killed by blocks
				if(Util.isExempt(victim, "death.other")) return;
				LocalSession session = AsyncDataCollector.get(victim);
				session.deaths().playerDied(victim.getLocation(), cause);
				session.player().misc().died();
			} else {															// + Player died
				if(Util.isExempt(victim, "death.other")) return;
				LocalSession session = AsyncDataCollector.get(victim);
				session.deaths().playerDied(victim.getLocation(), cause);
				session.player().misc().died();
			}
		} else {
			if (!(lastDamageEvent instanceof EntityDamageByEntityEvent)) return;
			
			Entity killerEntity = ((EntityDamageByEntityEvent) lastDamageEvent).getDamager();
			
			if (killerEntity instanceof Arrow) {								// + Player shot an entity
				Arrow arrow = (Arrow) killerEntity;
				if (!(arrow.getShooter() instanceof Player)) return;
				Player killer = (Player) arrow.getShooter();
				if(Util.isExempt(killer, "death.pve")) return;
				if (victimEntity instanceof Creature) {							// | + Player shot Creature
					Creature victim = (Creature) victimEntity;
					AsyncDataCollector.get(killer).PVE().playerKilledCreature(victim, new ItemStack(Material.ARROW));
				} else if (victimEntity instanceof Slime) {						// | + Player shot Slime
					Creature victim = (Creature) victimEntity;
					AsyncDataCollector.get(killer).PVE().playerKilledCreature(victim, new ItemStack(Material.ARROW));
				}
			} else if (killerEntity instanceof Player) {						// + Player killed an entity
				Player killer = (Player) killerEntity;
				if(Util.isExempt(killer, "death.pve")) return;
				if (victimEntity instanceof Creature) {							// | + Player killed Creature
					Creature victim = (Creature) victimEntity;
					AsyncDataCollector.get(killer).PVE().playerKilledCreature(victim, killer.getItemInHand());
				} else if (victimEntity instanceof Slime) {						// | + Player killed Slime
					Creature victim = (Creature) victimEntity;
					AsyncDataCollector.get(killer).PVE().playerKilledCreature(victim, killer.getItemInHand());
				}
			}
		}
	}
}
