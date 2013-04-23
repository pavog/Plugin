/*
 * FactionsData.java
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

package com.wolvencraft.yasp.db.data;

import java.util.List;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.tables.Hook.FactionsTable;
import com.wolvencraft.yasp.util.hooks.FactionsHook;

/**
 * Hooks into Factions to track its statistics
 * @author bitWolfy
 *
 */
public class FactionsData implements DataStore {
    
    private int playerId;
    private NormalData data;
    
    public FactionsData(Player player, int playerId) {
        this.playerId = playerId;
        data = new FactionsPlayerData(player, playerId);
    }
    
    @Override
    public DataStoreType getType() { return DataStoreType.Hook_Factions; }

    @Override
    @Deprecated
    public List<NormalData> getNormalData() { return null; }

    @Override
    @Deprecated
    public List<DetailedData> getDetailedData() { return null; }

    @Override
    public void sync() {
        data.pushData(playerId);
    }

    @Override
    public void dump() { }
    
    public class FactionsPlayerData implements NormalData {
        
        private String playerName;
        
        public FactionsPlayerData (Player player, int playerId) {
            this.playerName = player.getName();
            
            fetchData(playerId);
        }
        
        @Override
        public void fetchData(int playerId) {
            if(Query.table(FactionsTable.TableName)
                    .condition(FactionsTable.PlayerId, playerId)
                    .exists()) return;
            
            Query.table(FactionsTable.TableName)
                .value(FactionsTable.PlayerId, playerId)
                .value(FactionsTable.CurrentPower, FactionsHook.getPower(playerName))
                .value(FactionsTable.MaximumPower, FactionsHook.getMaxPower(playerName))
                .value(FactionsTable.CurrentlyIn, FactionsHook.getCurrentLocation(playerName))
                .value(FactionsTable.FactionName, FactionsHook.getCurrentFaction(playerName))
                .value(FactionsTable.Title, FactionsHook.getTitle(playerName))
                .value(FactionsTable.FactionRole, FactionsHook.getRole(playerName))
                .insert();
        }

        @Override
        public boolean pushData(int playerId) {
            return Query.table(FactionsTable.TableName)
                .value(FactionsTable.CurrentPower, FactionsHook.getPower(playerName))
                .value(FactionsTable.MaximumPower, FactionsHook.getMaxPower(playerName))
                .value(FactionsTable.CurrentlyIn, FactionsHook.getCurrentLocation(playerName))
                .value(FactionsTable.FactionName, FactionsHook.getCurrentFaction(playerName))
                .value(FactionsTable.Title, FactionsHook.getTitle(playerName))
                .value(FactionsTable.FactionRole, FactionsHook.getRole(playerName))
                .condition(FactionsTable.PlayerId, playerId)
                .update();
        }

        @Override
        @Deprecated
        public void clearData(int playerId) { }
        
    }
    
}
