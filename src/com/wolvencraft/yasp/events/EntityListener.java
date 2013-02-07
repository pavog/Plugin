package com.wolvencraft.yasp.events;

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

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.data.detailed.DeathOther;
import com.wolvencraft.yasp.db.data.detailed.DeathPVE;
import com.wolvencraft.yasp.db.data.detailed.DeathPVP;
import com.wolvencraft.yasp.stats.CollectedData;

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
							CollectedData.addDetailedData(new DeathPVP((Player) arrow.getShooter(),
									playerVictim,
									new ItemStack(Material.ARROW)));
						} else if (arrow.getShooter() instanceof Creature) {
							// Creature killed Player
							CollectedData.addDetailedData(new DeathPVE(playerVictim,
									arrow.getShooter().getType(),
									new ItemStack(Material.ARROW),
									true));
						}
				} else if (damager instanceof Player) {
					// Player killed Player
					CollectedData.addDetailedData(new DeathPVP((Player) damager,
							playerVictim,
							((Player) damager).getItemInHand()));
				} else if (damager instanceof Explosive) {
					// Player exploded
					CollectedData.addDetailedData(new DeathOther(playerVictim,
							deathCause));
				} else if (damager instanceof Creature) {
					// Creature killed Player
					CollectedData.addDetailedData(new DeathPVE(playerVictim,
							damager.getType(),
							new ItemStack(Material.AIR),
							true));
				} else if (damager instanceof Slime) {
					// Slime killed Player
					CollectedData.addDetailedData(new DeathPVE(playerVictim,
							damager.getType(),
							new ItemStack(Material.AIR),
							true));
				}
			} else if (victimLastDamageEvent instanceof EntityDamageByBlockEvent) {
				// Block killed Player
				CollectedData.addDetailedData(new DeathOther(playerVictim,
						deathCause));
			} else {
				// Player died (unknown cause)
				CollectedData.addDetailedData(new DeathOther(playerVictim,
						deathCause));
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
						CollectedData.addDetailedData(new DeathPVE(playerKiller,
								victim.getType(),
								new ItemStack(Material.ARROW),
								false));
					} else if (victim instanceof Slime) {
						// Player shot Slime
						CollectedData.addDetailedData(new DeathPVE(playerKiller,
								victim.getType(),
								new ItemStack(Material.ARROW),
								false));
					}
				} else if (damager instanceof Player) {
					Player playerKiller = (Player)damager;
					if (victim instanceof Creature) {
						// Player killed Creature
						CollectedData.addDetailedData(new DeathPVE(playerKiller,
								victim.getType(),
								playerKiller.getItemInHand(),
								false));
					} else if (victim instanceof Slime) {
						// Player killed Slime
						CollectedData.addDetailedData(new DeathPVE(playerKiller,
								victim.getType(),
								playerKiller.getItemInHand(),
								false));
					}
				}
			}
		}
	}
}
