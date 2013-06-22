/*
 * AdminCmdHook.java
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

import java.util.ArrayList;

import org.bukkit.entity.Player;

import be.Balor.Player.IBan;
import be.Balor.bukkit.AdminCmd.ACHelper;
import belgium.Balor.Workers.AFKWorker;
import belgium.Balor.Workers.InvisibleWorker;

import com.wolvencraft.yasp.settings.Module;
import com.wolvencraft.yasp.util.serializable.BanRecordSerializable;

public class AdminCmdHook extends PluginHook {
    
    public AdminCmdHook() {
        super(Module.AdminCmd, "AdminCmd");
    }
    
    /**
     * Returns the player's AFK status
     * @param player Player to look up
     * @return <b>true</b> if the player is AFK, <b>false</b> otherwise
     */
    public static boolean isAfk(Player player) {
        return AFKWorker.getInstance().isAfk(player);
    }
    
    /**
     * Checks if the player is vanished
     * @param player Player to look up
     * @return <b>true</b> if the player is vanished, <b>false</b> otherwise
     */
    public static boolean isInvisible(Player player) {
        return InvisibleWorker.getInstance().getAllInvisiblePlayers().contains(player);
    }
    
    /**
     * Returns the ban data as a Json array
     * @param playerName Player to look up
     * @return Ban data, or an empty string
     */
    public static String getBan(String playerName) {
        IBan ban = ACHelper.getInstance().getBan(playerName);
        if(ban == null) return BanRecordSerializable.serialize(new ArrayList<BanRecordSerializable>());
        BanRecordSerializable banReason = new BanRecordSerializable(ban.getBanner(), ban.getReason(), ban.getDate().getTime() / 1000, -1L);
        return BanRecordSerializable.serialize(banReason);
    }
}
