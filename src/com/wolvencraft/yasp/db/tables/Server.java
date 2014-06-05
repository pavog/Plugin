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

package com.wolvencraft.yasp.db.tables;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents any Server tables in the database.<br >
 * Stores table and column names, so that they can be safely used from the plugin.
 * @author bitWolfy
 *
 */
public class Server {
    
     /**
     * Represents the <i>settings</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum ServerStatsTable implements DBTable {
        TableName       ("server_statistics"),
        Id              ("server_id"),
        Name            ("server_name"),
        Ip              ("server_ip"),
        Motd            ("server_motd"),
        Port            ("server_port"),
        Version         ("bukkit_version"),
        CurrentUptime   ("current_uptime"),
        FirstStartup    ("first_startup"),
        FreeMemory      ("free_memory"),
        JavaVendor      ("java.vendor"),
        JavaVendorUrl   ("java.vendor.url"),
        JavaVersion     ("java.version"),
        JavaVmName      ("java.vm.name"),
        JavaVmVendor    ("java.vm.vendor"),
        JavaVmVersion   ("java.vm.version"),
        LastShutdown    ("last_shutdown"),
        LastStartup     ("last_startup"),
        MaxPlayers      ("max_players_online"),
        MaxPlayersTime  ("max_players_online_time"),
        OsArch          ("os.arch"),
        OsName          ("os.name"),
        OsVersion       ("os.version"),
        PlayersAllowed  ("players_allowed"),
        Plugins         ("plugins"),
        ServerTime      ("server_time"),
        TPS             ("ticks_per_second"),
        TotalMemory     ("total_memory"),
        TotalUptime     ("total_uptime"),
        Weather         ("weather"),
        WeatherDuration ("weather_duration");
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }
    
}
