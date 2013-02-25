package com.wolvencraft.yasp.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.util.Util;

public class PlayerListener implements Listener {

	public PlayerListener(StatsPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player)) return;
		DataCollector.get(player).login();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player)) return;
		DataCollector.get(player).logout();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player)) return;
		double distance = player.getLocation().distance(event.getTo());
		if(player.isInsideVehicle()) {
			Vehicle vehicle = (Vehicle) player.getVehicle();
			if(vehicle.getType().equals(EntityType.MINECART)) {
				DataCollector.get(player).addDistanceMinecart(distance);
			} else if(vehicle.getType().equals(EntityType.BOAT)) {
				DataCollector.get(player).addDistanceBoat(distance);
			} else if(vehicle.getType().equals(EntityType.PIG)) {
				DataCollector.get(player).addDistancePig(distance);
			}
		} else {
			DataCollector.get(player).addDistanceFoot(distance);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player)) return;
		DataCollector.get(player).itemPickUp(event.getItem().getItemStack());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player)) return;
		DataCollector.get(player).itemDrop(event.getItemDrop().getItemStack());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerItemUse(FoodLevelChangeEvent event) {
		if(!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		if(Util.isExempt(player)) return;
		DataCollector.get(player).itemUse(player.getItemInHand());
	}
}
