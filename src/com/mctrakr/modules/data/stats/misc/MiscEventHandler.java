/*
 * MiscEventHandler.java
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

package com.mctrakr.modules.data.stats.misc;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.bukkit.entity.Player;

import com.mctrakr.cache.SessionCache;
import com.mctrakr.modules.data.stats.misc.Tables.MiscInfoTable;
import com.mctrakr.settings.ConfigLock.PrimaryType;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class MiscEventHandler {
    
    /**
     * Executed when a player's stat has to be incremented asynchronously
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class PlayerIncrementStat implements Runnable {

        private Player player;
        private MiscInfoTable stat;
        private double value;
        
        public PlayerIncrementStat(Player player, MiscInfoTable stat) {
            this.player = player;
            this.stat = stat;
            this.value = 1;
        }
        
        @Override
        public void run() {
            MiscDataStore misc = ((MiscDataStore) SessionCache.fetch(player).getDataStore(PrimaryType.Misc));
            if(misc != null) misc.getNormalData().incrementStat(stat, value);
        }
    }
    
}
