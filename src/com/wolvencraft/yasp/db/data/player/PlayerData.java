/*
 * PlayerData.java
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

package com.wolvencraft.yasp.db.data.player;

import org.bukkit.Location;

import com.wolvencraft.yasp.db.data.ConfigLock;
import com.wolvencraft.yasp.db.data.DataStore;
import com.wolvencraft.yasp.db.data.player.DetailedPlayerStats.PlayerLogin;
import com.wolvencraft.yasp.db.data.player.TotalPlayerStats.BasicPlayerStats;
import com.wolvencraft.yasp.session.OnlineSession;

/**
 * Holds generic player stats, plus login/logout times
 * @author bitWolfy
 *
 */
public class PlayerData extends DataStore<BasicPlayerStats, PlayerLogin> {
    
    public static ConfigLock lock = new ConfigLock(Type.Player.getAlias());
    
    public PlayerData(OnlineSession session) {
        super(session, Type.Player);
        
        addNormalDataEntry(new BasicPlayerStats(session.getId(), session.getBukkitPlayer()));
    }
    
    @Override
    public boolean onDataSync() {
        return lock.isEnabled();
    }
    
    /**
     * Logs player's login/logout location
     * @param location Location of the login
     * @param isLogin <b>true</b> if the player has logged in, <b>false</b> otherwise
     */
    public void addPlayerLog(Location location, boolean isLogin) {
        addDetailedDataEntry(new PlayerLogin(location, isLogin));
    }
    
    public BasicPlayerStats get() {
        return getNormalData().get(0);
    }
    
}
