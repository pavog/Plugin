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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.Settings;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.data.sync.DataStore.DetailedData;
import com.wolvencraft.yasp.db.data.sync.DataStore.NormalData;
import com.wolvencraft.yasp.db.tables.DBTable;
import com.wolvencraft.yasp.db.tables.Detailed.LogPlayers;
import com.wolvencraft.yasp.db.tables.Normal.DistancePlayersTable;
import com.wolvencraft.yasp.db.tables.Normal.MiscInfoPlayersTable;
import com.wolvencraft.yasp.db.tables.Normal.PlayersInv;
import com.wolvencraft.yasp.db.tables.Normal.PlayersTable;
import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.util.SimpleInventoryItem;
import com.wolvencraft.yasp.util.SimplePotionEffect;
import com.wolvencraft.yasp.util.Util;

/**
 * A unique data store that contains basic information about the player
 * @author bitWolfy
 *
 */
public class PlayersData {
    
    private int playerId;
    private Players generalData;
    private DistancePlayers distanceData;
    private MiscInfoPlayers miscData;
    private InventoryData inventoryData;
    
    private List<DetailedData> detailedData;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new PlayersData object based on the data provided
     * @param player Player object
     * @param playerId Player ID
     */
    public PlayersData(Player player, int playerId) {
        this.playerId = playerId;
        generalData = new Players(playerId, player);
        distanceData = new DistancePlayers(playerId);
        miscData = new MiscInfoPlayers(playerId, player);
        inventoryData = new InventoryData(playerId);
        
        detailedData = new ArrayList<DetailedData>();
    }
    
    /**
     * Returns a static copy of DetailedData to prevent ConcurrentModificationException occurrences
     * @return List of DetailedData objects
     */
    private List<DetailedData> getDetailedData() {
        List<DetailedData> temp = new ArrayList<DetailedData>();
        for(DetailedData value : detailedData) temp.add(value);
        return temp;
    }
    
    /**
     * Pushes the data to the database
     */
    public void sync() {
        generalData.pushData(playerId);
        distanceData.pushData(playerId);
        miscData.pushData(playerId);
        inventoryData.pushData(playerId);
        
        for(DetailedData entry : getDetailedData()) {
            if(entry.pushData(playerId)) { detailedData.remove(entry); }
        }
    }
    
    /**
     * Erases all locally stored data
     */
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
    public Players getGeneralData() {
        return generalData;
    }
    
    /**
     * Returns the information from the Distances table.
     * @return Distances data store
     */
    public DistancePlayers getDistanceData() {
        return distanceData;
    }
    
    /**
     * Returns the information from the Miscellaneous table.<br />
     * This information is likely to change rapidly.
     * @return Miscellaneous data store
     */
    public MiscInfoPlayers getMiscData() {
        return miscData;
    }
    
    /**
     * Logs player's login/logout location
     * @param location Location of the login
     * @param isLogin <b>true</b> if the player has logged in, <b>false</b> otherwise
     */
    public void addPlayerLog(Location location, boolean isLogin) {
        detailedData.add(new DetailedLogPlayersEntry(location, isLogin));
    }
    
    /**
     * Represents the Player data that is being tracked.<br />
     * Each entry must have a unique player name.
     * @author bitWolfy
     *
     */
    public class Players implements NormalData {

        private long lastSync;
        
        private long sessionStart;
        private long totalPlaytime;
        private long firstJoin;
        private int logins;
        
        public Players (int playerId, Player player) {
            this.lastSync = Util.getTimestamp();
            
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
            
            firstJoin = result.asLong(PlayersTable.FirstLogin);
            if(firstJoin == -1) { firstJoin = Util.getTimestamp(); }
            logins = result.asInt(PlayersTable.Logins);
            totalPlaytime = result.asLong(PlayersTable.TotalPlaytime);
        }

        @Override
        public boolean pushData(int playerId) {
            boolean result = Query.table(PlayersTable.TableName)
                .value(PlayersTable.SessionStart, sessionStart)
                .value(PlayersTable.FirstLogin, firstJoin)
                .value(PlayersTable.Logins, logins)
                .value(PlayersTable.TotalPlaytime, totalPlaytime)
                .condition(PlayersTable.PlayerId, playerId)
                .update();
            return result;
        }

        /**
         * Returns the total playtime for the player
         * @return Total playtime
         */
        public long getPlaytime() {
            return generalData.totalPlaytime;
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
            } else {
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
            return result;
        }
        
        /**
         * Increments the distance of the specified type by the amount
         * @param type Travel type
         * @param distance Distance travelled
         */
        public void addDistance(DistancePlayersTable type, double distance) {
            switch(type) {
                case Swimmed:
                    swim += distance;
                    break;
                case Flight:
                    flight  += distance;
                    break;
                case Boat:
                    boat += distance;
                    break;
                case Minecart:
                    minecart += distance;
                    break;
                case Pig:
                    pig += distance;
                    break;
                default:
                    foot += distance;
            }
        }
    }
    
    /**
     * Represents all the miscellaneous information that does not fit any other category
     * @author bitWolfy
     *
     */
    public class MiscInfoPlayers implements NormalData {
        
        private Map<DBTable, Object> values;
        private String playerName;
        
        /**
         * <b>Default constructor</b><br />
         * Creates a new MiscInfoPlayers object based on arguments provided
         * @param playerId Player ID
         * @param player Player object
         */
        public MiscInfoPlayers(int playerId, Player player) {
            this.playerName = player.getPlayerListName();
            
            this.values = new HashMap<DBTable, Object>();
            if(player.isOp()) values.put(MiscInfoPlayersTable.IsOp, 1);
            else values.put(MiscInfoPlayersTable.IsOp, 0);
            if(player.isBanned()) values.put(MiscInfoPlayersTable.IsBanned, 1);
            else values.put(MiscInfoPlayersTable.IsBanned, 0);
            values.put(MiscInfoPlayersTable.PlayerIp, player.getAddress().toString());
            
            values.put(MiscInfoPlayersTable.Gamemode, player.getGameMode().getValue());
            values.put(MiscInfoPlayersTable.ExperiencePercent, player.getExp());
            values.put(MiscInfoPlayersTable.ExperienceTotal, player.getTotalExperience());
            values.put(MiscInfoPlayersTable.ExperienceLevel, player.getLevel());
            values.put(MiscInfoPlayersTable.FoodLevel, player.getFoodLevel());
            values.put(MiscInfoPlayersTable.HealthLevel, player.getHealth());
            
            values.put(MiscInfoPlayersTable.FishCaught, 0);
            values.put(MiscInfoPlayersTable.TimesKicked, 0);
            values.put(MiscInfoPlayersTable.EggsThrown, 0);
            values.put(MiscInfoPlayersTable.FoodEaten, 0);
            values.put(MiscInfoPlayersTable.ArrowsShot, 0);
            values.put(MiscInfoPlayersTable.DamageTaken, 0);
            values.put(MiscInfoPlayersTable.WordsSaid, 0);
            values.put(MiscInfoPlayersTable.CommandsSent, 0);
            values.put(MiscInfoPlayersTable.TimesJumped, 0);
            
            values.put(MiscInfoPlayersTable.CurKillStreak, 0);
            values.put(MiscInfoPlayersTable.MaxKillStreak, 0);
            
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
                    .valueRaw(values)
                    .insert();
            } else {
                values.put(MiscInfoPlayersTable.FishCaught, result.asInt(MiscInfoPlayersTable.FishCaught));
                values.put(MiscInfoPlayersTable.TimesKicked, result.asInt(MiscInfoPlayersTable.TimesKicked));
                values.put(MiscInfoPlayersTable.EggsThrown, result.asInt(MiscInfoPlayersTable.EggsThrown));
                values.put(MiscInfoPlayersTable.FoodEaten, result.asInt(MiscInfoPlayersTable.FoodEaten));
                values.put(MiscInfoPlayersTable.ArrowsShot, result.asInt(MiscInfoPlayersTable.ArrowsShot));
                values.put(MiscInfoPlayersTable.DamageTaken, result.asInt(MiscInfoPlayersTable.DamageTaken));
                values.put(MiscInfoPlayersTable.BedsEntered, result.asInt(MiscInfoPlayersTable.BedsEntered));
                values.put(MiscInfoPlayersTable.PortalsEntered, result.asInt(MiscInfoPlayersTable.PortalsEntered));
                values.put(MiscInfoPlayersTable.WordsSaid, result.asInt(MiscInfoPlayersTable.WordsSaid));
                values.put(MiscInfoPlayersTable.CommandsSent, result.asInt(MiscInfoPlayersTable.CommandsSent));
                values.put(MiscInfoPlayersTable.MaxKillStreak, result.asInt(MiscInfoPlayersTable.MaxKillStreak));
                values.put(MiscInfoPlayersTable.TimesJumped, result.asInt(MiscInfoPlayersTable.TimesJumped));
            }
        }

        @Override
        public boolean pushData(int playerId) {
            refreshPlayerData();
            boolean result = Query.table(MiscInfoPlayersTable.TableName)
                .valueRaw(values)
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

            if(player.isOp()) values.put(MiscInfoPlayersTable.IsOp, 1);
            else values.put(MiscInfoPlayersTable.IsOp, 0);
            if(player.isBanned()) values.put(MiscInfoPlayersTable.IsBanned, 1);
            else values.put(MiscInfoPlayersTable.IsBanned, 0);
            values.put(MiscInfoPlayersTable.PlayerIp, player.getAddress().toString());
            
            values.put(MiscInfoPlayersTable.Gamemode, player.getGameMode().getValue());
            values.put(MiscInfoPlayersTable.ExperiencePercent, player.getExp());
            values.put(MiscInfoPlayersTable.ExperienceTotal, player.getTotalExperience());
            values.put(MiscInfoPlayersTable.ExperienceLevel, player.getLevel());
            values.put(MiscInfoPlayersTable.FoodLevel, player.getFoodLevel());
            values.put(MiscInfoPlayersTable.HealthLevel, player.getHealth());
        }
        
        /**
         * Increments the specified miscellaneous statistic by 1
         * @param type Statistic type
         */
        public void incrementStat(MiscInfoPlayersTable type) {
            int value = ((Integer) values.get(type)).intValue() + 1;
            values.put(type, value);
        }
        
        /**
         * Increments the miscellaneous statistic by the specified amount
         * @param type Statistic type
         * @param value Amount
         */
        public void incrementStat(MiscInfoPlayersTable type, int value) {
            value += ((Integer) values.get(type)).intValue();
            values.put(type, value);
        }
        
        /**
         * Logs player killing another player
         * @param player Player that was killed
         */
        public void killed(Player player) {
            DataCollector.get(player).died();
            int curKillStreak = ((Integer) values.get(MiscInfoPlayersTable.CurKillStreak)).intValue() + 1;
            int maxKillStreak = ((Integer) values.get(MiscInfoPlayersTable.MaxKillStreak)).intValue();
            values.put(MiscInfoPlayersTable.CurKillStreak, curKillStreak);
            if(curKillStreak > maxKillStreak) {
                maxKillStreak++;
                values.put(MiscInfoPlayersTable.MaxKillStreak, maxKillStreak);
            }
        }
        
        /**
         * Logs player being killed by mobs or natural causes
         */
        public void died() {
            int curKillStreak = ((Integer) values.get(MiscInfoPlayersTable.CurKillStreak)).intValue();
            int maxKillStreak = ((Integer) values.get(MiscInfoPlayersTable.MaxKillStreak)).intValue();
            if(curKillStreak > maxKillStreak) {
                maxKillStreak++;
                values.put(MiscInfoPlayersTable.MaxKillStreak, maxKillStreak);
            }
            values.put(MiscInfoPlayersTable.CurKillStreak, 0);
        }
    }
    
    /**
     * Represents player inventory and potion effects
     * @author bitWolfy
     *
     */
    public class InventoryData implements NormalData {
        
        /**
         * <b>Default constructor</b><br />
         * Creates a new InventoryData object based on arguments provided
         * @param playerId Player ID
         */
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

        @SuppressWarnings("deprecation")
        @Override
        public boolean pushData(int playerId) {
            OnlineSession session = DataCollector.get(playerId);
            if(session == null) return false;
            Player player = Bukkit.getPlayerExact(session.getName());
            if(player == null) return false;
            player.updateInventory();
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

            for(int i = 0; i < 9; i++) { invRow.add(inv.getItem(i)); }
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
    
    /**
     * Tracks player's login and logout locations
     * @author bitWolfy
     *
     */
    public class DetailedLogPlayersEntry implements DetailedData {
        
        private long time;
        private boolean isLogin;
        private Location location;
         
        /**
         * <b>Default constructor</b><br />
         * Creates a new DetailedLogPlayersEntry object based on arguments provided
         * @param location Location of the event
         * @param isLogin <b>true</b> if the player has logged in, <b>false</b> if he logged off
         */
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
