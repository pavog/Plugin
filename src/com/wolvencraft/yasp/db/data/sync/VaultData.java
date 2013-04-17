/*
 * VaultData.java
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

import java.util.List;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.Settings;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.tables.Hook.VaultTable;
import com.wolvencraft.yasp.util.hooks.VaultHook;
import com.wolvencraft.yasp.util.serializable.GroupsSerializable;

/**
 * Hooks into Vault to track its statistics
 * @author bitWolfy
 *
 */
public class VaultData implements DataStore {
    
    private int playerId;
    private NormalData data;
    
    public VaultData(Player player, int playerId) {
        this.playerId = playerId;
        data = new VaultPlayerData(player, playerId);
    }

    @Override
    public DataStoreType getType() {
        return DataStoreType.Vault;
    }

    @Override
    public List<NormalData> getNormalData() { return null; }

    @Override
    public List<DetailedData> getDetailedData() { return null; }

    @Override
    public void sync() {
        data.pushData(playerId);
    }

    @Override
    public void dump() { }
    
    /**
     * Represents the information about player's group and balance
     * @author bitWolfy
     *
     */
    public class VaultPlayerData implements NormalData {
        
        private String playerName;
        
        private String groups;
        private double balance;
        
        /**
         * <b>Default constructor</b><br />
         * Creates a new normal table for the player
         * @param player Player object
         * @param playerId Player ID
         */
        public VaultPlayerData(Player player, int playerId) {
            this.playerName = player.getName();
            groups = "";
            balance = 0;
            
            fetchData(playerId);
        }
        
        @Override
        public void fetchData(int playerId) {
            if(!Settings.LocalConfiguration.Standalone.asBoolean()) {
                clearData(playerId);
                return;
            }
            
            groups = GroupsSerializable.serialize(playerName);
            balance = VaultHook.getBalance(playerName);
            
            if(Query.table(VaultTable.TableName)
                    .condition(VaultTable.PlayerId, playerId)
                    .exists()) return;
            
            Query.table(VaultTable.TableName)
                 .value(VaultTable.PlayerId, playerId)
                 .value(VaultTable.Balance, balance)
                 .value(VaultTable.GroupName, groups)
                 .insert();
        }
        
        @Override
        public boolean pushData(int playerId) {
            return Query.table(VaultTable.TableName)
                .value(VaultTable.Balance, balance)
                .value(VaultTable.GroupName, groups)
                .condition(VaultTable.PlayerId, playerId)
                .update();
        }
        
        @Override
        @Deprecated
        public void clearData(int playerId) { }
        
    }
    
}
