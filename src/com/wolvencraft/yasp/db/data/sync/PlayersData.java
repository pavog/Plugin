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
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.Settings;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.tables.Detailed.LogPlayers;
import com.wolvencraft.yasp.db.tables.Normal.DistancePlayersTable;
import com.wolvencraft.yasp.db.tables.Normal.MiscInfoPlayersTable;
import com.wolvencraft.yasp.db.tables.Normal.PlayersTable;
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
    private List<DetailedData> detailedData;
    
    public PlayersData(Player player, int playerId) {
        this.playerId = playerId;
        generalData = new Players(playerId, player);
        distanceData = new DistancePlayers(playerId);
        miscData = new MiscInfoPlayers(playerId, player);
        
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
        Query.table(PlayersTable.TableName.toString())
            .value(PlayersTable.Online.toString(), true)
            .condition(PlayersTable.PlayerId.toString(), playerId)
            .update();
    }
    
    /**
     * Registers player logging out with all corresponding statistics trackers.<br />
     * Player's online status is updated in the database instantly.
     */
    public void logout(Location location) {
        generalData.setOnline(false);
        detailedData.add(new DetailedLogPlayersEntry(location, false));
        Query.table(PlayersTable.TableName.toString())
            .value(PlayersTable.Online.toString(), false)
            .condition(PlayersTable.PlayerId.toString(), playerId)
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
            QueryResult result = Query.table(PlayersTable.TableName.toString())
                .column(PlayersTable.Logins.toString())
                .column(PlayersTable.FirstLogin.toString())
                .column(PlayersTable.TotalPlaytime.toString())
                .condition(PlayersTable.PlayerId.toString(), playerId)
                .select();
            if(result == null) {
                Query.table(PlayersTable.TableName.toString())
                    .value(PlayersTable.PlayerId.toString(), playerId)
                    .value(PlayersTable.Name.toString(), playerName)
                    .value(PlayersTable.Online.toString(), online)
                    .value(PlayersTable.SessionStart.toString(), sessionStart)
                    .value(PlayersTable.FirstLogin.toString(), firstJoin)
                    .value(PlayersTable.Logins.toString(), logins)
                    .value(PlayersTable.TotalPlaytime.toString(), totalPlaytime)
                    .insert();
            } else {
                firstJoin = result.getValueAsLong(PlayersTable.FirstLogin.toString());
                if(firstJoin == -1) firstJoin = Util.getTimestamp();
                logins = result.getValueAsInteger(PlayersTable.Logins.toString());
                totalPlaytime = result.getValueAsLong(PlayersTable.TotalPlaytime.toString());
            }
        }

        @Override
        public boolean pushData(int playerId) {
            online = Bukkit.getServer().getPlayer(playerName) != null;
            if(online) {
                totalPlaytime += (lastSync - sessionStart);
                lastSync = Util.getTimestamp();
            }
            boolean result = Query.table(PlayersTable.TableName.toString())
                .value(PlayersTable.Name.toString(), playerName)
                .value(PlayersTable.Online.toString(), online)
                .value(PlayersTable.SessionStart.toString(), sessionStart)
                .value(PlayersTable.FirstLogin.toString(), firstJoin)
                .value(PlayersTable.Logins.toString(), logins)
                .value(PlayersTable.TotalPlaytime.toString(), totalPlaytime)
                .condition(PlayersTable.PlayerId.toString(), playerId)
                .update(true);
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
            QueryResult result = Query.table(DistancePlayersTable.TableName.toString())
                    .column(DistancePlayersTable.Foot.toString())
                    .column(DistancePlayersTable.Swimmed.toString())
                    .column(DistancePlayersTable.Flight.toString())
                    .column(DistancePlayersTable.Boat.toString())
                    .column(DistancePlayersTable.Minecart.toString())
                    .column(DistancePlayersTable.Pig.toString())
                    .condition(DistancePlayersTable.PlayerId.toString(), playerId)
                    .select();
            if(result == null) {
                Query.table(DistancePlayersTable.TableName.toString())
                    .value(DistancePlayersTable.PlayerId.toString(), playerId)
                    .value(DistancePlayersTable.Foot.toString(), foot)
                    .value(DistancePlayersTable.Swimmed.toString(), swim)
                    .value(DistancePlayersTable.Flight.toString(), flight)
                    .value(DistancePlayersTable.Boat.toString(), boat)
                    .value(DistancePlayersTable.Minecart.toString(), minecart)
                    .value(DistancePlayersTable.Pig.toString(), pig)
                    .insert();
            }
            else {
                foot = result.getValueAsInteger(DistancePlayersTable.Foot.toString());
                swim = result.getValueAsInteger(DistancePlayersTable.Swimmed.toString());
                flight = result.getValueAsInteger(DistancePlayersTable.Flight.toString());
                boat = result.getValueAsInteger(DistancePlayersTable.Boat.toString());
                minecart = result.getValueAsInteger(DistancePlayersTable.Minecart.toString());
                pig = result.getValueAsInteger(DistancePlayersTable.Pig.toString());
            }
        }

        @Override
        public boolean pushData(int playerId) {
            boolean result = Query.table(DistancePlayersTable.TableName.toString())
                .value(DistancePlayersTable.Foot.toString(), foot)
                .value(DistancePlayersTable.Swimmed.toString(), swim)
                .value(DistancePlayersTable.Flight.toString(), flight)
                .value(DistancePlayersTable.Boat.toString(), boat)
                .value(DistancePlayersTable.Minecart.toString(), minecart)
                .value(DistancePlayersTable.Pig.toString(), pig)
                .condition(DistancePlayersTable.PlayerId.toString(), playerId)
                .update(true);
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
        
        private Collection<PotionEffect> potionEffects;
        
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
            this.playerIp = player.getAddress().toString();
            
            this.potionEffects = player.getActivePotionEffects();
            
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
            QueryResult result = Query.table(MiscInfoPlayersTable.TableName.toString())
                .condition(MiscInfoPlayersTable.PlayerId.toString(), playerId)
                .select();
            if(result == null) {
                Query.table(MiscInfoPlayersTable.TableName.toString())
                    .value(MiscInfoPlayersTable.PlayerId.toString(), playerId)
                    .value(MiscInfoPlayersTable.PlayerIp.toString(), playerIp)
                    .value(MiscInfoPlayersTable.ExperiencePercent.toString(), expPercent)
                    .value(MiscInfoPlayersTable.ExperienceTotal.toString(), expTotal)
                    .value(MiscInfoPlayersTable.ExperienceLevel.toString(), expLevel)
                    .value(MiscInfoPlayersTable.FoodLevel.toString(), foodLevel)
                    .value(MiscInfoPlayersTable.HealthLevel.toString(), healthLevel)
                    .value(MiscInfoPlayersTable.Gamemode.toString(), gamemode)
                    .value(MiscInfoPlayersTable.FishCaught.toString(), fishCaught)
                    .value(MiscInfoPlayersTable.TimesKicked.toString(), timesKicked)
                    .value(MiscInfoPlayersTable.EggsThrown.toString(), eggsThrown)
                    .value(MiscInfoPlayersTable.FoodEaten.toString(), foodEaten)
                    .value(MiscInfoPlayersTable.ArrowsShot.toString(), arrowsShot)
                    .value(MiscInfoPlayersTable.DamageTaken.toString(), damageTaken)
                    .value(MiscInfoPlayersTable.BedsEntered.toString(), bedsEntered)
                    .value(MiscInfoPlayersTable.PortalsEntered.toString(), portalsEntered)
                    .value(MiscInfoPlayersTable.TimesJumped.toString(), jumps)
                    .value(MiscInfoPlayersTable.WordsSaid.toString(), wordsSaid)
                    .value(MiscInfoPlayersTable.CommandsSent.toString(), commandsSent)
                    .value(MiscInfoPlayersTable.CurKillStreak.toString(), curKillStreak)
                    .value(MiscInfoPlayersTable.MaxKillStreak.toString(), maxKillStreak)
                    .value(MiscInfoPlayersTable.PotionEffects.toString(), SimplePotionEffect.toGsonArray(potionEffects))
                    .insert();
            } else {
                fishCaught = result.getValueAsInteger(MiscInfoPlayersTable.FishCaught.toString());
                timesKicked = result.getValueAsInteger(MiscInfoPlayersTable.TimesKicked.toString());
                eggsThrown = result.getValueAsInteger(MiscInfoPlayersTable.EggsThrown.toString());
                foodEaten = result.getValueAsInteger(MiscInfoPlayersTable.FoodEaten.toString());
                arrowsShot = result.getValueAsInteger(MiscInfoPlayersTable.ArrowsShot.toString());
                damageTaken = result.getValueAsInteger(MiscInfoPlayersTable.DamageTaken.toString());
                bedsEntered = result.getValueAsInteger(MiscInfoPlayersTable.BedsEntered.toString());
                portalsEntered = result.getValueAsInteger(MiscInfoPlayersTable.PortalsEntered.toString());
                wordsSaid = result.getValueAsInteger(MiscInfoPlayersTable.WordsSaid.toString());
                commandsSent = result.getValueAsInteger(MiscInfoPlayersTable.CommandsSent.toString());
                maxKillStreak = result.getValueAsInteger(MiscInfoPlayersTable.MaxKillStreak.toString());
            }
        }

        @Override
        public boolean pushData(int playerId) {
            refreshPlayerData();
            boolean result = Query.table(MiscInfoPlayersTable.TableName.toString())
                .value(MiscInfoPlayersTable.PlayerIp.toString(), playerIp)
                .value(MiscInfoPlayersTable.ExperiencePercent.toString(), expPercent)
                .value(MiscInfoPlayersTable.ExperienceTotal.toString(), expTotal)
                .value(MiscInfoPlayersTable.ExperienceLevel.toString(), expLevel)
                .value(MiscInfoPlayersTable.FoodLevel.toString(), foodLevel)
                .value(MiscInfoPlayersTable.HealthLevel.toString(), healthLevel)
                .value(MiscInfoPlayersTable.Gamemode.toString(), gamemode)
                .value(MiscInfoPlayersTable.FishCaught.toString(), fishCaught)
                .value(MiscInfoPlayersTable.TimesKicked.toString(), timesKicked)
                .value(MiscInfoPlayersTable.EggsThrown.toString(), eggsThrown)
                .value(MiscInfoPlayersTable.FoodEaten.toString(), foodEaten)
                .value(MiscInfoPlayersTable.ArrowsShot.toString(), arrowsShot)
                .value(MiscInfoPlayersTable.DamageTaken.toString(), damageTaken)
                .value(MiscInfoPlayersTable.BedsEntered.toString(), bedsEntered)
                .value(MiscInfoPlayersTable.PortalsEntered.toString(), portalsEntered)
                .value(MiscInfoPlayersTable.TimesJumped.toString(), jumps)
                .value(MiscInfoPlayersTable.WordsSaid.toString(), wordsSaid)
                .value(MiscInfoPlayersTable.CommandsSent.toString(), commandsSent)
                .value(MiscInfoPlayersTable.CurKillStreak.toString(), curKillStreak)
                .value(MiscInfoPlayersTable.MaxKillStreak.toString(), maxKillStreak)
                .value(MiscInfoPlayersTable.PotionEffects.toString(), SimplePotionEffect.toGsonArray(potionEffects))
                .condition(MiscInfoPlayersTable.PlayerId.toString(), playerId)
                .update(true);
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
            
            this.potionEffects = player.getActivePotionEffects();
            
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
            return Query.table(LogPlayers.TableName.toString())
                    .value(LogPlayers.PlayerId.toString(), playerId)
                    .value(LogPlayers.Timestamp.toString(), time)
                    .value(LogPlayers.IsLogin.toString(), isLogin)
                    .value(LogPlayers.World.toString(), location.getWorld().getName())
                    .value(LogPlayers.XCoord.toString(), location.getBlockX())
                    .value(LogPlayers.YCoord.toString(), location.getBlockY())
                    .value(LogPlayers.ZCoord.toString(), location.getBlockZ())
                    .insert();
        }
     
    }
}
