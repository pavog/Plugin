package com.wolvencraft.yasp.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class VariableManager {
    
    public interface Variable {
        
        /**
         * Returns the alias associated with the variable
         * @return Variable alias
         */
        public String getAlias();
        
    }
    
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum ServerVariable implements Variable {
        
        BLOCKS_BROKEN       ("blBroken"),
        BLOCKS_PLACED       ("blPlaced"),
        
        DISTANCE_TRAVELED   ("distTotal"),
        DISTANCE_FOOT       ("distFoot"),
        DISTANCE_RIDE       ("distRide"),
        DISTANCE_CART       ("distCart"),
        DISTANCE_BOAT       ("distBoat"),
        DISTANCE_FLIGHT     ("distFlight"),
        DISTANCE_SWIM       ("distSwim"),
        
        ITEMS_DROPPED       ("itDropped"),
        ITEMS_PICKEDUP      ("itPickedUp"),
        ITEMS_BROKEN        ("itBroken"),
        ITEMS_CRAFTED       ("itCrafted"),
        ITEMS_EATEN         ("itEaten"),
        
        PVP_KILLS           ("pvpKills"),
        PVE_KILLS           ("pveKills"),
        DEATHS              ("deaths"),
        
        LAST_SYNC           ("lastSync"),
        FIRST_STARTUP       ("firstStartup"),
        LAST_STARTUP        ("lastStartup"),
        CURRENT_UPTIME      ("uptime"),
        TOTAL_UPTIME        ("totalUptime"),
        LAST_SHUTDOWN       ("lastShutdown"),
        
        TOTAL_MEMORY        ("totalMemory"),
        FREE_MEMORY         ("freeMemory"),
        TICKS_PER_SECOND    ("tps"),
        
        SERVER_IP           ("ip"),
        SERVER_PORT         ("port"),
        SERVER_MOTD         ("motd"),
        BUKKIT_VERSION      ("version"),
        PLUGINS_COUNT       ("plugins"),
        
        SERVER_TIME         ("serverTime"),
        WEATHER             ("weather"),
        WEATHER_DURATION    ("weatherLeft"),
        
        MAX_PLAYERS_ONLINE  ("playerRecord"),
        MAX_PLAYERS_TIME    ("playerRecordTime"),
        
        MAX_PLAYERS_ALLOWED ("playersAllowed"),
        PLAYERS_NOW         ("playersNow"),
        
        ;
        
        private String alias;
        
    }
    
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum PlayerVariable implements Variable {
        
        BLOCKS_BROKEN       ("blBroken"),
        BLOCKS_PLACED       ("blPlaced"),
        
        DISTANCE_TRAVELED   ("distTotal"),
        DISTANCE_FOOT       ("distFoot"),
        DISTANCE_RIDE       ("distRide"),
        DISTANCE_CART       ("distCart"),
        DISTANCE_BOAT       ("distBoat"),
        DISTANCE_FLIGHT     ("distFlight"),
        DISTANCE_SWIM       ("distSwim"),
        
        ITEMS_DROPPED       ("itDropped"),
        ITEMS_PICKEDUP      ("itPickedUp"),
        ITEMS_BROKEN        ("itBroken"),
        ITEMS_CRAFTED       ("itCrafted"),
        ITEMS_EATEN         ("itEaten"),
        
        PVP_KILLS           ("pvpKills"),
        PVE_KILLS           ("pveKills"),
        DEATHS              ("deaths"),
        KILL_DEATH_RATIO    ("kdr"),
        
        SESSION_START       ("join"),
        SESSION_LENGTH      ("sessionLength"),
        SESSION_LENGTH_RAW  ("sessionLengthRaw"),
        TOTAL_PLAYTIME      ("playtime"),
        TOTAL_PLAYTIME_RAW  ("playtimeRaw"),
        
        LOGINS              ("logins"),
        
        ;
        
        private String alias;
        
    }
    
}
