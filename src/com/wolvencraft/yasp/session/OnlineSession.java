/*
 * OnlineSession.java
 * 
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

package com.wolvencraft.yasp.session;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.data.AdvancedDataStore;
import com.wolvencraft.yasp.db.data.AdvancedDataStore.DataStoreType;
import com.wolvencraft.yasp.db.data.blocks.BlocksData;
import com.wolvencraft.yasp.db.data.deaths.DeathsData;
import com.wolvencraft.yasp.db.data.items.ItemsData;
import com.wolvencraft.yasp.db.data.players.PlayersData;
import com.wolvencraft.yasp.db.data.pve.PVEData;
import com.wolvencraft.yasp.db.data.pvp.PVPData;
import com.wolvencraft.yasp.db.tables.Normal.DistancePlayersTable;
import com.wolvencraft.yasp.db.tables.Normal.MiscInfoPlayersTable;
import com.wolvencraft.yasp.db.tables.Normal.PlayersTable;
import com.wolvencraft.yasp.db.totals.PlayerTotals;
import com.wolvencraft.yasp.util.NamedInteger;
import com.wolvencraft.yasp.util.Util;
import com.wolvencraft.yasp.util.cache.OnlineSessionCache;
import com.wolvencraft.yasp.util.cache.PlayerCache;

/**
 * Represents a player session that is created when a player logs into the server.<br />
 * This session can only be created from a Player object, therefore, the player must have
 * logged into the server at least once.
 * @author bitWolfy
 *
 */
@SuppressWarnings("rawtypes")
public class OnlineSession implements PlayerSession {
    
    private final int id;
    private final String name;
    
    private PlayersData playersData;
    private List<AdvancedDataStore> dataStores;
    
    private PlayerTotals playerTotals;
    private Scoreboard scoreboard;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new player session from the Player object
     * @param player Player object
     */
    public OnlineSession(Player player) {
        name = player.getName();
        id = PlayerCache.get(player);
        
        this.playersData = new PlayersData(player, id);
        
        this.dataStores = new ArrayList<AdvancedDataStore>();
        this.dataStores.addAll(Util.getModules(player, id));
        this.dataStores.addAll(Util.getHooks(player, id));
        
        this.playerTotals = new PlayerTotals(id);
        this.scoreboard = null;
    }
    
    /**
     * <b>Constructor</b><Br />
     * Creates a new online session from an offline session
     * @param session Session to inherit
     * @throws InstantiationException Thrown if the player is not currently online
     */
    public OnlineSession(OfflineSession session) throws InstantiationException {
        id = session.getId();
        name = session.getName();
        
        Player player = Bukkit.getPlayerExact(name);
        if(player == null) throw new InstantiationException("Player " + name + " is not online!");

        this.playersData = new PlayersData(player, id);
        this.dataStores = Util.getModules(player, id);
        this.playerTotals = new PlayerTotals(id);
    }
    
    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isOnline() {
        if(Bukkit.getPlayerExact(name) == null) return false;
        return true;
    }
    
    /**
     * Returns the total playtime for the player
     * @return Total playtime
     */
    public long getPlaytime() {
        return playersData.getGeneralData().getTotalPlaytime();
    }
    
    /**
     * Returns the data store with the specified type
     * @param type Data store type
     * @return Data store, or <b>null</b> if the type is not valid
     */
    public AdvancedDataStore getData(DataStoreType type) {
        for(AdvancedDataStore store : dataStores) {
            if(store.getType().equals(type)) return store;
        }
        return null;
    }
    
    /**
     * Performs a database operation to push the locally stored data.
     */
    public void pushData() {
        playersData.sync();
        for(AdvancedDataStore store : dataStores) store.pushData();
        
        playerTotals.fetchData();
    }
    
    /**
     * Dumps all locally stored data
     */
    public void dumpData() {
        for(AdvancedDataStore store : dataStores) store.dump();
    }
    
    /**
     * Returns the totals associated with this player
     * @return Player totals
     */
    public PlayerTotals getTotals() {
        return playerTotals;
    }
    
    /**
     * Player has logged in
     * @param location Location on login
     */
    public void login(Location location) {
        playersData.addPlayerLog(location, true);
        Query.table(PlayersTable.TableName)
            .value(PlayersTable.Online, true)
            .condition(PlayersTable.PlayerId, id)
            .update();
    }
    
    /**
     * Player has logged out, but the logout event was not caught
     */
    public void logout() {
        Query.table(PlayersTable.TableName)
            .value(PlayersTable.Online, false)
            .condition(PlayersTable.PlayerId, id)
            .update();
    }
    
    /**
     * Player has logged out
     * @param location Location on logout
     */
    public void logout(Location location) {
        playersData.addPlayerLog(location, false);
        Query.table(PlayersTable.TableName)
            .value(PlayersTable.Online, false)
            .condition(PlayersTable.PlayerId, id)
            .update();
    }
    
    /**
     * Add distance of the specified type to the statistics
     * @param type Travel type
     * @param distance Distance traveled
     */
    public void addDistance(DistancePlayersTable type, double distance) {
        playersData.getDistanceData().addDistance(type, distance);
        playerTotals.addDistance(type, distance);
    }
    
    /**
     * Sets the value of a miscellaneous value
     * @param type Value type
     * @param value Value
     */
    public void addMiscValue(MiscInfoPlayersTable type, int value) {
        playersData.getMiscData().incrementStat(type, value);
    }
    
    /**
     * Sets the value of a miscellaneous value
     * @param type Value type
     */
    public void addMiscValue(MiscInfoPlayersTable type) {
        playersData.getMiscData().incrementStat(type);
    }
    
    /**
     * Registers the player death in the data store
     * @param victim Player who was killed 
     * @param weapon Weapon used by killer
     */
    public void killedPlayer(Player victim, ItemStack weapon) {
        ((PVPData) getData(DataStoreType.PVP)).playerKilledPlayer(victim, weapon);
        playersData.getMiscData().killed(victim);
        playerTotals.pvpKill();
        OnlineSessionCache.fetch(victim).getTotals().death();
    }
    
    /**
     * Registers the creature death in the data store
     * @param victim Creature killed
     * @param weapon Weapon used by killer
     */
    public void killedCreature(Entity victim, ItemStack weapon) {
        ((PVEData) getData(DataStoreType.PVE)).playerKilledCreature(victim, weapon);
        playerTotals.pveKill();
    }
    
    /**
     * Registers the player death in the data store
     * @param killer Creature that killed the player
     * @param weapon Weapon used by killer
     */
    public void killedByCreature(Entity killer, ItemStack weapon) {
        ((PVEData) getData(DataStoreType.PVE)).creatureKilledPlayer(killer, weapon);
        died();
    }
    
    /**
     * Runs when the session owner was killed by the environment
     * @param location Location of the death
     * @param cause Death cause
     */
    public void killedByEnvironment(Location location, DamageCause cause) {
        ((DeathsData) getData(DataStoreType.Deaths)).playerDied(location, cause);
        died();
    }
    
    /**
     * Runs when the player dies (any cause).<br />
     * This method is for internal use; you do not need to run it from listener
     */
    public void died() {
        playersData.getMiscData().died();
        playerTotals.death();
    }
    
    /**
     * Registers the broken block in the data stores
     * @param location Location of the block
     * @param block BlockState of the block
     */
    public void blockBreak(Location location, BlockState block) {
        ((BlocksData) getData(DataStoreType.Blocks)).blockBreak(location, block);
        playerTotals.blockBreak();
    }
    
    /**
     * Registers the placed block in the data stores
     * @param location Location of the block
     * @param block BlockState of the block
     */
    public void blockPlace(Location location, BlockState block) {
        ((BlocksData) getData(DataStoreType.Blocks)).blockPlace(location, block);
        playerTotals.blockPlace();
    }
    
    /**
     * Registers the dropped item in the data stores
     * @param location Location of the event
     * @param itemStack Stack of items in question
     */
    public void itemDrop(Location location, ItemStack itemStack) {
        ((ItemsData) getData(DataStoreType.Items)).itemDrop(location, itemStack);
    }
    
    /**
     * Registers the picked up item in the data stores
     * @param location Location of the event
     * @param itemStack Stack of items in question
     */
    public void itemPickUp(Location location, ItemStack itemStack) {
        ((ItemsData) getData(DataStoreType.Items)).itemPickUp(location, itemStack);
    }
    
    /**
     * Registers the used item in the data stores
     * @param location Location of the event
     * @param itemStack Stack of items in question
     */
    public void itemUse(Location location, ItemStack itemStack) {
        ((ItemsData) getData(DataStoreType.Items)).itemUse(location, itemStack);
        playerTotals.snacksEaten();
    }
    
    /**
     * Registers the crafted item in the data stores
     * @param location Location of the event
     * @param itemStack Stack of items in question
     */
    public void itemCraft(Location location, ItemStack itemStack) {
        ((ItemsData) getData(DataStoreType.Items)).itemCraft(location, itemStack);
        playerTotals.itemCraft();
    }
    
    /**
     * Registers the smelted item in the data stores
     * @param location Location of the event
     * @param itemStack Stack of items in question
     */
    public void itemSmelt(Location location, ItemStack itemStack) {
        ((ItemsData) getData(DataStoreType.Items)).itemSmelt(location, itemStack);
    }
    
    /**
     * Registers the broken item in the data stores
     * @param location Location of the event
     * @param itemStack Stack of items in question
     */
    public void itemBreak(Location location, ItemStack itemStack) {
        ((ItemsData) getData(DataStoreType.Items)).itemBreak(location, itemStack);
        playerTotals.toolBreak();
    }
    
    /**
     * Registers the enchanted item in the data stores
     * @param location Location of the event
     * @param itemStack Stack of items in question
     */
    public void itemEnchant(Location location, ItemStack itemStack) {
        ((ItemsData) getData(DataStoreType.Items)).itemEnchant(location, itemStack);
    }
    
    /**
     * Registers the reparied item in the data stores
     * @param location Location of the event
     * @param itemStack Stack of items in question
     */
    public void itemRepair(Location location, ItemStack itemStack) {
        ((ItemsData) getData(DataStoreType.Items)).itemRepair(location, itemStack);
    }
    
    /**
     * Toggles the scoreboard state
     * @return <b>true</b> if the scoreboard has been turned on, <b>false</b> if it is now off
     */
    public boolean toggleScoreboard() {
        if(scoreboard != null) {
            clearScoreboard();
            scoreboard = null;
            return false;
        }
        
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        scoreboard = manager.getNewScoreboard();
        Bukkit.getServer().getPlayer(getName()).setScoreboard(scoreboard);
        
        scoreboard.clearSlot(DisplaySlot.SIDEBAR);
        Objective stats = scoreboard.registerNewObjective("stats", "dummy");
        stats.setDisplaySlot(DisplaySlot.SIDEBAR);
        stats.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Statistics");        
        return true;
    }
    
    /**
     * Refreshes the statistics displayed on a scoreboard
     */
    public void refreshScoreboard() {
        if(scoreboard == null) return;

        Objective stats = scoreboard.getObjective("stats");
        
        for(NamedInteger value : playerTotals.getNamedValues()) {
            for(String name : value.getPossibleNames()) scoreboard.resetScores(Bukkit.getOfflinePlayer(name));
            stats.getScore(Bukkit.getOfflinePlayer(value.getName()))
                 .setScore((Integer) (value.getValue()));
        }
    }
    
    /**
     * Clears the scoreboard from all content
     */
    public void clearScoreboard() {
        if(scoreboard == null) return;
        scoreboard.resetScores(Bukkit.getOfflinePlayer(ChatColor.RED + "PVP Kills"));
        scoreboard.resetScores(Bukkit.getOfflinePlayer(ChatColor.RED + "PVE Kills"));
        scoreboard.resetScores(Bukkit.getOfflinePlayer(ChatColor.RED + "Deaths"));
        
        scoreboard.resetScores(Bukkit.getOfflinePlayer(ChatColor.AQUA + "Blocks Broken"));
        scoreboard.resetScores(Bukkit.getOfflinePlayer(ChatColor.AQUA + "Blocks Placed"));

        scoreboard.resetScores(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Travelled"));
    }
}
