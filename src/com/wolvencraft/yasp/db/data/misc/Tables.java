package com.wolvencraft.yasp.db.data.misc;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.wolvencraft.yasp.db.DBTable;

public class Tables {
    
    private Tables() { }
    
    /**
     * Represents the <i>misc_info_players</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum PlayerData implements DBTable {
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
