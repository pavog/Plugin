/*
 * MiscInfoPlayerEntry.java
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

package com.wolvencraft.yasp.db.data.players;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.data.NormalData;
import com.wolvencraft.yasp.db.tables.DBTable;
import com.wolvencraft.yasp.db.tables.Normal.MiscInfoPlayersTable;
import com.wolvencraft.yasp.settings.RemoteConfiguration;
import com.wolvencraft.yasp.util.Util;
import com.wolvencraft.yasp.util.cache.OnlineSessionCache;

/**
 * Represents all the miscellaneous information that does not fit any other category
 * @author bitWolfy
 *
 */
public class MiscInfoPlayerEntry extends NormalData {
    
    private Map<DBTable, Object> values;
    private String playerName;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new MiscInfoPlayers object based on arguments provided
     * @param playerId Player ID
     * @param player Player object
     */
    public MiscInfoPlayerEntry(int playerId, Player player) {
        playerName = player.getPlayerListName();
        
        values = new HashMap<DBTable, Object>();
        
        if(player.isOp()) values.put(MiscInfoPlayersTable.IsOp, 1);
        else values.put(MiscInfoPlayersTable.IsOp, 0);
        if(player.isBanned()) values.put(MiscInfoPlayersTable.IsBanned, 1);
        else values.put(MiscInfoPlayersTable.IsBanned, 0);
        values.put(MiscInfoPlayersTable.PlayerIp, player.getAddress().getAddress().getHostAddress());
        
        values.put(MiscInfoPlayersTable.Gamemode, player.getGameMode().getValue());
        values.put(MiscInfoPlayersTable.ExpPercent, player.getExp());
        values.put(MiscInfoPlayersTable.ExpTotal, player.getTotalExperience());
        values.put(MiscInfoPlayersTable.ExpLevel, player.getLevel());
        values.put(MiscInfoPlayersTable.FoodLevel, player.getFoodLevel());
        values.put(MiscInfoPlayersTable.HealthLevel, player.getHealth());
        values.put(MiscInfoPlayersTable.ArmorLevel, Util.getArmorRating(player.getInventory()));
        
        values.put(MiscInfoPlayersTable.FishCaught, 0);
        values.put(MiscInfoPlayersTable.TimesKicked, 0);
        values.put(MiscInfoPlayersTable.EggsThrown, 0);
        values.put(MiscInfoPlayersTable.FoodEaten, 0);
        values.put(MiscInfoPlayersTable.ArrowsShot, 0);
        values.put(MiscInfoPlayersTable.DamageTaken, 0);
        values.put(MiscInfoPlayersTable.BedsEntered, 0);
        values.put(MiscInfoPlayersTable.PortalsEntered, 0);
        values.put(MiscInfoPlayersTable.WordsSaid, 0);
        values.put(MiscInfoPlayersTable.CommandsSent, 0);
        values.put(MiscInfoPlayersTable.TimesJumped, 0);
        
        values.put(MiscInfoPlayersTable.CurKillStreak, 0);
        values.put(MiscInfoPlayersTable.MaxKillStreak, 0);
        
        fetchData(playerId);
    }
    
    @Override
    public void fetchData(int playerId) {
        if(RemoteConfiguration.MergedDataTracking.asBoolean()) {
            clearData(playerId);
            return;
        }
        
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
            .update(RemoteConfiguration.MergedDataTracking.asBoolean());
        fetchData(playerId);
        return result;
    }
    
    public void clearData(int playerId) {
        values.put(MiscInfoPlayersTable.FishCaught, 0);
        values.put(MiscInfoPlayersTable.TimesKicked, 0);
        values.put(MiscInfoPlayersTable.EggsThrown, 0);
        values.put(MiscInfoPlayersTable.FoodEaten, 0);
        values.put(MiscInfoPlayersTable.ArrowsShot, 0);
        values.put(MiscInfoPlayersTable.DamageTaken, 0);
        values.put(MiscInfoPlayersTable.BedsEntered, 0);
        values.put(MiscInfoPlayersTable.PortalsEntered, 0);
        values.put(MiscInfoPlayersTable.WordsSaid, 0);
        values.put(MiscInfoPlayersTable.CommandsSent, 0);
        values.put(MiscInfoPlayersTable.TimesJumped, 0);
        
        values.put(MiscInfoPlayersTable.CurKillStreak, 0);
        values.put(MiscInfoPlayersTable.MaxKillStreak, 0);
    }
    
    /**
     * Fetches the player data from the player, if he is online
     */
    public void refreshPlayerData() {
        Player player = Bukkit.getServer().getPlayerExact(playerName);
        if(player == null) return;

        if(player.isOp()) values.put(MiscInfoPlayersTable.IsOp, 1);
        else values.put(MiscInfoPlayersTable.IsOp, 0);
        if(player.isBanned()) values.put(MiscInfoPlayersTable.IsBanned, 1);
        else values.put(MiscInfoPlayersTable.IsBanned, 0);
        values.put(MiscInfoPlayersTable.PlayerIp, player.getAddress().getAddress().getHostAddress());
        
        values.put(MiscInfoPlayersTable.Gamemode, player.getGameMode().getValue());
        values.put(MiscInfoPlayersTable.ExpPercent, player.getExp());
        values.put(MiscInfoPlayersTable.ExpTotal, player.getTotalExperience());
        values.put(MiscInfoPlayersTable.ExpLevel, player.getLevel());
        values.put(MiscInfoPlayersTable.FoodLevel, player.getFoodLevel());
        values.put(MiscInfoPlayersTable.HealthLevel, player.getHealth());
        values.put(MiscInfoPlayersTable.ArmorLevel, Util.getArmorRating(player.getInventory()));
    }
    
    /**
     * Increments the specified miscellaneous statistic by 1
     * @param type Statistic type
     */
    public void incrementStat(MiscInfoPlayersTable type) {
        int value = 1;
        if(values.containsKey(type)) value = ((Integer) values.get(type)).intValue() + 1;
        values.put(type, value);
    }
    
    /**
     * Increments the miscellaneous statistic by the specified amount
     * @param type Statistic type
     * @param value Amount
     */
    public void incrementStat(MiscInfoPlayersTable type, int value) {
        if(values.containsKey(type)) value += ((Integer) values.get(type)).intValue();
        values.put(type, value);
    }
    
    /**
     * Logs player killing another player
     * @param player Player that was killed
     */
    public void killed(Player player) {
        OnlineSessionCache.fetch(player).died();
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
