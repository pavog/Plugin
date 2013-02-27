package com.wolvencraft.yasp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.data.DetailedDataHolder;
import com.wolvencraft.yasp.db.data.BlocksDataHolder;
import com.wolvencraft.yasp.db.data.DeathsDataHolder;
import com.wolvencraft.yasp.db.data.ItemsDataHolder;
import com.wolvencraft.yasp.db.data.PVEDataHolder;
import com.wolvencraft.yasp.db.data.PVPDataHolder;
import com.wolvencraft.yasp.db.data.detailed.*;
import com.wolvencraft.yasp.db.data.normal.*;
import com.wolvencraft.yasp.db.tables.normal.Players;

public class LocalSession {
	
	public LocalSession(Player player) {
		this.playerName = player.getPlayerListName();
		this.playerId = DataCollector.getCachedPlayerId(playerName);
		this.playerData = new PlayerData(player, playerName, playerId);
		
		this.playersDistances = new PlayerDistances(playerId);
		
		this.totalBlocks = new BlocksDataHolder();
		this.totalItems = new ItemsDataHolder();
		this.totalDeaths = new DeathsDataHolder();
		this.totalPVE = new PVEDataHolder();
		this.totalPVP = new PVPDataHolder();
		
		this.detailedData = new DetailedDataHolder();
	}
	
	private String playerName;
	private int playerId;
	
	private PlayerData playerData;
	private PlayerDistances playersDistances;
	private BlocksDataHolder totalBlocks;
	private ItemsDataHolder totalItems;
	private DeathsDataHolder totalDeaths;
	private PVEDataHolder totalPVE;
	private PVPDataHolder totalPVP;
	
	private DetailedDataHolder detailedData;
	
	public void pushData() {
		playerData.pushData();
		playersDistances.pushData();
		totalBlocks.sync();
		totalItems.sync();
		totalDeaths.sync();
		totalPVE.sync();
		totalPVP.sync();
		detailedData.sync();
	}
	
	/**
	 * Returns the unique player name
	 * @return <b>String</b> Player name
	 */
	public String getPlayerName() { return playerName; }
	
	/**
	 * Returns the database ID of the player
	 * @return <b>int</b> Player ID
	 */
	public int getPlayerId() { return playerId; }
	
	/**
	 * Returns the Player object associated with the session, if it exists
	 * @return <b>Player</b> object if it exists, <b>null</b> otherwise
	 */
	public Player getPlayer() { return Bukkit.getServer().getPlayer(playerName); }
	
	/**
	 * Returns the player's current online status.
	 * @return <b>true</b> if online, <b>false</b> otherwise
	 */
	public boolean getOnline() {
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(player.getPlayerListName().equals(playerName)) return true;
		}
		return false;
	}
	
	/**
	 * <b>PlayersDistances</b> wrapper.<br />
	 * Increments the distance traveled by foot.
	 * @param distance Additional distance traveled by foot.
	 */
	public void addDistanceFoot(double distance) { playersDistances.addFootDistance(distance); }
	
	/**
	 * <b>PlayersDistances</b> wrapper.<br />
	 * Increments the distance traveled by boat.
	 * @param distance Additional distance traveled by boat
	 */
	public void addDistanceBoat(double distance) { playersDistances.addBoatDistance(distance); }
	
	/**
	 * <b>PlayersDistances</b> wrapper.<br />
	 * Increments the distance traveled by minecart.
	 * @param distance Additional distance traveled by minecart
	 */
	public void addDistanceMinecart(double distance) { playersDistances.addMinecartDistance(distance); }
	
	/**
	 * <b>PlayersDistances</b> wrapper.<br />
	 * Increments the distance traveled by pig.
	 * @param distance Additional distance traveled by pig
	 */
	public void addDistancePig(double distance) { playersDistances.addPigDistance(distance); }
	
	/**
	 * Registers player logging in with all corresponding statistics trackers.<br />
	 * Player's online status is updated in the database instantly.
	 */
	public void login() {
		playerData.setOnline(true);
		detailedData.add(new DetailedLogPlayersData(getPlayer(), playerId, true));
		QueryUtils.update(
			Players.TableName.toString(),
			Players.Online.toString(),
			1 + "",
			Players.PlayerId + " = " + playerId
		);
	}
	
	/**
	 * Registers player logging out with all corresponding statistics trackers.<br />
	 * Player's online status is updated in the database instantly.
	 */
	public void logout() {
		playerData.setOnline(false);
		detailedData.add(new DetailedLogPlayersData(getPlayer(), playerId, false));
		QueryUtils.update(
				Players.TableName.toString(),
				Players.Online.toString(),
				0 + "",
				Players.PlayerId + " = " + playerId
			);
	}
	
	/**
	 * Registers block breaking with all corresponding statistics trackers
	 * @param materialData Data of the block in question
	 */
	public void blockBreak(MaterialData materialData) {
		totalBlocks.get(playerId, materialData).addBroken();
		detailedData.add(new DetailedDestroyerdBlocksData(getPlayer(), materialData));
	}
	
	/**
	 * Registers block placement with all corresponding statistics trackers
	 * @param materialData Data of the block in question
	 */
	public void blockPlace(MaterialData materialData) {
		totalBlocks.get(playerId, materialData).addPlaced();
		detailedData.add(new DetailedPlacedBlocksData(getPlayer(), materialData));
	}
	
	/**
	 * Registers item drop with all corresponding statistics trackers
	 * @param itemStack Stack of items in question
	 */
	public void itemDrop(ItemStack itemStack) {
		totalItems.get(playerId, itemStack).addDropped();
		detailedData.add(new DetailedDroppedItemsData(getPlayer(), itemStack));
	}
	
	/**
	 * Registers item pickup with all corresponding statistics trackers
	 * @param itemStack Stack of items in question
	 */
	public void itemPickUp(ItemStack itemStack) {
		totalItems.get(playerId, itemStack).addPickedUp();
		detailedData.add(new DetailedPickedupItemsData(getPlayer(), itemStack));
	}
	
	/**
	 * Registers item use with all corresponding statistics trackers
	 * @param itemStack Stack of items in question
	 */
	public void itemUse(ItemStack itemStack) {
		totalItems.get(playerId,  itemStack).addUsed();
		detailedData.add(new DetailedUsedItemsData(getPlayer(), itemStack));
	}
	
	/**
	 * Registers item crafting with all corresponding statistics trackers
	 * @param itemStack Stack of items in question
	 */
	public void itemCraft(ItemStack itemStack) {
		totalItems.get(playerId, itemStack).addCrafted();
	}
	
	/**
	 * Registers item smelting with all corresponding statistics trackers
	 * @param itemStack Stack of items in question
	 */
	public void itemSmelt(ItemStack itemStack) {
		totalItems.get(playerId, itemStack).addSmelted();
	}
	
	/**
	 * Registers player death from other player with all corresponding statistics trackers
	 * @param killer Player who killed the victim
	 * @param victim Player who was killed 
	 * @param weapon Weapon used by killer
	 */
	public void playerKilledPlayer(Player killer, Player victim, ItemStack weapon) {
		int victimId = DataCollector.getCachedPlayerId(victim.getPlayerListName());
		totalPVP.get(playerId, victimId, weapon).addTimes();
		detailedData.add(new DetailedPVPKillsData(killer, victim, weapon));
	}
	
	/**
	 * Registers creature death from a player with all corresponding statistics trackers
	 * @param killer Player who killed the victim
	 * @param victim Creature killed
	 * @param weapon Weapon used by killer
	 */
	public void playerKilledCreature(Player killer, Creature victim, ItemStack weapon) {
		totalPVE.get(playerId, victim.getType(), weapon).addCreatureDeaths();
		detailedData.add(new DetailedPVEKillsData(killer, victim.getType(), weapon, false));
	}
	
	/**
	 * Registers player death from a creature with all corresponding statistics trackers
	 * @param killer Creature who killed the player
	 * @param victim Player killed
	 * @param weapon Weapon used by killer
	 */
	public void creatureKilledPlayer(Creature killer, Player victim, ItemStack weapon) {
		totalPVE.get(playerId, killer.getType(), weapon).addPlayerDeaths();
		detailedData.add(new DetailedPVEKillsData(victim, killer.getType(), weapon, true));
	}
	
	/**
	 * Registers any other death with all corresponding statistics trackers
	 * @param player Player who died
	 * @param cause Death cause
	 */
	public void playerDied(Player player, DamageCause cause) {
		totalDeaths.get(playerId, cause).addTimes();
		detailedData.add(new DetailedDeathPlayersData(player, cause));
	}
}
