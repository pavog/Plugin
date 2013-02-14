package com.wolvencraft.yasp.events;

import java.util.List;

import org.bukkit.entity.Player;
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
import com.wolvencraft.yasp.db.data.normal.DataHolder;
import com.wolvencraft.yasp.db.data.normal.DataLabel;
import com.wolvencraft.yasp.db.data.normal.TrackedPlayer;
import com.wolvencraft.yasp.stats.DataCollector;

public class PlayerListener implements Listener {

	public PlayerListener(StatsPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		DataCollector.addDetailedData(new PlayerLog(player));
		
		List<DataHolder> data = DataCollector.getNormalDataByType(DataLabel.Player.toParameterizedString(player.getPlayerListName()));
		DataHolder trackedPlayer;
		if(data.isEmpty()) {
			trackedPlayer = new TrackedPlayer(player);
			DataCollector.addNormalData(trackedPlayer);
		} else trackedPlayer = data.get(0);
		
		// TODO Log player data
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
		DataCollector.addDetailedData(new ItemPickedUp(event.getPlayer(), event.getItem()));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		DataCollector.addDetailedData(new ItemDropped(event.getPlayer(), event.getItemDrop()));
	}
}
