/*
 * Hook.java
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

package com.wolvencraft.yasp.db.tables;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents all plugin hook tables in the database.<br >
 * Stores table and column names, so that they can be safely used from the plugin
 * @author bitWolfy
 *
 */
public class Hook {
    
    /**
     * Represents the <i>hook_admincmd</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum AdminCmdTable implements DBTable {
        
        TableName       ("hook_admincmd"),
        PlayerId        ("player_id"),
        Afk             ("afk"),
        Vanished        ("vanished"),
        BanReason       ("ban_reason"),
        ;
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>hook_banhammer</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum BanHammerTable implements DBTable {
        
        TableName       ("hook_banhammer"),
        PlayerId        ("player_id"),
        Bans            ("bans")
        ;
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>hook_commandbook</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum CommandBookTable implements DBTable {
        
        TableName       ("hook_commandbook"),
        PlayerId        ("player_id"),
        Afk             ("afk"),
        God             ("god"),
        ;
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>hook_factions</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum FactionsTable implements DBTable {
        
        TableName       ("hook_factions"),
        PlayerId        ("player_id"),
        FactionName     ("faction_name"),
        CurrentlyIn     ("current_position"),
        CurrentPower    ("current_power"),
        MaximumPower    ("max_power"),
        FactionRole     ("role"),
        Title           ("title");
        ;
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>hook_jail</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum JailTable implements DBTable {
        
        TableName       ("hook_jail"),
        PlayerId        ("player_id"),
        IsJailed        ("is_jailed"),
        Jailer          ("jailer"),
        RemainingTime   ("remaining_time"),
        ;
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>hook_jobs</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum JobsTable implements DBTable {
        
        TableName       ("hook_jobs"),
        PlayerId        ("player_id"),
        ;
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>hook_mcmmo</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum McMMOTable implements DBTable {
        
        TableName       ("hook_mcmmo"),
        PlayerId        ("player_id"),
        Experience      ("experience"),
        Levels          ("levels"),
        Party           ("party"),
        ;
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>hook_mobarena</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum MobArenaTable implements DBTable {
        
        TableName       ("hook_mobarena"),
        PlayerId        ("player_id"),
        IsPlaying       ("is_playing"),
        CurrentArena    ("current_arena"),
        ;
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>hook_pvparena</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum PvpArenaTable implements DBTable {
        
        TableName       ("hook_pvparena"),
        PlayerId        ("player_id"),
        IsPlaying       ("is_playing"),
        CurrentArena    ("current_arena"),
        ;
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>hook_towny</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum TownyTable implements DBTable {
        
        TableName       ("hook_towny"),
        PlayerId        ("player_id"),
        PlayerData      ("player_data"),
        ;
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>hook_vanish</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum VanishTable implements DBTable {
        
        TableName       ("hook_vanish"),
        PlayerId        ("player_id"),
        IsVanished      ("is_vanished"),
        ;
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>hook_vault</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum VaultTable implements DBTable {
        
        TableName       ("hook_vault"),
        PlayerId        ("player_id"),
        GroupName       ("group"),
        Balance         ("balance");
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>hook_votifier_totals</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum VotifierTotalsTable implements DBTable {
        
        TableName       ("hook_votifier_totals"),
        PlayerId        ("player_id"),
        ServiceName     ("service_name"),
        Votes           ("votes");
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>hook_votifier_detailed</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum DetailedVotifierTable implements DBTable {
        
        TableName       ("hook_votifier_detailed"),
        PlayerId        ("player_id"),
        ServiceName     ("service_name"),
        Timestamp       ("time");
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
    /**
     * Represents the <i>hook_worldguard</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum WorldGuardTable implements DBTable {
        
        TableName       ("hook_worldguard"),
        PlayerId        ("player_id"),
        RegionName      ("regions"),
        RegionFlags     ("flags");
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
}
