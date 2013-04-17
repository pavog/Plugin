package com.wolvencraft.yasp.db.data.sync;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.tables.Hook.WorldGuardTable;
import com.wolvencraft.yasp.util.hooks.WorldGuardHook;

public class WorldGuardData implements DataStore {
    
    private int playerId;
    private NormalData data;
    
    public WorldGuardData(Player player, int playerId) {
        data = new WorldGuardPlayerData(playerId, player);
        this.playerId = playerId;
    }
    
    @Override
    public DataStoreType getType() {
        return DataStoreType.WorldGuard;
    }

    @Override
    @Deprecated
    public List<NormalData> getNormalData() { return null; }

    @Override
    @Deprecated
    public List<DetailedData> getDetailedData() { return null; }

    @Override
    public void sync() {
        data.pushData(playerId);
    }

    @Override
    public void dump() { }
    
    public class WorldGuardPlayerData implements NormalData {
        
        private String playerName;
        
        public WorldGuardPlayerData(int playerId, Player player) {
            playerName = player.getName();
            
            fetchData(playerId);
        }
        
        @Override
        public void fetchData(int playerId) {
            if(Query.table(WorldGuardTable.TableName)
                    .condition(WorldGuardTable.PlayerId, playerId)
                    .exists()) return;
            
            Query.table(WorldGuardTable.TableName)
                 .value(WorldGuardTable.PlayerId, playerId)
                 .value(WorldGuardTable.RegionName, "")
                 .value(WorldGuardTable.RegionFlags, "")
                 .insert();
        }

        @Override
        public boolean pushData(int playerId) {
            Player player = Bukkit.getServer().getPlayerExact(playerName);
            if(player == null) return false;
            
            return Query.table(WorldGuardTable.TableName)
                 .value(WorldGuardTable.RegionName, WorldGuardHook.getRegions(player.getLocation()))
                 .value(WorldGuardTable.RegionFlags, WorldGuardHook.getFlags(player.getLocation()))
                 .condition(WorldGuardTable.PlayerId, playerId)
                 .update();
        }

        @Override
        @Deprecated
        public void clearData(int playerId) { }
        
        
        
    }

}
