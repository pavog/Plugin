/*
 * JailHook.java
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

import com.matejdro.bukkit.jail.Jail;
import com.matejdro.bukkit.jail.JailAPI;
import com.matejdro.bukkit.jail.JailPrisoner;
import com.wolvencraft.yasp.settings.Module;

public class JailHook extends PluginHook {
    
    private static JailAPI instance;
    
    public JailHook() {
        super(Module.Jail, "Jail");
    }
    
    @Override
    protected void onEnable() {
        instance = ((Jail) plugin).API;
    }
    
    @Override
    protected void onDisable() {
        instance = null;
    }
    
    /**
     * Checks if the player is jailed
     * @param playerName Player name
     * @return <b>true</b> if the player is jailed, <b>false</b> otherwise
     */
    public static boolean isJailed(String playerName) {
        if(instance.getPrisoner(playerName) == null) return false;
        return true;
    }
    
    /**
     * Returns the jailer's name
     * @param playerName Player name
     * @return Jailer
     */
    public static String getJailer(String playerName) {
        JailPrisoner prisoner = instance.getPrisoner(playerName);
        if(prisoner == null) return "";
        return prisoner.getJailer();
    }
    
    /**
     * Returns the remaining jail time
     * @param playerName Player name
     * @return Remaining jail time
     */
    public static int getRemainingTime(String playerName) {
        JailPrisoner prisoner = instance.getPrisoner(playerName);
        if(prisoner == null) return 0;
        return prisoner.getRemainingTime();
    }
    
}
