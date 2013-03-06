package com.wolvencraft.yasp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.data.DetailedDataHolder;
import com.wolvencraft.yasp.db.data.BlocksDataHolder;
import com.wolvencraft.yasp.db.data.DeathsDataHolder;
import com.wolvencraft.yasp.db.data.ItemsDataHolder;
import com.wolvencraft.yasp.db.data.PVEDataHolder;
import com.wolvencraft.yasp.db.data.PVPDataHolder;
import com.wolvencraft.yasp.db.data.detailed.*;
import com.wolvencraft.yasp.db.data.normal.*;
import com.wolvencraft.yasp.db.tables.Normal;

public class LocalSession {
	
	public LocalSession(Player player) {
		this.playerId = DataCollector.getPlayerId(player);
		
		this.playerData = new PlayerData(player);
		this.playerDataMisc = new PlayerDataMisc(player);
		this.playerDistances = new PlayerDistances();
		this.totalBlocks = new BlocksDataHolder();
		this.totalItems = new ItemsDataHolder();
		this.totalDeaths = new DeathsDataHolder();
		this.totalPVE = new PVEDataHolder();
		this.totalPVP = new PVPDataHolder();
		
		this.detailedData = new DetailedDataHolder();
	}
	
	private int playerId;
	
	private PlayerData playerData;
	private PlayerDataMisc playerDataMisc;
	private PlayerDistances playerDistances;
	private BlocksDataHolder totalBlocks;
	private ItemsDataHolder totalItems;
	private DeathsDataHolder totalDeaths;
	private PVEDataHolder totalPVE;
	private PVPDataHolder totalPVP;
	
	private DetailedDataHolder detailedData;
	
	public void pushData() {
		playerData.pushData(playerId);
		playerDataMisc.pushData(playerId);
		playerDistances.pushData(playerId);
		totalBlocks.sync(playerId);
		totalItems.sync(playerId);
		totalDeaths.sync(playerId);
		totalPVE.sync(playerId);
		totalPVP.sync(playerId);
		detailedData.sync(playerId);
	}
	
	public void dump() {
		totalBlocks.clear();
		totalItems.clear();
		totalDeaths.clear();
		totalPVE.clear();
		totalPVP.clear();
		detailedData.clear();
	}
	
	/**
	 * Returns the unique player name.<br />
	 * Wraps around a corresponding <b>PlayerData</b> method
	 * @return <b>String</b> Player name
	 */
	public String getPlayerName() { return playerData.getName(); }
	
	/**
	 * Returns the Player object associated with the session, if it exists
	 * @deprecated
	 * @return <b>Player</b> object if it exists, <b>null</b> otherwise
	 */
	public Player getPlayer() { return Bukkit.getServer().getPlayer(playerData.getName()); }
	
	/**
	 * Returns the location of the Player associated with the session, if it exists
	 * @return <b>Location</b> if the player is online, <b>null</b> otherwise
	 */
	public Location getLocation() {
		Player player = Bukkit.getServer().getPlayer(playerData.getName());
		if(player == null) return null;
		return player.getLocation();
	}
	
	/**
	 * Returns the player's current online status.
	 * @return <b>true</b> if online, <b>false</b> otherwise
	 */
	public boolean isOnline() {
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(player.getPlayerListName().equals(playerData.getName())) return true;
		}
		return false;
	}
	
	/**
	 * <b>PlayersDistances</b> wrapper.<br />
	 * Increments the distance traveled by foot.
	 * @param distance Additional distance traveled by foot.
	 */
	public void addDistanceFoot(double distance) { playerDistances.addFootDistance(distance); }
	
	/**
	 * <b>PlayersDistances</b> wrapper.<br />
	 * Increments the distance swimmed.
	 * @param distance Additional distance swimmed.
	 */
	public void addDistanceSwimmed(double distance) { playerDistances.addSwimmedDistance(distance); }
	
	/**
	 * <b>PlayersDistances</b> wrapper.<br />
	 * Increments the distance traveled by boat.
	 * @param distance Additional distance traveled by boat
	 */
	public void addDistanceBoat(double distance) { playerDistances.addBoatDistance(distance); }
	
	/**
	 * <b>PlayersDistances</b> wrapper.<br />
	 * Increments the distance traveled by minecart.
	 * @param distance Additional distance traveled by minecart
	 */
	public void addDistanceMinecart(double distance) { playerDistances.addMinecartDistance(distance); }
	
	/**
	 * <b>PlayersDistances</b> wrapper.<br />
	 * Increments the distance traveled by pig.
	 * @param distance Additional distance traveled by pig
	 */
	public void addDistancePig(double distance) { playerDistances.addPigDistance(distance); }
	
	/**
	 * Registers player logging in with all corresponding statistics trackers.<br />
	 * Player's online status is updated in the database instantly.
	 */
	public void login() {
		playerData.setOnline(true);
		detailedData.add(new DetailedLogPlayersData(getLocation(), true));
		QueryUtils.update(
			Normal.Players.TableName.toString(),
			Normal.Players.Online.toString(),
			1 + "",
			new String[] {Normal.Players.PlayerId.toString(), playerId + ""}
		);
	}
	
	/**
	 * Registers player logging out with all corresponding statistics trackers.<br />
	 * Player's online status is updated in the database instantly.
	 */
	public void logout() {
		playerData.setOnline(false);
		detailedData.add(new DetailedLogPlayersData(getLocation(), false));
		QueryUtils.update(
				Normal.Players.TableName.toString(),
				Normal.Players.Online.toString(),
				0 + "",
				new String[] {Normal.Players.PlayerId.toString(), playerId + ""}
			);
	}
	
	/**
	 * Registers block breaking with all corresponding statistics trackers
	 * @param type Material type
	 * @param data Damage value
	 */
	public void blockBreak(Material type, byte data) {
		totalBlocks.get(type, data).addBroken();
		detailedData.add(new DetailedDestroyerdBlocksData(getLocation(), type, data));
	}
	
	/**
	 * Registers block placement with all corresponding statistics trackers
	 * @param type Material type
	 * @param data Damage value
	 */
	public void blockPlace(Material type, byte data) {
		totalBlocks.get(type, data).addPlaced();
		detailedData.add(new DetailedPlacedBlocksData(getLocation(), type, data));
	}
	
	/**
	 * Registers item drop with all corresponding statistics trackers
	 * @param itemStack Stack of items in question
	 */
	public void itemDrop(ItemStack itemStack) {
		totalItems.get(itemStack).addDropped();
		detailedData.add(new DetailedDroppedItemsData(getLocation(), itemStack));
	}
	
	/**
	 * Registers item pickup with all corresponding statistics trackers
	 * @param itemStack Stack of items in question
	 */
	public void itemPickUp(ItemStack itemStack) {
		totalItems.get(itemStack).addPickedUp();
		detailedData.add(new DetailedPickedupItemsData(getLocation(), itemStack));
	}
	
	/**
	 * Registers item use with all corresponding statistics trackers
	 * @param itemStack Stack of items in question
	 */
	public void itemUse(ItemStack itemStack) {
		totalItems.get(itemStack).addUsed();
		detailedData.add(new DetailedUsedItemsData(getLocation(), itemStack));
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
	public PlayerDataMisc misc() {
		return playerDataMisc;
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
		detailedData.add(new DetailedPVPKillsData(victim.getLocation(), victimId, weapon));
	}
	
	/**
	 * Registers creature death from a player with all corresponding statistics trackers
	 * @param killer Player who killed the victim
	 * @param victim Creature killed
	 * @param weapon Weapon used by killer
	 */
	public void playerKilledCreature(Creature victim, ItemStack weapon) {
		totalPVE.get(victim.getType(), weapon).addCreatureDeaths();
		detailedData.add(new DetailedPVEKillsData(getLocation(), victim.getType(), weapon, false));
	}
	
	/**
	 * Registers player death from a creature with all corresponding statistics trackers
	 * @param killer Creature who killed the player
	 * @param victim Player killed
	 * @param weapon Weapon used by killer
	 */
	public void creatureKilledPlayer(Creature killer, ItemStack weapon) {
		totalPVE.get(killer.getType(), weapon).addPlayerDeaths();
		detailedData.add(new DetailedPVEKillsData(getLocation(), killer.getType(), weapon, true));
	}
	
	/**
	 * Registers any other death with all corresponding statistics trackers
	 * @param player Player who died
	 * @param cause Death cause
	 */
	public void playerDied(DamageCause cause) {
		totalDeaths.get(cause).addTimes();
		detailedData.add(new DetailedDeathPlayersData(getLocation(), cause));
	}
}
