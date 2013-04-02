/*
 * PlayersData.java
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

package com.wolvencraft.yasp.db.data.sync;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.LocalSession;
import com.wolvencraft.yasp.Settings;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.tables.Detailed.LogPlayers;
import com.wolvencraft.yasp.db.tables.Normal.DistancePlayersTable;
import com.wolvencraft.yasp.db.tables.Normal.MiscInfoPlayersTable;
import com.wolvencraft.yasp.db.tables.Normal.PlayersInv;
import com.wolvencraft.yasp.db.tables.Normal.PlayersTable;
import com.wolvencraft.yasp.util.SimpleInventoryItem;
import com.wolvencraft.yasp.util.SimplePotionEffect;
import com.wolvencraft.yasp.util.Util;

/**
 * Not a real data store, but a combination of three separate tables:
 * Players, Distances, Miscellaneous
 * @author bitWolfy
 *
 */
public class PlayersData implements DataStore {
    
    private int playerId;
    private Players generalData;
    private DistancePlayers distanceData;
    private MiscInfoPlayers miscData;
    private InventoryData inventoryData;
    private List<DetailedData> detailedData;
    
    public PlayersData(Player player, int playerId) {
        this.playerId = playerId;
        generalData = new Players(playerId, player);
        distanceData = new DistancePlayers(playerId);
        miscData = new MiscInfoPlayers(playerId, player);
        inventoryData = new InventoryData(playerId);
        
        detailedData = new ArrayList<DetailedData>();
    }
    
    @Override
    public List<NormalData> getNormalData() {
        return null;
    }
    
    @Override
    public List<DetailedData> getDetailedData() {
        List<DetailedData> temp = new ArrayList<DetailedData>();
        for(DetailedData value : detailedData) temp.add(value);
        return temp;
    }
    
    @Override
    public void sync() {
        generalData.pushData(playerId);
        distanceData.pushData(playerId);
        miscData.pushData(playerId);
        inventoryData.pushData(playerId);
        
        for(DetailedData entry : getDetailedData()) {
            if(entry.pushData(playerId)) detailedData.remove(entry);
        }
    }
    
    @Override
    public void dump() {
        for(DetailedData entry : getDetailedData()) {
            detailedData.remove(entry);
        }
    }
    
    /**
     * Returns the generic player data from the Players table.<br />
     * This information rarely changes
     * @return Players data store
     */
    public Players general() {
        return generalData;
    }
    
    /**
     * Returns the information from the Distances table.
     * @return Distances data store
     */
    public DistancePlayers distance() {
        return distanceData;
    }
    
    /**
     * Returns the information from the Miscellaneous table.<br />
     * This information is likely to change rapidly.
     * @return Miscellaneous data store
     */
    public MiscInfoPlayers misc() {
        return miscData;
    }
    
    /**
     * Registers player logging in with all corresponding statistics trackers.<br />
     * Player's online status is updated in the database instantly.
     */
    public void login(Location location) {
        generalData.setOnline(true);
        detailedData.add(new DetailedLogPlayersEntry(location, true));
        Query.table(PlayersTable.TableName)
            .value(PlayersTable.Online, true)
            .condition(PlayersTable.PlayerId, playerId)
            .update();
    }
    
    /**
     * Registers player logging out with all corresponding statistics trackers.<br />
     * Player's online status is updated in the database instantly.
     */
    public void logout(Location location) {
        generalData.setOnline(false);
        detailedData.add(new DetailedLogPlayersEntry(location, false));
        Query.table(PlayersTable.TableName)
            .value(PlayersTable.Online, false)
            .condition(PlayersTable.PlayerId, playerId)
            .update();
    }
    
    /**
     * Represents the Player data that is being tracked.<br />
     * Each entry must have a unique player name.
     * @author bitWolfy
     *
     */
    public class Players implements NormalData {

        private long lastSync;
        
        private String playerName;
        
        private boolean online;
        private long sessionStart;
        private long totalPlaytime;
        private long firstJoin;
        private int logins;
        
        public Players (int playerId, Player player) {
            this.lastSync = Util.getTimestamp();
            
            this.playerName = player.getName();
            
            this.online = true;
            this.sessionStart = lastSync;
            this.totalPlaytime = 0;
            this.firstJoin = lastSync;
            this.logins = 0;
            
            fetchData(playerId);
            
            logins++;
        }
        
        @Override
        public void fetchData(int playerId) {
            QueryResult result = Query.table(PlayersTable.TableName)
                .column(PlayersTable.Logins)
                .column(PlayersTable.FirstLogin)
                .column(PlayersTable.TotalPlaytime)
                .condition(PlayersTable.PlayerId, playerId)
                .select();
            if(result == null) {
                Query.table(PlayersTable.TableName)
                    .value(PlayersTable.PlayerId, playerId)
                    .value(PlayersTable.Name, playerName)
                    .value(PlayersTable.Online, online)
                    .value(PlayersTable.SessionStart, sessionStart)
                    .value(PlayersTable.FirstLogin, firstJoin)
                    .value(PlayersTable.Logins, logins)
                    .value(PlayersTable.TotalPlaytime, totalPlaytime)
                    .insert();
            } else {
                firstJoin = result.asLong(PlayersTable.FirstLogin);
                if(firstJoin == -1) firstJoin = Util.getTimestamp();
                logins = result.asInt(PlayersTable.Logins);
                totalPlaytime = result.asLong(PlayersTable.TotalPlaytime);
            }
        }

        @Override
        public boolean pushData(int playerId) {
            online = Bukkit.getServer().getPlayerExact(playerName) != null;
            if(online) {
                totalPlaytime += (lastSync - sessionStart);
                lastSync = Util.getTimestamp();
            }
            boolean result = Query.table(PlayersTable.TableName)
                .value(PlayersTable.Name, playerName)
                .value(PlayersTable.Online, online)
                .value(PlayersTable.SessionStart, sessionStart)
                .value(PlayersTable.FirstLogin, firstJoin)
                .value(PlayersTable.Logins, logins)
                .value(PlayersTable.TotalPlaytime, totalPlaytime)
                .condition(PlayersTable.PlayerId, playerId)
                .update();
            fetchData(playerId);
            return result;
        }
        
        /**
         * Returns the player name
         * @return Player name
         */
        public String getName() {
            return playerName;
        }
        
        /**
         * Changes the online status of the player
         * @param online New online status
         */
        public void setOnline(boolean online) { 
            this.online = online;
            if(!online) totalPlaytime += Util.getTimestamp() - sessionStart;
        }
    }
    
    /**
     * Represents the distances a player traveled.
     * Only one entry per player is allowed.
     * @author bitWolfy
     *
     */
    public class DistancePlayers implements NormalData {
        
        private double foot;
        private double swim;
        private double flight;
        private double boat;
        private double minecart;
        private double pig;
        
        /**
         * Default constructor. Takes in the Player object and pulls corresponding values from the remote database.<br />
         * If no data is found in the database, the default values are inserted.
         * @param playerId ID of the tracked player
         */
        public DistancePlayers(int playerId) {
            this.foot = 0;
            this.swim = 0;
            this.flight = 0;
            this.boat = 0;
            this.minecart = 0;
            this.pig = 0;
            
            fetchData(playerId);
        }
        
        @Override
        public void fetchData(int playerId) {
            QueryResult result = Query.table(DistancePlayersTable.TableName)
                    .column(DistancePlayersTable.Foot)
                    .column(DistancePlayersTable.Swimmed)
                    .column(DistancePlayersTable.Flight)
                    .column(DistancePlayersTable.Boat)
                    .column(DistancePlayersTable.Minecart)
                    .column(DistancePlayersTable.Pig)
                    .condition(DistancePlayersTable.PlayerId, playerId)
                    .select();
            if(result == null) {
                Query.table(DistancePlayersTable.TableName)
                    .value(DistancePlayersTable.PlayerId, playerId)
                    .value(DistancePlayersTable.Foot, foot)
                    .value(DistancePlayersTable.Swimmed, swim)
                    .value(DistancePlayersTable.Flight, flight)
                    .value(DistancePlayersTable.Boat, boat)
                    .value(DistancePlayersTable.Minecart, minecart)
                    .value(DistancePlayersTable.Pig, pig)
                    .insert();
            }
            else {
                foot = result.asInt(DistancePlayersTable.Foot);
                swim = result.asInt(DistancePlayersTable.Swimmed);
                flight = result.asInt(DistancePlayersTable.Flight);
                boat = result.asInt(DistancePlayersTable.Boat);
                minecart = result.asInt(DistancePlayersTable.Minecart);
                pig = result.asInt(DistancePlayersTable.Pig);
            }
        }

        @Override
        public boolean pushData(int playerId) {
            boolean result = Query.table(DistancePlayersTable.TableName)
                .value(DistancePlayersTable.Foot, foot)
                .value(DistancePlayersTable.Swimmed, swim)
                .value(DistancePlayersTable.Flight, flight)
                .value(DistancePlayersTable.Boat, boat)
                .value(DistancePlayersTable.Minecart, minecart)
                .value(DistancePlayersTable.Pig, pig)
                .condition(DistancePlayersTable.PlayerId, playerId)
                .update();
            if(Settings.LocalConfiguration.Cloud.asBoolean()) fetchData(playerId);
            return result;
        }
        
        /**
         * Increments the distance traveled by foot.
         * @param distance Additional distance traveled by foot.
         */
        public void addDistanceFoot(double distance) {
            foot += distance;
        }
        
        /**
         * Increments the distance swimmed.
         * @param distance Additional distance swimmed.
         */
        public void addDistanceSwimmed(double distance) {
            swim += distance;
        }
        
        /**
         * Increments the distance traveled by boat.
         * @param distance Additional distance traveled by boat
         */
        public void addDistanceBoat(double distance) {
            boat += distance;
        }
        
        /**
         * Increments the distance traveled by minecart.
         * @param distance Additional distance traveled by minecart
         */
        public void addDistanceMinecart(double distance) {
            minecart += distance;
        }
        
        /**
         * Increments the distance traveled by pig.
         * @param distance Additional distance traveled by pig
         */
        public void addDistancePig(double distance) {
            pig += distance;
        }
        
        /**
         * Increments the distance flown.
         * @param distance Additional distance flown
         */
        public void addDistanceFlown(double distance) {
            flight += distance;
        }
    }
    
    public class MiscInfoPlayers implements NormalData {
        
        private String playerName;
        private String playerIp;

        private boolean isOp;
        private boolean isBanned;
        
        private int gamemode;
        private float expPercent;
        private int expTotal;
        private int expLevel;
        private int foodLevel;
        private int healthLevel;
        
        private int fishCaught;
        private int timesKicked;
        private int eggsThrown;
        private int foodEaten;
        private int arrowsShot;
        private int damageTaken;
        private int bedsEntered;
        private int portalsEntered;
        
        private int jumps;
        
        private int wordsSaid;
        private int commandsSent;
        
        private int curKillStreak = 0;
        private int maxKillStreak = 0;
        
        public MiscInfoPlayers(int playerId, Player player) {
            this.playerName = player.getPlayerListName();
            this.isOp = player.isOp();
            this.isBanned = player.isBanned();
            this.playerIp = player.getAddress().toString();
            
            this.gamemode = 0;
            this.expPercent = player.getExp();
            this.expTotal = player.getTotalExperience();
            this.expLevel = player.getLevel();
            this.foodLevel = player.getFoodLevel();
            this.healthLevel = player.getHealth();
            
            this.fishCaught = 0;
            this.timesKicked = 0;
            this.eggsThrown = 0;
            this.foodEaten = 0;
            this.arrowsShot = 0;
            this.damageTaken = 0;
            this.wordsSaid = 0;
            this.commandsSent = 0;
            
            this.curKillStreak = 0;
            this.maxKillStreak = 0;
            
            fetchData(playerId);
        }
        
        @Override
        public void fetchData(int playerId) {
            QueryResult result = Query.table(MiscInfoPlayersTable.TableName)
                .condition(MiscInfoPlayersTable.PlayerId, playerId)
                .select();
            if(result == null) {
                Query.table(MiscInfoPlayersTable.TableName)
                    .value(MiscInfoPlayersTable.PlayerId, playerId)
                    .value(MiscInfoPlayersTable.IsOp, isOp)
                    .value(MiscInfoPlayersTable.IsBanned, isBanned)
                    .value(MiscInfoPlayersTable.PlayerIp, playerIp)
                    .value(MiscInfoPlayersTable.ExperiencePercent, expPercent)
                    .value(MiscInfoPlayersTable.ExperienceTotal, expTotal)
                    .value(MiscInfoPlayersTable.ExperienceLevel, expLevel)
                    .value(MiscInfoPlayersTable.FoodLevel, foodLevel)
                    .value(MiscInfoPlayersTable.HealthLevel, healthLevel)
                    .value(MiscInfoPlayersTable.Gamemode, gamemode)
                    .value(MiscInfoPlayersTable.FishCaught, fishCaught)
                    .value(MiscInfoPlayersTable.TimesKicked, timesKicked)
                    .value(MiscInfoPlayersTable.EggsThrown, eggsThrown)
                    .value(MiscInfoPlayersTable.FoodEaten, foodEaten)
                    .value(MiscInfoPlayersTable.ArrowsShot, arrowsShot)
                    .value(MiscInfoPlayersTable.DamageTaken, damageTaken)
                    .value(MiscInfoPlayersTable.BedsEntered, bedsEntered)
                    .value(MiscInfoPlayersTable.PortalsEntered, portalsEntered)
                    .value(MiscInfoPlayersTable.TimesJumped, jumps)
                    .value(MiscInfoPlayersTable.WordsSaid, wordsSaid)
                    .value(MiscInfoPlayersTable.CommandsSent, commandsSent)
                    .value(MiscInfoPlayersTable.CurKillStreak, curKillStreak)
                    .value(MiscInfoPlayersTable.MaxKillStreak, maxKillStreak)
                    .insert();
            } else {
                fishCaught = result.asInt(MiscInfoPlayersTable.FishCaught);
                timesKicked = result.asInt(MiscInfoPlayersTable.TimesKicked);
                eggsThrown = result.asInt(MiscInfoPlayersTable.EggsThrown);
                foodEaten = result.asInt(MiscInfoPlayersTable.FoodEaten);
                arrowsShot = result.asInt(MiscInfoPlayersTable.ArrowsShot);
                damageTaken = result.asInt(MiscInfoPlayersTable.DamageTaken);
                bedsEntered = result.asInt(MiscInfoPlayersTable.BedsEntered);
                portalsEntered = result.asInt(MiscInfoPlayersTable.PortalsEntered);
                wordsSaid = result.asInt(MiscInfoPlayersTable.WordsSaid);
                commandsSent = result.asInt(MiscInfoPlayersTable.CommandsSent);
                maxKillStreak = result.asInt(MiscInfoPlayersTable.MaxKillStreak);
            }
        }

        @Override
        public boolean pushData(int playerId) {
            refreshPlayerData();
            boolean result = Query.table(MiscInfoPlayersTable.TableName)
                .value(MiscInfoPlayersTable.PlayerIp, playerIp)
                .value(MiscInfoPlayersTable.IsOp, isOp)
                .value(MiscInfoPlayersTable.IsBanned, isBanned)
                .value(MiscInfoPlayersTable.ExperiencePercent, expPercent)
                .value(MiscInfoPlayersTable.ExperienceTotal, expTotal)
                .value(MiscInfoPlayersTable.ExperienceLevel, expLevel)
                .value(MiscInfoPlayersTable.FoodLevel, foodLevel)
                .value(MiscInfoPlayersTable.HealthLevel, healthLevel)
                .value(MiscInfoPlayersTable.Gamemode, gamemode)
                .value(MiscInfoPlayersTable.FishCaught, fishCaught)
                .value(MiscInfoPlayersTable.TimesKicked, timesKicked)
                .value(MiscInfoPlayersTable.EggsThrown, eggsThrown)
                .value(MiscInfoPlayersTable.FoodEaten, foodEaten)
                .value(MiscInfoPlayersTable.ArrowsShot, arrowsShot)
                .value(MiscInfoPlayersTable.DamageTaken, damageTaken)
                .value(MiscInfoPlayersTable.BedsEntered, bedsEntered)
                .value(MiscInfoPlayersTable.PortalsEntered, portalsEntered)
                .value(MiscInfoPlayersTable.TimesJumped, jumps)
                .value(MiscInfoPlayersTable.WordsSaid, wordsSaid)
                .value(MiscInfoPlayersTable.CommandsSent, commandsSent)
                .value(MiscInfoPlayersTable.CurKillStreak, curKillStreak)
                .value(MiscInfoPlayersTable.MaxKillStreak, maxKillStreak)
                .condition(MiscInfoPlayersTable.PlayerId, playerId)
                .update();
            if(Settings.LocalConfiguration.Cloud.asBoolean()) fetchData(playerId);
            return result;
        }
        
        /**
         * Fetches the player data from the player, if he is online
         */
        public void refreshPlayerData() {
            Player player = null;
            for(Player pl : Bukkit.getServer().getOnlinePlayers()) {
                if(pl.getPlayerListName().equals(playerName)) player = pl;
            }
            if(player == null) return;
            
            this.isOp = player.isOp();
            this.isBanned = player.isBanned();
            
            this.gamemode = player.getGameMode().getValue();
            this.expPercent = player.getExp();
            this.expTotal = player.getTotalExperience();
            this.expLevel = player.getLevel();
            this.foodLevel = player.getFoodLevel();
            this.healthLevel = player.getHealth();
        }
        
        public void fishCaught() {
            fishCaught++;
        }
        
        public void kicked() {
            timesKicked++;
        }
        
        public void eggThrown() {
            eggsThrown++;
        }
        
        public void foodEaten() {
            foodEaten++;
        }
        
        public void arrowShot() {
            arrowsShot++;
        }
        
        public void jumped() {
            jumps++;
        }
        
        public void damageTaken(int damage) {
            damageTaken += damage;
        }
        
        public void bedEntered() {
            bedsEntered++;
        }
        
        public void portalEntered() {
            portalsEntered++;
        }
        
        public void chatMessageSent(int words) {
            wordsSaid += words;
        }
        
        public void commandSent() {
            commandsSent++;
        }
        
        /**
         * Logs player killing another player
         * @param player Player that was killed
         */
        public void playerKilled(Player player) {
            DataCollector.get(player).player().misc().died();
            curKillStreak++;
        }
        
        /**
         * Logs player being killed by mobs or natural causes
         */
        public void died() {
            if(maxKillStreak < curKillStreak) maxKillStreak = curKillStreak;
            curKillStreak = 0;
        }
    }
    
    public class InventoryData implements NormalData {
        
        public InventoryData(int playerId) {
            if(!Query.table(PlayersInv.TableName)
                    .column(PlayersInv.PlayerId)
                    .exists())
                Query.table(PlayersInv.TableName)
                    .value(PlayersInv.PlayerId, playerId)
                    .insert();
        }
        
        @Override
        public void fetchData(int playerId) { }

        @Override
        public boolean pushData(int playerId) {
            LocalSession session = DataCollector.get(playerId);
            if(session == null) return false;
            Player player = Bukkit.getPlayerExact(session.getName());
            if(player == null) return false;
            PlayerInventory inv = player.getInventory();
            List<ItemStack> invRow = new ArrayList<ItemStack>();
            
            for(int i = 9; i < 18; i++) { invRow.add(inv.getItem(i)); }
            String rowOne = SimpleInventoryItem.toJsonArray(invRow);
            invRow.clear();

            for(int i = 18; i < 27; i++) { invRow.add(inv.getItem(i)); }
            String rowTwo = SimpleInventoryItem.toJsonArray(invRow);
            invRow.clear();
            
            for(int i = 27; i < 36; i++) { invRow.add(inv.getItem(i)); }
            String rowThree = SimpleInventoryItem.toJsonArray(invRow);
            invRow.clear();

            for(int i = 36; i < 45; i++) { invRow.add(inv.getItem(i)); }
            String hotbar = SimpleInventoryItem.toJsonArray(invRow);
            invRow.clear();
            
            invRow.add(inv.getHelmet());
            invRow.add(inv.getChestplate());
            invRow.add(inv.getLeggings());
            invRow.add(inv.getBoots());
            String armor = SimpleInventoryItem.toJsonArray(invRow);
            
            String potionEffects = SimplePotionEffect.toJsonArray(player.getActivePotionEffects());
            
            Query.table(PlayersInv.TableName)
                .value(PlayersInv.Armor, armor)
                .value(PlayersInv.RowOne, rowOne)
                .value(PlayersInv.RowTwo, rowTwo)
                .value(PlayersInv.RowThree, rowThree)
                .value(PlayersInv.Hotbar, hotbar)
                .value(PlayersInv.PotionEffects, potionEffects)
                .condition(PlayersInv.PlayerId, playerId)
                .update();
            return false;
        }
    }
    
    public class DetailedLogPlayersEntry implements DetailedData {
        
        private long time;
        private boolean isLogin;
        private Location location;
         
        public DetailedLogPlayersEntry(Location location, boolean isLogin) {
            this.time = Util.getTimestamp();
            this.isLogin = isLogin;
            this.location = location;
        }
         
        @Override
        public boolean pushData(int playerId) {
            return Query.table(LogPlayers.TableName)
                    .value(LogPlayers.PlayerId, playerId)
                    .value(LogPlayers.Timestamp, time)
                    .value(LogPlayers.IsLogin, isLogin)
                    .value(LogPlayers.World, location.getWorld().getName())
                    .value(LogPlayers.XCoord, location.getBlockX())
                    .value(LogPlayers.YCoord, location.getBlockY())
                    .value(LogPlayers.ZCoord, location.getBlockZ())
                    .insert();
        }
     
    }
}
