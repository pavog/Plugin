package com.wolvencraft.yasp.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.data.detailed.ItemDropped;
import com.wolvencraft.yasp.db.data.detailed.ItemPickedUp;
import com.wolvencraft.yasp.db.data.detailed.PlayerLog;
import com.wolvencraft.yasp.stats.CollectedData;

public class PlayerListener implements Listener {

	public PlayerListener(StatsPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		CollectedData.addDetailedData(new PlayerLog(event.getPlayer()));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event) {
		// Player logged out
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
		// Player moved
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		CollectedData.addDetailedData(new ItemPickedUp(event.getPlayer(), event.getItem()));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		CollectedData.addDetailedData(new ItemDropped(event.getPlayer(), event.getItemDrop()));
	}
}
