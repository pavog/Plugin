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

package com.mctrakr.session;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.mctrakr.cache.SessionCache;
import com.mctrakr.database.Query;
import com.mctrakr.managers.ModuleManager;
import com.mctrakr.modules.data.DataStore;
import com.mctrakr.modules.data.stats.player.Tables.PlayersTable;
import com.mctrakr.settings.ConfigLock.ModuleType;

/**
 * Represents a player session that is created when a player logs into the server.<br />
 * This session can only be created from a Player object, therefore, the player must have
 * logged into the server at least once.
 * @author bitWolfy
 *
 */
@Getter(AccessLevel.PUBLIC)
public class OnlineSession extends PlayerSession {
    
    private List<DataStore> dataStores;
    private boolean firstJoin;
    
    private Scoreboard scoreboard;
    @Setter(AccessLevel.PUBLIC)
    private Objective objective;
    
    public OnlineSession(Player player) {
        super(player.getName(), SessionCache.getPlayerId(player));
        
        dataStores = new ArrayList<DataStore>();
        dataStores.addAll(ModuleManager.getModules(this));
        
        firstJoin = false;
        
        Query.table(PlayersTable.TableName)
            .value(PlayersTable.Online, true)
            .condition(PlayersTable.PlayerId, id)
            .update();
    }
    
    public OnlineSession(Player player, boolean firstJoin) {
        super(player.getName(), SessionCache.getPlayerId(player));
        
        dataStores = new ArrayList<DataStore>();
        dataStores.addAll(ModuleManager.getModules(this));
        
        this.firstJoin = firstJoin;
        
        Query.table(PlayersTable.TableName)
            .value(PlayersTable.Online, true)
            .condition(PlayersTable.PlayerId, id)
            .update();
    }
    
    /**
     * Returns the Bukkit player object associated with this session
     * @return Player object
     */
    public Player getBukkitPlayer() {
        return Bukkit.getPlayerExact(name);
    }
    
    /**
     * Returns the data store with the specified type
     * @param type Data store type
     * @return Data store, or <b>null</b> if the type is not valid
     */
    public DataStore getDataStore(ModuleType type) {
        return getDataStore(type.getAlias());
    }
    
    /**
     * Returns the data store with the specified type
     * @param type Data store type
     * @return Data store, or <b>null</b> if the type is not valid
     */
    public DataStore getDataStore(String type) {
        for(DataStore store : dataStores) {
            if(store.getType().getAlias().equals(type)) return store;
        }
        return null;
    }
    
    /**
     * Performs a database operation to push the locally stored data.
     */
    public void pushData() {
        for(DataStore store : dataStores) store.pushData();
        playerTotals.fetchData();
    }
    
    /**
     * Dumps all locally stored data
     */
    public void dumpData() {
        for(DataStore store : dataStores) store.dump();
    }
    
    @Override
    public void finalize() {
        Query.table(PlayersTable.TableName)
            .value(PlayersTable.Online, false)
            .condition(PlayersTable.PlayerId, id)
            .update();
    }
    
    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
        getBukkitPlayer().setScoreboard(scoreboard);
    }
}
