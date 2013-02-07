package com.wolvencraft.yasp.events;

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

import com.wolvencraft.yasp.StatsPlugin;

public class EntityListener implements Listener {

	public EntityListener(StatsPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent event) {
		Entity victim = event.getEntity();
		EntityDamageEvent victimLastDamageEvent = victim.getLastDamageCause();
		
		if (victimLastDamageEvent == null) return;
		DamageCause deathCause = victimLastDamageEvent.getCause();

		if (victim instanceof Player) {
			Player playerVictim = (Player) victim;
			if (victimLastDamageEvent instanceof EntityDamageByEntityEvent) {
				Entity damager = ((EntityDamageByEntityEvent) victimLastDamageEvent).getDamager();

				if (damager instanceof Arrow) {
						Arrow arrow = (Arrow) damager;
						if (arrow.getShooter() instanceof Player) {
							// Player killed Player
						} else if (arrow.getShooter() instanceof Creature) {
							// Creature killed Player
						}
				} else if (damager instanceof Player) {
					// Player killed Player
				} else if (damager instanceof Explosive) {
					// Player exploded
				} else if (damager instanceof Creature) {
					// Creature killed Player
				} else if (damager instanceof Slime) {
					// Slime killed Player
				}
			} else if (victimLastDamageEvent instanceof EntityDamageByBlockEvent) {
				// Block killed Player
			} else {
				// Player died (unknown cause)
			}
		} else {
			if (victimLastDamageEvent instanceof EntityDamageByEntityEvent) {
				Entity damager = ((EntityDamageByEntityEvent) victimLastDamageEvent).getDamager();
				
				if (damager instanceof Arrow) {
					Arrow arrow = (Arrow)damager;
					if (!(arrow.getShooter() instanceof Player)) return; 
					Player playerKiller = (Player)arrow.getShooter();
					if (victim instanceof Creature) {
						// Player shot Creature
					} else if (victim instanceof Slime) {
						// Player shot Slime
					}
				} else if (damager instanceof Player) {
					Player playerKiller = (Player)damager;
					if (victim instanceof Creature) {
						// Player killed Creature
					} else if (victim instanceof Slime) {
						// Player killed Slime
					}
				}
			}
		}
	}
}
