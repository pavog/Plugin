/*
 * VaultHookFactory.java
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

package com.wolvencraft.yasp.db.data.hooks;

import java.util.List;
import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

import com.wolvencraft.yasp.Settings;
import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.tables.Hook.VaultTable;
import com.wolvencraft.yasp.exceptions.DatabaseConnectionException;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.PatchFetcher;
import com.wolvencraft.yasp.util.PatchFetcher.PatchType;

/**
 * Hooks into Vault to track its statistics
 * @author bitWolfy
 *
 */
public class VaultHookFactory implements HookDataStore {
    
    private static VaultHookFactory instance;
    private static Economy economy;
    private static Permission permissions;
    
    public VaultHookFactory() {
        ServicesManager svm = Statistics.getInstance().getServer().getServicesManager();
        boolean broken = false;
        
        try { economy = ((RegisteredServiceProvider<Economy>)(svm.getRegistration(Economy.class))).getProvider();}
        catch(Exception ex) { Message.log(Level.SEVERE, "An error occurred while initializing economy"); broken = true; }
        
        try { permissions = ((RegisteredServiceProvider<Permission>)(svm.getRegistration(Permission.class))).getProvider(); }
        catch(Exception ex) { Message.log(Level.SEVERE, "An error occurred while initializing permissions"); broken = true; }
        
        if(!broken) {
            Message.log("Vault hook enabled!");
            Settings.ActiveHooks.HookVault.setActive(true);
            instance = this;
        }
    }
    
    public static VaultHookFactory getInstance() {
        return instance;
    }
    
    @Override
    public void onEnable() {
        try {
            PatchFetcher.fetch(PatchType.Vault);
            Database.executePatch("1.vault");
        } catch (DatabaseConnectionException ex) {
            Message.log(Level.SEVERE, ex.getMessage());
        }
    }

    @Override
    public void onDisable() {
        economy = null;
        permissions = null;
    }
    
    /**
     * Represents the general player's information
     * @author bitWolfy
     *
     */
    public class VaultPlayerData implements HookNormalData {
        
        private String playerName;
        
        private String groupName;
        private double balance;
        
        /**
         * <b>Default constructor</b><br />
         * Creates a new normal table for the player
         * @param player Player object
         * @param playerId Player ID
         */
        public VaultPlayerData(Player player, int playerId) {
            this.playerName = player.getName();
            this.groupName = "";
            this.balance = 0;
            
            fetchData(playerId);
        }
        
        @Override
        public void fetchData(int playerId) {
            Player player = Bukkit.getPlayerExact(playerName);
            if(player == null) return;
            
            if(permissions != null) groupName = permissions.getPrimaryGroup(player);
            else groupName = null;
            if(economy != null) balance = economy.getBalance(player.getPlayerListName());
            else balance = -1;
            
            if(Query.table(VaultTable.TableName)
                    .condition(VaultTable.PlayerId, playerId)
                    .exists()) return;
            
            Query.table(VaultTable.TableName)
            .value(VaultTable.PlayerId, playerId)
            .value(VaultTable.Balance, balance)
            .value(VaultTable.GroupName, groupName)
            .insert();
        }
        
        @Override
        public boolean pushData(int playerId) {
            return Query.table(VaultTable.TableName)
                .value(VaultTable.Balance, balance)
                .value(VaultTable.GroupName, groupName)
                .condition(VaultTable.PlayerId, playerId)
                .update();
        }
        
    }
    
    /**
     * Represents the detailed balance information for the player
     * @author bitWolfy
     *
     */
    public class DetailedEconomyData implements HookDetailedData {

        @Override
        public boolean pushData(int playerId) {
            // TODO Auto-generated method stub
            return false;
        }
        
    }

    @Override
    public StoreType getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<NormalData> getNormalData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<DetailedData> getDetailedData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void sync() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dump() {
        // TODO Auto-generated method stub
        
    }
}
