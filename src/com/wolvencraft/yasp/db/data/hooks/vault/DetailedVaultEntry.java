/*
 * Copyright (C) 2014 Mario
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.wolvencraft.yasp.db.data.hooks.vault;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.data.DetailedData;
import com.wolvencraft.yasp.db.tables.Hook;
import com.wolvencraft.yasp.util.Util;
import com.wolvencraft.yasp.util.hooks.VaultHook;
import org.bukkit.entity.Player;

/**
 *
 * @author Mario
 */
public class DetailedVaultEntry extends DetailedData {
    private final String playerName;
    
    private final double balance;
    private final long timestamp; 
    
     /**
     * <b>Default constructor</b><br />
     * Creates a new detailed Vault entry fot the player (current money + rank)
     * @param player Player object
     * @param playerId Player ID
     */   
    public DetailedVaultEntry(Player player, int playerId) {
        this.playerName = player.getName();
        this.balance = VaultHook.getBalance(playerName);
        this.timestamp = Util.getTimestamp();
    } 
    
    @Override
    public boolean pushData(int playerId) {
        return Query.table(Hook.DetailedVaultTable.TableName)
                    .value(Hook.DetailedVaultTable.PlayerId, playerId)
                    .value(Hook.DetailedVaultTable.Balance, balance)
                    .value(Hook.DetailedVaultTable.TimeStamp, timestamp)
                    .insert();
        }
}
 