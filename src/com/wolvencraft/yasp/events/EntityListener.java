package com.wolvencraft.yasp.events;

import java.util.List;

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
import com.wolvencraft.yasp.db.data.normal.DataHolder;
import com.wolvencraft.yasp.db.data.normal.DataLabel;
import com.wolvencraft.yasp.db.data.normal.TotalDeaths;
import com.wolvencraft.yasp.db.data.normal.TotalPVE;
import com.wolvencraft.yasp.db.data.normal.TotalPVP;
import com.wolvencraft.yasp.stats.DataCollector;

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
							Player killer = (Player) arrow.getShooter();
							DataCollector.addDetailedData(
								new DeathPVP(
									killer,
									playerVictim,
									new ItemStack(Material.ARROW)
								)
							);
							List<DataHolder> data = DataCollector.getNormalDataByType(DataLabel.TotalPVP.getAliasParameterized(killer.getPlayerListName()));
							DataHolder totalPVP;
							if(data.isEmpty()) {
								totalPVP = new TotalPVP(killer, playerVictim);
								DataCollector.addNormalData(totalPVP);
							} else totalPVP = data.get(0);
							TotalPVP.class.cast(totalPVP).addTimes();
						} else if (arrow.getShooter() instanceof Creature) {
							// Creature killed Player
							DataCollector.addDetailedData(
								new DeathPVE(
									playerVictim,
									arrow.getShooter().getType(),
									new ItemStack(Material.ARROW),
									true
								)
							);
							List<DataHolder> data = DataCollector.getNormalDataByType(DataLabel.TotalPVE.getAliasParameterized(playerVictim.getPlayerListName()));
							DataHolder totalPVE;
							if(data.isEmpty()) {
								totalPVE = new TotalPVE(playerVictim, (Creature)(arrow.getShooter()));
								DataCollector.addNormalData(totalPVE);
							} else totalPVE = data.get(0);
							TotalPVE.class.cast(totalPVE).addPlayerDeaths();
						}
				} else if (damager instanceof Player) {
					// Player killed Player
					Player killer = (Player) damager;
					DataCollector.addDetailedData(
						new DeathPVP(
							killer,
							playerVictim,
							((Player) damager).getItemInHand()
						)
					);
					List<DataHolder> data = DataCollector.getNormalDataByType(DataLabel.TotalPVP.getAliasParameterized(killer.getPlayerListName()));
					DataHolder totalPVP;
					if(data.isEmpty()) {
						totalPVP = new TotalPVP(killer, playerVictim);
						DataCollector.addNormalData(totalPVP);
					} else totalPVP = data.get(0);
					TotalPVP.class.cast(totalPVP).addTimes();
				} else if (damager instanceof Explosive) {
					//Player exploded
					DataCollector.addDetailedData(
						new DeathOther(
							playerVictim,
							deathCause
						)
					);
					List<DataHolder> data = DataCollector.getNormalDataByType(DataLabel.TotalDeaths.getAliasParameterized(playerVictim.getPlayerListName()));
					DataHolder totalDeaths;
					if(data.isEmpty()) {
						totalDeaths = new TotalDeaths(playerVictim, deathCause);
						DataCollector.addNormalData(totalDeaths);
					} else totalDeaths = data.get(0);
					TotalDeaths.class.cast(totalDeaths).addTimes();
				} else if (damager instanceof Creature) {
					// Creature killed Player
					DataCollector.addDetailedData(
						new DeathPVE(
							playerVictim,
							damager.getType(),
							new ItemStack(Material.AIR),
							true
						)
					);
					List<DataHolder> data = DataCollector.getNormalDataByType(DataLabel.TotalPVE.getAliasParameterized(playerVictim.getPlayerListName()));
					DataHolder totalPVE;
					if(data.isEmpty()) {
						totalPVE = new TotalPVE(playerVictim, (Creature)(damager));
						DataCollector.addNormalData(totalPVE);
					} else totalPVE = data.get(0);
					TotalPVE.class.cast(totalPVE).addPlayerDeaths();
				} else if (damager instanceof Slime) {
					// Slime killed Player
					DataCollector.addDetailedData(
						new DeathPVE(
							playerVictim,
							damager.getType(),
							new ItemStack(Material.AIR),
							true
						)
					);
					List<DataHolder> data = DataCollector.getNormalDataByType(DataLabel.TotalPVE.getAliasParameterized(playerVictim.getPlayerListName()));
					DataHolder totalPVE;
					if(data.isEmpty()) {
						totalPVE = new TotalPVE(playerVictim, (Creature)(damager));
						DataCollector.addNormalData(totalPVE);
					} else totalPVE = data.get(0);
					TotalPVE.class.cast(totalPVE).addPlayerDeaths();
				}
			} else if (victimLastDamageEvent instanceof EntityDamageByBlockEvent) {
				//Block killed Player
				DataCollector.addDetailedData(
					new DeathOther(
						playerVictim,
						deathCause
					)
				);
				List<DataHolder> data = DataCollector.getNormalDataByType(DataLabel.TotalDeaths.getAliasParameterized(playerVictim.getPlayerListName()));
				DataHolder totalDeaths;
				if(data.isEmpty()) {
					totalDeaths = new TotalDeaths(playerVictim, deathCause);
					DataCollector.addNormalData(totalDeaths);
				} else totalDeaths = data.get(0);
				TotalDeaths.class.cast(totalDeaths).addTimes();
			} else {
				//Player died (unknown cause)
				DataCollector.addDetailedData(
					new DeathOther(
						playerVictim,
						deathCause
					)
				);
				List<DataHolder> data = DataCollector.getNormalDataByType(DataLabel.TotalDeaths.getAliasParameterized(playerVictim.getPlayerListName()));
				DataHolder totalDeaths;
				if(data.isEmpty()) {
					totalDeaths = new TotalDeaths(playerVictim, deathCause);
					DataCollector.addNormalData(totalDeaths);
				} else totalDeaths = data.get(0);
				TotalDeaths.class.cast(totalDeaths).addTimes();
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
						DataCollector.addDetailedData(
							new DeathPVE(
								playerKiller,
								victim.getType(),
								new ItemStack(Material.ARROW),
								false
							)
						);
						List<DataHolder> data = DataCollector.getNormalDataByType(DataLabel.TotalPVE.getAliasParameterized(playerKiller.getPlayerListName()));
						DataHolder totalPVE;
						if(data.isEmpty()) {
							totalPVE = new TotalPVE(playerKiller, (Creature)(victim));
							DataCollector.addNormalData(totalPVE);
						} else totalPVE = data.get(0);
						TotalPVE.class.cast(totalPVE).addCreatureDeaths();
					} else if (victim instanceof Slime) {
						// Player shot Slime
						DataCollector.addDetailedData(
							new DeathPVE(
								playerKiller,
								victim.getType(),
								new ItemStack(Material.ARROW),
								false
							)
						);
						List<DataHolder> data = DataCollector.getNormalDataByType(DataLabel.TotalPVE.getAliasParameterized(playerKiller.getPlayerListName()));
						DataHolder totalPVE;
						if(data.isEmpty()) {
							totalPVE = new TotalPVE(playerKiller, (Creature)(victim));
							DataCollector.addNormalData(totalPVE);
						} else totalPVE = data.get(0);
						TotalPVE.class.cast(totalPVE).addCreatureDeaths();
					}
				} else if (damager instanceof Player) {
					Player playerKiller = (Player)damager;
					if (victim instanceof Creature) {
						// Player killed Creature
						DataCollector.addDetailedData(
							new DeathPVE(
								playerKiller,
								victim.getType(),
								playerKiller.getItemInHand(),
								false
							)
						);
						List<DataHolder> data = DataCollector.getNormalDataByType(DataLabel.TotalPVE.getAliasParameterized(playerKiller.getPlayerListName()));
						DataHolder totalPVE;
						if(data.isEmpty()) {
							totalPVE = new TotalPVE(playerKiller, (Creature)(victim));
							DataCollector.addNormalData(totalPVE);
						} else totalPVE = data.get(0);
						TotalPVE.class.cast(totalPVE).addCreatureDeaths();
					} else if (victim instanceof Slime) {
						// Player killed Slime
						DataCollector.addDetailedData(
							new DeathPVE(
								playerKiller,
								victim.getType(),
								playerKiller.getItemInHand(),
								false
							)
						);
						List<DataHolder> data = DataCollector.getNormalDataByType(DataLabel.TotalPVE.getAliasParameterized(playerKiller.getPlayerListName()));
						DataHolder totalPVE;
						if(data.isEmpty()) {
							totalPVE = new TotalPVE(playerKiller, (Creature)(victim));
							DataCollector.addNormalData(totalPVE);
						} else totalPVE = data.get(0);
						TotalPVE.class.cast(totalPVE).addCreatureDeaths();
					}
				}
			}
		}
	}
}
