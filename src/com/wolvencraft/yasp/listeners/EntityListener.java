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
import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.util.Util;

public class EntityListener implements Listener {

	public EntityListener(StatsPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent event) {
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
							if(Util.isExempt(killer)) return;
							DataCollector.get(killer).playerKilledPlayer(victim, new ItemStack(Material.ARROW));
						} else if (arrow.getShooter() instanceof Creature) {	// | | + Creature shot Player
							Creature killer = (Creature) arrow.getShooter();
							DataCollector.get(victim).creatureKilledPlayer(killer, new ItemStack(Material.ARROW));
						}
				} else if (killerEntity instanceof Player) {					// | + Player killed Player
					Player killer = (Player) killerEntity;
					if(Util.isExempt(killer)) return;
					DataCollector.get(killer).playerKilledPlayer(victim, killer.getItemInHand());
				} else if (killerEntity instanceof Explosive) {					// | + Player exploded
					DataCollector.get(victim).playerDied(cause);
				} else if (killerEntity instanceof Creature) {					// | + Creature killed Player
					Creature killer = (Creature) killerEntity;
					DataCollector.get(victim).creatureKilledPlayer(killer, new ItemStack(Material.AIR));
				} else if (killerEntity instanceof Slime) {						// | + Slime killed player
					Creature killer = (Creature) killerEntity; //TODO Check if the Slime kill behavior is the same as the one with Creature
					DataCollector.get(victim).creatureKilledPlayer(killer, new ItemStack(Material.AIR));
				} else {														// | + Player died
					DataCollector.get(victim).playerDied(cause);
				}
			} else if (lastDamageEvent instanceof EntityDamageByBlockEvent) {	// + Player killed by blocks
				DataCollector.get(victim).playerDied(cause);
			} else {															// + Player died
				DataCollector.get(victim).playerDied(cause);
			}
		} else {
			if (!(lastDamageEvent instanceof EntityDamageByEntityEvent)) return;
			
			Entity killerEntity = ((EntityDamageByEntityEvent) lastDamageEvent).getDamager();
			
			if (killerEntity instanceof Arrow) {								// + Player shot an entity
				Arrow arrow = (Arrow) killerEntity;
				if (!(arrow.getShooter() instanceof Player)) return;
				Player killer = (Player) arrow.getShooter();
				if(Util.isExempt(killer)) return;
				if (victimEntity instanceof Creature) {							// | + Player shot Creature
					Creature victim = (Creature) victimEntity;
					DataCollector.get(killer).playerKilledCreature(victim, new ItemStack(Material.ARROW));
				} else if (victimEntity instanceof Slime) {						// | + Player shot Slime
					Creature victim = (Creature) victimEntity;
					DataCollector.get(killer).playerKilledCreature(victim, new ItemStack(Material.ARROW));
				}
			} else if (killerEntity instanceof Player) {						// + Player killed an entity
				Player killer = (Player) killerEntity;
				if(Util.isExempt(killer)) return;
				if (victimEntity instanceof Creature) {							// | + Player killed Creature
					Creature victim = (Creature) victimEntity;
					DataCollector.get(killer).playerKilledCreature(victim, killer.getItemInHand());
				} else if (victimEntity instanceof Slime) {						// | + Player killed Slime
					Creature victim = (Creature) victimEntity;
					DataCollector.get(killer).playerKilledCreature(victim, killer.getItemInHand());
				}
			}
		}
	}
}
