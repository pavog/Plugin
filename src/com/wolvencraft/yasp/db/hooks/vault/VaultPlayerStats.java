/*
 * VaultPlayerStats.java
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

package com.wolvencraft.yasp.db.hooks.vault;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.data.DataStore.HookType;
import com.wolvencraft.yasp.db.data.NormalData;
import com.wolvencraft.yasp.db.hooks.vault.Tables.VaultTable;
import com.wolvencraft.yasp.managers.HookManager;

/**
 * Represents the information about player's group and balance
 * @author bitWolfy
 *
 */
public class VaultPlayerStats extends NormalData {
    
    private String playerName;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new normal table for the player
     * @param player Player object
     * @param playerId Player ID
     */
    public VaultPlayerStats(Player player, int playerId) {
        this.playerName = player.getName();
        fetchData(playerId);
    }
    
    @Override
    public void fetchData(int playerId) {
        VaultHook hook = (VaultHook) HookManager.getHook(HookType.Vault);
        if(hook == null) return;
        
        if(Query.table(VaultTable.TableName)
                .condition(VaultTable.PlayerId, playerId)
                .exists()) return;
        
        Query.table(VaultTable.TableName)
             .value(VaultTable.PlayerId, playerId)
             .value(VaultTable.Balance, hook.getBalance(playerName))
             .value(VaultTable.GroupName, SerializableGroup.serialize(playerName, hook))
             .insert();
    }
    
    @Override
    public boolean pushData(int playerId) {
        VaultHook hook = (VaultHook) HookManager.getHook(HookType.Vault);
        if(hook == null) return false;
        
        return Query.table(VaultTable.TableName)
            .value(VaultTable.Balance, hook.getBalance(playerName))
            .value(VaultTable.GroupName, SerializableGroup.serialize(playerName, hook))
            .condition(VaultTable.PlayerId, playerId)
            .update();
    }
    
}
