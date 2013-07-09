/*
 * PlayerDataStore.java
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

package com.mctrakr.db.data.player;

import org.bukkit.Location;

import com.mctrakr.db.data.ConfigLock;
import com.mctrakr.db.data.DataStore;
import com.mctrakr.db.data.player.PlayerDetailedStats.PlayerLogin;
import com.mctrakr.db.data.player.PlayerTotalStats.BasicPlayerStats;
import com.mctrakr.session.OnlineSession;

/**
 * Holds generic player stats, plus login/logout times
 * @author bitWolfy
 *
 */
public class PlayerDataStore extends DataStore<BasicPlayerStats, PlayerLogin> {
    
    public static ConfigLock lock = new ConfigLock(ModuleType.Player);
    
    public PlayerDataStore(OnlineSession session) {
        super(session, ModuleType.Player);
        
        addNormalDataEntry(new BasicPlayerStats(session.getId(), session.getBukkitPlayer()));
    }
    
    @Override
    public ConfigLock getLock() {
        return lock;
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
