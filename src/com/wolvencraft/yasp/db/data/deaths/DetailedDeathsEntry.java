package com.wolvencraft.yasp.db.data.deaths;

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.data.DetailedData;
import com.wolvencraft.yasp.db.tables.Detailed.DeathPlayers;
import com.wolvencraft.yasp.util.Util;

/**
 * Represents an entry in the Detailed data store.
 * It is static, i.e. it cannot be edited once it has been created.
 * @author bitWolfy
 *
 */
@Getter(AccessLevel.PUBLIC) 
public class DetailedDeathsEntry extends DetailedData {
    
    private DamageCause cause;
    private Location location;
    private long timestamp;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new DetailedDeathPlayersEntry based on the data provided
     * @param location
     * @param deathCause
     */
    public DetailedDeathsEntry(Location location, DamageCause cause) {
        this.cause = cause;
        this.location = location.clone();
        timestamp = Util.getTimestamp();
    }

    @Override
    public boolean pushData(int playerId) {
        return Query.table(DeathPlayers.TableName)
                .value(DeathPlayers.PlayerId, playerId)
                .value(DeathPlayers.Cause, cause.name())
                .value(DeathPlayers.World, location.getWorld().getName())
                .value(DeathPlayers.XCoord, location.getBlockX())
                .value(DeathPlayers.YCoord, location.getBlockY())
                .value(DeathPlayers.ZCoord, location.getBlockZ())
                .value(DeathPlayers.Timestamp, timestamp)
                .insert();
    }

}

