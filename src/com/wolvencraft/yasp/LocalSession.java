package com.wolvencraft.yasp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.data.*;
import com.wolvencraft.yasp.db.data.PlayersData.MiscInfoPlayers;
import com.wolvencraft.yasp.db.tables.Normal.PlayersTable;

public class LocalSession {
	
	public LocalSession(Player player) {
		this.playerId = DataCollector.getPlayerId(player);
		
		this.playerData = new PlayersData(player, playerId);
		this.totalBlocks = new BlocksData(playerId);
		this.totalItems = new ItemsData(playerId);
		this.totalDeaths = new DeathsData(playerId);
		this.totalPVE = new PVEData(playerId);
		this.totalPVP = new PVPData(playerId);
		
		this.detailedData = new DetailedData();
	}
	
	private int playerId;
	
	private PlayersData playerData;
	private BlocksData totalBlocks;
	private ItemsData totalItems;
	private DeathsData totalDeaths;
	private PVEData totalPVE;
	private PVPData totalPVP;
	
	private DetailedData detailedData;
	
	public void pushData() {
		playerData.sync();
		totalBlocks.sync();
		totalItems.sync();
		totalPVE.sync();
		totalPVP.sync();
		detailedData.sync(playerId);
	}
	
	public void dump() {
		detailedData.clear();
	}
	
	/**
	 * Returns the unique player name.<br />
	 * Wraps around a corresponding <b>PlayerData</b> method
	 * @return <b>String</b> Player name
	 */
	public String getPlayerName() { return playerData.general().getName(); }
	
	/**
	 * Returns the Player object associated with the session, if it exists
	 * @deprecated
	 * @return <b>Player</b> object if it exists, <b>null</b> otherwise
	 */
	public Player getPlayer() { return Bukkit.getServer().getPlayer(playerData.general().getName()); }
	
	/**
	 * Returns the location of the Player associated with the session, if it exists
	 * @return <b>Location</b> if the player is online, <b>null</b> otherwise
	 */
	public Location getLocation() {
		Player player = Bukkit.getServer().getPlayer(playerData.general().getName());
		if(player == null) return null;
		return player.getLocation();
	}
	
	/**
	 * Returns the player's current online status.
	 * @return <b>true</b> if online, <b>false</b> otherwise
	 */
	public boolean isOnline() {
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(player.getPlayerListName().equals(playerData.general().getName())) return true;
		}
		return false;
	}
	
	/**
	 * <b>PlayersDistances</b> wrapper.<br />
	 * Increments the distance traveled by foot.
	 * @param distance Additional distance traveled by foot.
	 */
	public void addDistanceFoot(double distance) { playerData.distance().addFootDistance(distance); }
	
	/**
	 * <b>PlayersDistances</b> wrapper.<br />
	 * Increments the distance swimmed.
	 * @param distance Additional distance swimmed.
	 */
	public void addDistanceSwimmed(double distance) { playerData.distance().addSwimmedDistance(distance); }
	
	/**
	 * <b>PlayersDistances</b> wrapper.<br />
	 * Increments the distance traveled by boat.
	 * @param distance Additional distance traveled by boat
	 */
	public void addDistanceBoat(double distance) { playerData.distance().addBoatDistance(distance); }
	
	/**
	 * <b>PlayersDistances</b> wrapper.<br />
	 * Increments the distance traveled by minecart.
	 * @param distance Additional distance traveled by minecart
	 */
	public void addDistanceMinecart(double distance) { playerData.distance().addMinecartDistance(distance); }
	
	/**
	 * <b>PlayersDistances</b> wrapper.<br />
	 * Increments the distance traveled by pig.
	 * @param distance Additional distance traveled by pig
	 */
	public void addDistancePig(double distance) { playerData.distance().addPigDistance(distance); }
	
	/**
	 * Registers player logging in with all corresponding statistics trackers.<br />
	 * Player's online status is updated in the database instantly.
	 */
	public void login() {
		playerData.general().setOnline(true);
		detailedData.add(playerData.new DetailedLogPlayersEntry(getLocation(), true));
		QueryUtils.update(
			PlayersTable.TableName.toString(),
			PlayersTable.Online.toString(),
			1 + "",
			new String[] {PlayersTable.PlayerId.toString(), playerId + ""}
		);
	}
	
	/**
	 * Registers player logging out with all corresponding statistics trackers.<br />
	 * Player's online status is updated in the database instantly.
	 */
	public void logout() {
		playerData.general().setOnline(false);
		detailedData.add(playerData.new DetailedLogPlayersEntry(getLocation(), false));
		QueryUtils.update(
				PlayersTable.TableName.toString(),
				PlayersTable.Online.toString(),
				0 + "",
				new String[] {PlayersTable.PlayerId.toString(), playerId + ""}
			);
	}
	
	/**
	 * Registers block breaking with all corresponding statistics trackers
	 * @param type Material type
	 * @param data Damage value
	 */
	public void blockBreak(Material type, byte data) {
		totalBlocks.get(type, data).addBroken();
		detailedData.add(totalBlocks.new DetailedDestroyerdBlocksEntry(getLocation(), type, data));
	}
	
	/**
	 * Registers block placement with all corresponding statistics trackers
	 * @param type Material type
	 * @param data Damage value
	 */
	public void blockPlace(Material type, byte data) {
		totalBlocks.get(type, data).addPlaced();
		detailedData.add(totalBlocks.new DetailedPlacedBlocksEntry(getLocation(), type, data));
	}
	
	/**
	 * Registers item drop with all corresponding statistics trackers
	 * @param itemStack Stack of items in question
	 */
	public void itemDrop(ItemStack itemStack) {
		totalItems.get(itemStack).addDropped();
		detailedData.add(totalItems.new DetailedDroppedItemsEntry(getLocation(), itemStack));
	}
	
	/**
	 * Registers item pickup with all corresponding statistics trackers
	 * @param itemStack Stack of items in question
	 */
	public void itemPickUp(ItemStack itemStack) {
		totalItems.get(itemStack).addPickedUp();
		detailedData.add(totalItems.new DetailedPickedupItemsEntry(getLocation(), itemStack));
	}
	
	/**
	 * Registers item use with all corresponding statistics trackers
	 * @param itemStack Stack of items in question
	 */
	public void itemUse(ItemStack itemStack) {
		totalItems.get(itemStack).addUsed();
		detailedData.add(totalItems.new DetailedUsedItemsEntry(getLocation(), itemStack));
	}
	
	/**
	 * Registers item crafting with all corresponding statistics trackers
	 * @param itemStack Stack of items in question
	 */
	public void itemCraft(ItemStack itemStack) {
		totalItems.get(itemStack).addCrafted();
	}
	
	/**
	 * Registers item smelting with all corresponding statistics trackers
	 * @param itemStack Stack of items in question
	 */
	public void itemSmelt(ItemStack itemStack) {
		totalItems.get(itemStack).addSmelted();
	}
	
	/**
	 * Registers tool breaking with all corresponding statistics trackers
	 * @param itemStack Stack of items in question
	 */
	public void toolBreak(ItemStack itemStack) {
		totalItems.get(itemStack).addBroken();
	}
	
	/**
	 * Registers item enchantment with all corresponding statistics trackers
	 * @param itemStack Stack of items in question
	 */
	public void itemEnchant(ItemStack itemStack) {
		totalItems.get(itemStack).addEnchanted();
	}
	
	/**
	 * Allows for direct access to miscellaneous data collector
	 * @return <b>PlayerDataMisc</b> miscellaneous data collector
	 */
	public MiscInfoPlayers misc() {
		return playerData.misc();
	}
	
	/**
	 * Registers player death from other player with all corresponding statistics trackers
	 * @param killer Player who killed the victim
	 * @param victim Player who was killed 
	 * @param weapon Weapon used by killer
	 */
	public void playerKilledPlayer(Player victim, ItemStack weapon) {
		int victimId = DataCollector.getPlayerId(victim);
		totalPVP.get(victimId, weapon).addTimes();
		detailedData.add(totalPVP.new DetailedPVPEntry(victim.getLocation(), victimId, weapon));
	}
	
	/**
	 * Registers creature death from a player with all corresponding statistics trackers
	 * @param killer Player who killed the victim
	 * @param victim Creature killed
	 * @param weapon Weapon used by killer
	 */
	public void playerKilledCreature(Creature victim, ItemStack weapon) {
		totalPVE.get(victim.getType(), weapon).addCreatureDeaths();
		detailedData.add(totalPVE.new DetailedPVEEntry(getLocation(), victim.getType(), weapon, false));
	}
	
	/**
	 * Registers player death from a creature with all corresponding statistics trackers
	 * @param killer Creature who killed the player
	 * @param victim Player killed
	 * @param weapon Weapon used by killer
	 */
	public void creatureKilledPlayer(Creature killer, ItemStack weapon) {
		totalPVE.get(killer.getType(), weapon).addPlayerDeaths();
		detailedData.add(totalPVE.new DetailedPVEEntry(getLocation(), killer.getType(), weapon, true));
	}
	
	/**
	 * Registers any other death with all corresponding statistics trackers
	 * @param player Player who died
	 * @param cause Death cause
	 */
	public void playerDied(DamageCause cause) {
		totalDeaths.get(cause).addTimes();
		detailedData.add(totalDeaths.new DetailedDeathEntry(getLocation(), cause));
	}
}
