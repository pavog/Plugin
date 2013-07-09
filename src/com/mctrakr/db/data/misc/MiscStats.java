/*
 * MiscStats.java
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

package com.mctrakr.db.data.misc;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.mctrakr.db.DBTable;
import com.mctrakr.db.Query;
import com.mctrakr.db.Query.QueryResult;
import com.mctrakr.db.data.NormalData;
import com.mctrakr.db.data.misc.Tables.MiscInfoTable;
import com.mctrakr.util.Util;
import com.mctrakr.util.cache.SessionCache;

/**
 * Represents all the miscellaneous information that does not fit any other category
 * @author bitWolfy
 *
 */
public class MiscStats extends NormalData {

    private final String playerName;
    private Map<DBTable, Object> values;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new MiscInfoPlayers object based on arguments provided
     * @param playerId Player ID
     * @param player Player object
     */
    public MiscStats(int playerId, Player player) {
        playerName = player.getName();
        
        values = new HashMap<DBTable, Object>();
        
        if(player.isOp()) values.put(MiscInfoTable.IsOp, 1);
        else values.put(MiscInfoTable.IsOp, 0);
        if(player.isBanned()) values.put(MiscInfoTable.IsBanned, 1);
        else values.put(MiscInfoTable.IsBanned, 0);
        
        InetAddress playerIp = player.getAddress().getAddress();
        if(playerIp == null) values.put(MiscInfoTable.PlayerIp, "192.168.0.1");
        else values.put(MiscInfoTable.PlayerIp, playerIp.getHostAddress());
        
        values.put(MiscInfoTable.Gamemode, player.getGameMode().getValue());
        values.put(MiscInfoTable.ExpPercent, player.getExp());
        values.put(MiscInfoTable.ExpTotal, player.getTotalExperience());
        values.put(MiscInfoTable.ExpLevel, player.getLevel());
        values.put(MiscInfoTable.FoodLevel, player.getFoodLevel());
        values.put(MiscInfoTable.HealthLevel, player.getHealth());
        values.put(MiscInfoTable.ArmorLevel, Util.getArmorRating(player.getInventory()));
        
        values.put(MiscInfoTable.FishCaught, 0);
        values.put(MiscInfoTable.TimesKicked, 0);
        values.put(MiscInfoTable.EggsThrown, 0);
        values.put(MiscInfoTable.FoodEaten, 0);
        values.put(MiscInfoTable.ArrowsShot, 0);
        values.put(MiscInfoTable.DamageTaken, 0);
        values.put(MiscInfoTable.BedsEntered, 0);
        values.put(MiscInfoTable.PortalsEntered, 0);
        values.put(MiscInfoTable.WordsSaid, 0);
        values.put(MiscInfoTable.CommandsSent, 0);
        values.put(MiscInfoTable.TimesJumped, 0);
        
        values.put(MiscInfoTable.CurKillStreak, 0);
        values.put(MiscInfoTable.MaxKillStreak, 0);
        
        fetchData(playerId);
    }
    
    @Override
    public void fetchData(int playerId) {
        QueryResult result = Query.table(MiscInfoTable.TableName)
            .condition(MiscInfoTable.PlayerId, playerId)
            .select();
        if(result == null) {
            Query.table(MiscInfoTable.TableName)
                .value(MiscInfoTable.PlayerId, playerId)
                .valueRaw(values)
                .insert();
        } else {
            values.put(MiscInfoTable.FishCaught, result.asInt(MiscInfoTable.FishCaught));
            values.put(MiscInfoTable.TimesKicked, result.asInt(MiscInfoTable.TimesKicked));
            values.put(MiscInfoTable.EggsThrown, result.asInt(MiscInfoTable.EggsThrown));
            values.put(MiscInfoTable.FoodEaten, result.asInt(MiscInfoTable.FoodEaten));
            values.put(MiscInfoTable.ArrowsShot, result.asInt(MiscInfoTable.ArrowsShot));
            values.put(MiscInfoTable.DamageTaken, result.asDouble(MiscInfoTable.DamageTaken));
            values.put(MiscInfoTable.BedsEntered, result.asInt(MiscInfoTable.BedsEntered));
            values.put(MiscInfoTable.PortalsEntered, result.asInt(MiscInfoTable.PortalsEntered));
            values.put(MiscInfoTable.WordsSaid, result.asInt(MiscInfoTable.WordsSaid));
            values.put(MiscInfoTable.CommandsSent, result.asInt(MiscInfoTable.CommandsSent));
            values.put(MiscInfoTable.MaxKillStreak, result.asInt(MiscInfoTable.MaxKillStreak));
            values.put(MiscInfoTable.TimesJumped, result.asInt(MiscInfoTable.TimesJumped));
        }
    }

    @Override
    public boolean pushData(int playerId) {
        refreshPlayerData();
        boolean result = Query.table(MiscInfoTable.TableName)
            .valueRaw(values)
            .condition(MiscInfoTable.PlayerId, playerId)
            .update();
        fetchData(playerId);
        return result;
    }
    
    public void clearData(int playerId) {
        values.put(MiscInfoTable.FishCaught, 0);
        values.put(MiscInfoTable.TimesKicked, 0);
        values.put(MiscInfoTable.EggsThrown, 0);
        values.put(MiscInfoTable.FoodEaten, 0);
        values.put(MiscInfoTable.ArrowsShot, 0);
        values.put(MiscInfoTable.DamageTaken, 0);
        values.put(MiscInfoTable.BedsEntered, 0);
        values.put(MiscInfoTable.PortalsEntered, 0);
        values.put(MiscInfoTable.WordsSaid, 0);
        values.put(MiscInfoTable.CommandsSent, 0);
        values.put(MiscInfoTable.TimesJumped, 0);
        
        values.put(MiscInfoTable.CurKillStreak, 0);
        values.put(MiscInfoTable.MaxKillStreak, 0);
    }
    
    /**
     * Fetches the player data from the player, if he is online
     */
    public void refreshPlayerData() {
        Player player = Bukkit.getServer().getPlayerExact(playerName);
        if(player == null) return;

        if(player.isOp()) values.put(MiscInfoTable.IsOp, 1);
        else values.put(MiscInfoTable.IsOp, 0);
        if(player.isBanned()) values.put(MiscInfoTable.IsBanned, 1);
        else values.put(MiscInfoTable.IsBanned, 0);
        values.put(MiscInfoTable.PlayerIp, player.getAddress().getAddress().getHostAddress());
        
        values.put(MiscInfoTable.Gamemode, player.getGameMode().getValue());
        values.put(MiscInfoTable.ExpPercent, player.getExp());
        values.put(MiscInfoTable.ExpTotal, player.getTotalExperience());
        values.put(MiscInfoTable.ExpLevel, player.getLevel());
        values.put(MiscInfoTable.FoodLevel, player.getFoodLevel());
        values.put(MiscInfoTable.HealthLevel, player.getHealth());
        values.put(MiscInfoTable.ArmorLevel, Util.getArmorRating(player.getInventory()));
    }
    
    /**
     * Increments the specified miscellaneous statistic by 1
     * @param type Statistic type
     */
    public void incrementStat(MiscInfoTable type) {
        double value = 1;
        if(values.containsKey(type)) {
            Object valueObj = values.get(type);
            if(valueObj instanceof Double)
                value = ((Double) valueObj).doubleValue() + 1;
            else value = ((Integer) valueObj).doubleValue() + 1;
        }
        values.put(type, value);
    }
    
    /**
     * Increments the miscellaneous statistic by the specified amount
     * @param type Statistic type
     * @param value Amount
     */
    public void incrementStat(MiscInfoTable type, double value) {
        if(values.containsKey(type)) {
            Object valueObj = values.get(type);
            if(valueObj instanceof Double)
                value += ((Double) valueObj).doubleValue();
            else value += ((Integer) valueObj).doubleValue();
        }
        values.put(type, value);
    }
    
    /**
     * Logs player killing another player
     * @param player Player that was killed
     */
    public void killed(Player player) {
        SessionCache.fetch(player).died();
        int curKillStreak = ((Integer) values.get(MiscInfoTable.CurKillStreak)).intValue() + 1;
        int maxKillStreak = ((Integer) values.get(MiscInfoTable.MaxKillStreak)).intValue();
        values.put(MiscInfoTable.CurKillStreak, curKillStreak);
        if(curKillStreak > maxKillStreak) {
            maxKillStreak++;
            values.put(MiscInfoTable.MaxKillStreak, maxKillStreak);
        }
    }
    
    /**
     * Logs player being killed by mobs or natural causes
     */
    public void died() {
        int curKillStreak = ((Integer) values.get(MiscInfoTable.CurKillStreak)).intValue();
        int maxKillStreak = ((Integer) values.get(MiscInfoTable.MaxKillStreak)).intValue();
        if(curKillStreak > maxKillStreak) {
            maxKillStreak++;
            values.put(MiscInfoTable.MaxKillStreak, maxKillStreak);
        }
        values.put(MiscInfoTable.CurKillStreak, 0);
    }
}
