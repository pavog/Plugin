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

package com.wolvencraft.yasp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.data.hooks.VaultHook;
import com.wolvencraft.yasp.db.data.hooks.WorldGuardHook;
import com.wolvencraft.yasp.db.data.hooks.VaultHook.VaultHookEntry;
import com.wolvencraft.yasp.db.data.hooks.WorldGuardHook.WorldGuardHookEntry;
import com.wolvencraft.yasp.db.data.receive.PlayerTotals;
import com.wolvencraft.yasp.db.data.sync.*;

/**
 * Represents a user session for an online tracked player
 * @author bitWolfy
 *
 */
public class LocalSession {
	
	/**
	 * <b>Default constructor</b><br />
	 * Creates a new user session based on the Player specified
	 * @param player Player to track
	 */
	public LocalSession(Player player) {
		confirmed = true;
		
		int playerId = DataCollector.getPlayerId(player);
		
		this.playersData = new PlayersData(player, playerId);
		this.blocksData = new BlocksData(playerId);
		this.itemsData = new ItemsData(playerId);
		this.deathsData = new DeathsData(playerId);
		this.PVEData = new PVEData(playerId);
		this.PVPData = new PVPData(playerId);
		
		this.playerTotals = new PlayerTotals(playerId);
		
		if(Settings.RemoteConfiguration.HookVault.asBoolean() && Settings.Modules.HookVault.getActive())
			vaultHookEntry = VaultHook.getInstance().new VaultHookEntry(player, playerId);
		if(Settings.RemoteConfiguration.HookWorldGuard.asBoolean() && Settings.Modules.HookWorldGuard.getActive())
			worldGuardHookEntry = WorldGuardHook.getInstance().new WorldGuardHookEntry(player, playerId);
	}
	
	private boolean confirmed;
	
	private PlayersData playersData;
	private BlocksData blocksData;
	private ItemsData itemsData;
	private DeathsData deathsData;
	private PVEData PVEData;
	private PVPData PVPData;
	
	private PlayerTotals playerTotals;
	
	private VaultHookEntry vaultHookEntry;
	private WorldGuardHookEntry worldGuardHookEntry;
	
	/**
	 * Performs an operation to push the locally stored data to the database
	 */
	public void pushData() {
		if(!confirmed) return;
		playersData.sync();
		blocksData.sync();
		itemsData.sync();
		deathsData.sync();
		PVEData.sync();
		PVPData.sync();
		
		playerTotals.fetchData();
		
		if(Settings.RemoteConfiguration.HookVault.asBoolean() && Settings.Modules.HookVault.getActive())
			vaultHookEntry.pushData();
		if(Settings.RemoteConfiguration.HookWorldGuard.asBoolean() && Settings.Modules.HookWorldGuard.getActive())
			worldGuardHookEntry.pushData();
	}
	
	/**
	 * Returns the confirmation status
	 * @return <b>true</b> if the player is confirmed, <b>false</b> if he is on hold
	 */
	public boolean getConfirmed() {
		return confirmed;
	}
	
	/**
	 * Sets the confirmation status for the player
	 * @param confirmed <b>true</b> if the player is confirmed, <b>false</b> if he is on hold
	 */
	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}
	
	/**
	 * <b>PlayersData</b> wrapper.<br />
	 * Returns the unique player name.
	 * @return <b>String</b> Player name
	 */
	public String getPlayerName() { return playersData.general().getName(); }
	
	/**
	 * <b>PlayersData</b> wrapper<br />
	 * Returns the Player object associated with the session, if it exists.
	 * @return <b>Player</b> object if it exists, <b>null</b> otherwise
	 */
	public Player getPlayer() { return Bukkit.getServer().getPlayer(playersData.general().getName()); }
	
	/**
	 * <b>PlayersData</b> wrapper<br />
	 * Returns the location of the Player object associated with the session, if it exists.
	 * @return <b>Location</b> if the player is online, <b>null</b> otherwise
	 */
	public Location getLocation() {
		Player player = Bukkit.getServer().getPlayer(playersData.general().getName());
		if(player == null) return null;
		return player.getLocation();
	}
	
	/**
	 * <b>PlayersData</b> wrapper<br />
	 * Returns the player's current online status.
	 * @return <b>true</b> if online, <b>false</b> otherwise
	 */
	public boolean isOnline() {
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(player.getPlayerListName().equals(playersData.general().getName())) return true;
		}
		return false;
	}
	
	/**
	 * Clears the data stores of all locally stored data.
	 */
	public void dump() {
		playersData.dump();
		blocksData.dump();
		itemsData.dump();
		deathsData.dump();
		PVEData.dump();
		PVPData.dump();
	}
	
	/**
	 * Returns the generic player data store.<br />
	 * Contains such information as player's online status, experience, health and food levels, distances traveled, etc.
	 * @return <b>PlayersData</b> data store
	 */
	public PlayersData player() { return playersData; }
	
	/**
	 * Returns the data store that contains information about broken and placed blocks
	 * @return <b>BlocksData</b> data store
	 */
	public BlocksData blocks() { return blocksData; }
	
	/**
	 * Returns the data store that contains information about item operations
	 * @return <b>ItemsData</b> data store
	 */
	public ItemsData items() { return itemsData; }
	
	/**
	 * Returns the data store that contains information about miscellaneous player deaths.<br >
	 * Does not include PVP or PVE.
	 * @return <b>DeathsData</b> data store
	 */
	public DeathsData deaths() { return deathsData; }
	
	/**
	 * Returns the data store that contains the PVE information related to the player
	 * @return <b>PVEData</b> data store
	 */
	public PVEData PVE() { return PVEData; }
	
	/**
	 * Returns the data store that contains the PVP information related to the player
	 * @return <b>PVPData</b> data store
	 */
	public PVPData PVP() { return PVPData; }
	
	/**
	 * Returns the player statistics fetched from the database.
	 * @return <b>PlayerTotals</b> data store
	 */
	public PlayerTotals playerTotals() { return playerTotals; }
}
