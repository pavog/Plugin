package com.wolvencraft.yasp.Listeners;

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
import com.wolvencraft.yasp.EventDataHandlers.EDHPlayer;

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
							// Register PVP
						} else if (arrow.getShooter() instanceof Creature) {
							// Register PVE
						}
				} else if (damager instanceof Player) {
					// Register PVP
				} else if (damager instanceof Explosive) {
					// Register Other
				} else if (damager instanceof Creature) {
					// Register PVE
				} else if (damager instanceof Slime) {
					// Register PVE
				}
			} else if (victimLastDamageEvent instanceof EntityDamageByBlockEvent) {
				// Register Other
			} else {
				// Register Other
			}
		} else {
			if (lastDamageEvent instanceof EntityDamageByEntityEvent) {
				Entity damager = ((EntityDamageByEntityEvent)lastDamageEvent).getDamager();
				
				if (damager instanceof Arrow) {
					Arrow arrow = (Arrow)damager;
					if (arrow.getShooter() instanceof Player) {
					Player playerKiller = (Player)arrow.getShooter();
						if (entity instanceof Creature) {
							this.edhPlayer.PlayerKilledCreatureProjectile(playerKiller, (Creature)entity, arrow, cause);
						} else if (entity instanceof Slime) {
							this.edhPlayer.PlayerKilledSlimeProjectile(playerKiller, (Slime)entity, arrow, cause);
						}
					}
				} else if (damager instanceof Player) {
					Player playerKiller = (Player)damager;
					if (entity instanceof Creature) {
						this.edhPlayer.PlayerKilledCreature(playerKiller, (Creature)entity, cause);
					} else if (entity instanceof Slime) {
						this.edhPlayer.PlayerKilledSlime(playerKiller, (Slime)entity, cause);
					}
				}
			}
		}
	}
}
