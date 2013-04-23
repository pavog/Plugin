/*
 * VaultHook.java
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

package com.wolvencraft.yasp.util.hooks;

import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.exceptions.DatabaseConnectionException;
import com.wolvencraft.yasp.settings.LocalConfiguration;
import com.wolvencraft.yasp.settings.Module;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.PatchFetcher;
import com.wolvencraft.yasp.util.PatchFetcher.PatchType;

/**
 * Simplistic Vault hook
 * @author bitWolfy
 *
 */
public class VaultHook {
    
    private static Economy economy;
    private static Permission permissions;
    
    /**
     * <b>Default constructor</b><br />
     * Connects to Vault economy and permissions hooks
     */
    public VaultHook() {
        ServicesManager svm = Statistics.getInstance().getServer().getServicesManager();
        
        try { economy = ((RegisteredServiceProvider<Economy>)(svm.getRegistration(Economy.class))).getProvider();}
        catch(Exception ex) { Message.log(Level.SEVERE, "An error occurred while initializing economy"); }
        
        try { permissions = ((RegisteredServiceProvider<Permission>)(svm.getRegistration(Permission.class))).getProvider(); }
        catch(Exception ex) { Message.log(Level.SEVERE, "An error occurred while initializing permissions"); }
        
        if(economy != null && permissions != null) {
            Message.log("Vault hook enabled!");
            Module.Vault.setActive(true);
        }
    }
    
    /**
     * <b>Returns the primary group for the specified player</b>
     * @deprecated Use <code>getGroup(player, world);</code> instead
     * @param player Player object
     * @return Player's group
     */
    public static String getGroup(Player player) {
        return permissions.getPrimaryGroup(player);
    }
    
    /**
     * <b>Returns the primary group for the specified player in the world</b>
     * @param player Player name
     * @param world World name
     * @return Player's group
     */
    public static String getGroup(String player, String world) {
        return permissions.getPrimaryGroup(world, player);
    }
    
    /**
     * Returns the player's balance
     * @param playerName Player's name
     * @return Player's balance
     */
    public static double getBalance(String playerName) {
        return economy.getBalance(playerName);
    }
    
    /**
     * Code that is to be executed when the hook is being enabled.<br />
     * This should include a database patch, if necessary
     */
    public void onEnable() {
        try {
            PatchFetcher.fetch(PatchType.Vault);
            Database.executePatch("1.vault");
        } catch (DatabaseConnectionException ex) {
            Message.log(Level.SEVERE, ex.getMessage());
            if(LocalConfiguration.Debug.asBoolean()) ex.printStackTrace();
        }
    }
    
    /**
     * Code that is to be executed when the hook is being disabled.<br />
     * This should include a cleanup routine.
     */
    public void onDisable() {
        economy = null;
        permissions = null;
    }
    
}
