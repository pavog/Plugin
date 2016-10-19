/*
 * Statistics Bukkit Plugin
 *
 * V2 Copyright (c) 2016 Paul <pavog> Vogel <http://www.paulvogel.me> and contributors.
 * V1 Copyright (c) 2016 bitWolfy <http://www.wolvencraft.com> and contributors.
 * Contributors are: Mario <MarioG1> Gallaun, Christian <Dazzl> Swan, Cory <Coryf88> Finnestad, Crimsonfoxy
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.wolvencraft.yasp.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class VariableManager {

    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum ServerVariable implements Variable {

        BLOCKS_BROKEN("blBroken"),
        BLOCKS_PLACED("blPlaced"),

        DISTANCE_TRAVELED("distTotal"),
        DISTANCE_FOOT("distFoot"),
        DISTANCE_RIDE("distRide"),
        DISTANCE_CART("distCart"),
        DISTANCE_BOAT("distBoat"),
        DISTANCE_FLIGHT("distFlight"),
        DISTANCE_SWIM("distSwim"),

        ITEMS_DROPPED("itDropped"),
        ITEMS_PICKEDUP("itPickedUp"),
        ITEMS_BROKEN("itBroken"),
        ITEMS_CRAFTED("itCrafted"),
        ITEMS_EATEN("itEaten"),

        PVP_KILLS("pvpKills"),
        PVE_KILLS("pveKills"),
        DEATHS("deaths"),

        LAST_SYNC("lastSync"),
        FIRST_STARTUP("firstStartup"),
        LAST_STARTUP("lastStartup"),
        CURRENT_UPTIME("uptime"),
        TOTAL_UPTIME("totalUptime"),
        LAST_SHUTDOWN("lastShutdown"),

        TOTAL_MEMORY("totalMemory"),
        FREE_MEMORY("freeMemory"),
        TICKS_PER_SECOND("tps"),

        SERVER_IP("ip"),
        SERVER_PORT("port"),
        SERVER_MOTD("motd"),
        BUKKIT_VERSION("version"),
        PLUGINS_COUNT("plugins"),

        SERVER_TIME("serverTime"),
        WEATHER("weather"),
        WEATHER_DURATION("weatherLeft"),

        MAX_PLAYERS_ONLINE("playerRecord"),
        MAX_PLAYERS_TIME("playerRecordTime"),

        MAX_PLAYERS_ALLOWED("playersAllowed"),
        PLAYERS_NOW("playersNow"),;

        private String alias;

    }

    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum PlayerVariable implements Variable {

        BLOCKS_BROKEN("blBroken"),
        BLOCKS_PLACED("blPlaced"),

        DISTANCE_TRAVELED("distTotal"),
        DISTANCE_FOOT("distFoot"),
        DISTANCE_RIDE("distRide"),
        DISTANCE_CART("distCart"),
        DISTANCE_BOAT("distBoat"),
        DISTANCE_FLIGHT("distFlight"),
        DISTANCE_SWIM("distSwim"),

        ITEMS_DROPPED("itDropped"),
        ITEMS_PICKEDUP("itPickedUp"),
        ITEMS_BROKEN("itBroken"),
        ITEMS_CRAFTED("itCrafted"),
        ITEMS_EATEN("itEaten"),

        PVP_KILLS("pvpKills"),
        PVE_KILLS("pveKills"),
        DEATHS("deaths"),
        KILL_DEATH_RATIO("kdr"),

        SESSION_START("join"),
        SESSION_LENGTH("sessionLength"),
        SESSION_LENGTH_RAW("sessionLengthRaw"),
        TOTAL_PLAYTIME("playtime"),
        TOTAL_PLAYTIME_RAW("playtimeRaw"),

        LOGINS("logins"),;

        private String alias;

    }

    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum HookVariable implements Variable {

        MONEY("money"),
        GROUP("group"),;

        private String alias;

    }

    public interface Variable {

        /**
         * Returns the alias associated with the variable
         *
         * @return Variable alias
         */
        public String getAlias();

    }

}
