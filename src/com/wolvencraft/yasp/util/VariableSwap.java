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

package com.wolvencraft.yasp.util;

import com.wolvencraft.yasp.db.totals.HookTotals;
import com.wolvencraft.yasp.db.totals.PlayerTotals;
import com.wolvencraft.yasp.util.VariableManager.PlayerVariable;
import com.wolvencraft.yasp.util.VariableManager.HookVariable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Mario
 */
public class VariableSwap {
    
    
    /**
     * A hard-coded list of all player aviable book variables.<br />
     * @author MarioG1
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum PlayerData {
        SESSION_LENGTH              ("%SESSION_LENGTH%", PlayerVariable.SESSION_LENGTH),
        TOTAL_PLAYTIME              ("%TOTAL_PLAYTIME%", PlayerVariable.TOTAL_PLAYTIME),
                
        PVP_KILLS                   ("%PVP_KILLS%", PlayerVariable.PVP_KILLS),
        PVE_KILLS                   ("%PVE_KILLS%", PlayerVariable.PVE_KILLS),
        DEATHS                      ("%DEATHS%", PlayerVariable.DEATHS),
        KILL_DEATH_RATIO            ("%KILL_DEATH_RATIO%", PlayerVariable.KILL_DEATH_RATIO),
        
        BLOCKS_BROKEN               ("%BLOCKS_BROKEN%", PlayerVariable.BLOCKS_BROKEN),
        BLOCKS_PLACED               ("%BLOCKS_PLACED%", PlayerVariable.BLOCKS_PLACED),
        
        DISTANCE_FOOT               ("%DISTANCE_FOOT%", PlayerVariable.DISTANCE_FOOT),
        DISTANCE_BOAT               ("%DISTANCE_BOAT%", PlayerVariable.DISTANCE_BOAT),
        DISTANCE_CART               ("%DISTANCE_CART%", PlayerVariable.DISTANCE_CART),
        DISTANCE_RIDE               ("%DISTANCE_RIDE%", PlayerVariable.DISTANCE_RIDE),
        DISTANCE_SWIM               ("%DISTANCE_SWIM%", PlayerVariable.DISTANCE_SWIM),
        DISTANCE_FLIGHT             ("%DISTANCE_FLIGHT%", PlayerVariable.DISTANCE_FLIGHT),
        DISTANCE_TRAVELED           ("%DISTANCE_TRAVELED%", PlayerVariable.DISTANCE_TRAVELED),
        
        ITEMS_DROPPED               ("%ITEMS_DROPPED%", PlayerVariable.ITEMS_DROPPED),
        ITEMS_PICKEDUP              ("%ITEMS_PICKEDUP%", PlayerVariable.ITEMS_PICKEDUP),
      
    ;
        
        private String name;
        private PlayerVariable internal;
        
        public static String swap(String line, PlayerTotals stats) {
            for(PlayerData entry : PlayerData.values()) {
                line = line.replace(entry.name, stats.getValue(entry.internal).toString());
            }
            return line;
        }
    }
    
     /**
     * A hard-coded list of all hook aviable book variables.<br />
     * @author MarioG1
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum HookData {
        MONEY              ("%MONEY%", HookVariable.MONEY),
        GROUP              ("%GROUP%", HookVariable.GROUP),
      
    ;
        
        private String name;
        private HookVariable internal;
        
        public static String swap(String line, HookTotals stats) {
            for(HookData entry : HookData.values()) {
                line = line.replace(entry.name, stats.getValue(entry.internal).toString());
            }
            return line;
        }
    }
    
    
    
}
