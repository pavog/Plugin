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
import com.wolvencraft.yasp.settings.Module;
import com.wolvencraft.yasp.util.Message;

public class VaultHook extends PluginHook {
    
    private static Economy economy;
    private static Permission permissions;
    
    public VaultHook() {
        super(Module.Vault, "Vault");
    }
    
    @Override
    protected void onEnable() {
        ServicesManager svm = Statistics.getInstance().getServer().getServicesManager();
        
        try { economy = ((RegisteredServiceProvider<Economy>)(svm.getRegistration(Economy.class))).getProvider();}
        catch(Throwable t) { Message.log(Level.SEVERE, "An error occurred while initializing economy"); }
        
        try { permissions = ((RegisteredServiceProvider<Permission>)(svm.getRegistration(Permission.class))).getProvider(); }
        catch(Throwable t) { Message.log(Level.SEVERE, "An error occurred while initializing permissions"); }
    }
    
    @Override
    protected void onDisable() {
        economy = null;
        permissions = null;
    }
    
    /**
     * <b>Returns the primary group for the specified player</b>
     * @deprecated Use <code>getGroup(player, world);</code> instead
     * @param player Player object
     * @return Player's group
     */
    public static String getGroup(Player player) {
        if(permissions == null) return "";
        return permissions.getPrimaryGroup(player);
    }
    
    /**
     * <b>Returns the primary group for the specified player in the world</b>
     * @param player Player name
     * @param world World name
     * @return Player's group
     */
    public static String getGroup(String player, String world) {
        if(permissions == null) return "";
        return permissions.getPrimaryGroup(world, player);
    }
    
    /**
     * Returns the player's balance
     * @param playerName Player's name
     * @return Player's balance
     */
    public static double getBalance(String playerName) {
        if(economy == null) return 0;
        return economy.getBalance(playerName);
    }
    
}
