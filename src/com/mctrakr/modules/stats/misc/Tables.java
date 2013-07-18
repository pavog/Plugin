/*
 * Tables.java
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

package com.mctrakr.modules.stats.misc;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.mctrakr.database.Column;

public class Tables {
    
    private Tables() { }
    
    /**
     * Represents the <i>misc_info_players</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum MiscInfoTable implements Column {
        TableName       ("misc_info_players"),
        PlayerId        ("player_id"),
        IsOp            ("is_op"),
        IsBanned        ("is_banned"),
        PlayerIp        ("player_ip"),
        Gamemode        ("gamemode"),
        ExpPercent      ("exp_perc"),
        ExpTotal        ("exp_total"),
        ExpLevel        ("exp_level"),
        FoodLevel       ("food_level"),
        HealthLevel     ("health"),
        ArmorLevel      ("armor_rating"),
        FishCaught      ("fish_caught"),
        TimesKicked     ("times_kicked"),
        EggsThrown      ("eggs_thrown"),
        FoodEaten       ("food_eaten"),
        ArrowsShot      ("arrows_shot"),
        DamageTaken     ("damage_taken"),
        TimesJumped     ("times_jumped"),
        BedsEntered     ("beds_entered"),
        PortalsEntered  ("portals_entered"),
        WordsSaid       ("words_said"),
        CommandsSent    ("commands_sent"),
        CurKillStreak   ("kill_streak"),
        MaxKillStreak   ("max_kill_streak");
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }

}
