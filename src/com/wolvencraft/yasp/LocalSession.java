package com.wolvencraft.yasp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.data.*;

public class LocalSession {
	
	public LocalSession(Player player) {
		this.playerId = DataCollector.getPlayerId(player);
		
		this.playersData = new PlayersData(player, playerId);
		this.blocksData = new BlocksData(playerId);
		this.itemsData = new ItemsData(playerId);
		this.deathsData = new DeathsData(playerId);
		this.PVEData = new PVEData(playerId);
		this.PVPData = new PVPData(playerId);
	}
	
	private int playerId;
	
	private PlayersData playersData;
	private BlocksData blocksData;
	private ItemsData itemsData;
	private DeathsData deathsData;
	private PVEData PVEData;
	private PVPData PVPData;
	
	public void pushData() {
		playersData.sync();
		blocksData.sync();
		itemsData.sync();
		deathsData.sync();
		PVEData.sync();
		PVPData.sync();
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
}
