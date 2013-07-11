package com.mctrakr.modules.hooks.admincmd;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import be.Balor.Player.IBan;
import be.Balor.bukkit.AdminCmd.ACHelper;
import belgium.Balor.Workers.AFKWorker;
import belgium.Balor.Workers.InvisibleWorker;

import com.mctrakr.modules.hooks.PluginHook;

public class AdminCmdHook extends PluginHook {
    
    private static final String PLUGIN_NAME = "AdminCmd";
    
    public AdminCmdHook() {
        super(AdminCmdDataStore.lock, PLUGIN_NAME);
    }
    
    /**
     * Returns the player's AFK status
     * @param player Player to look up
     * @return <b>true</b> if the player is AFK, <b>false</b> otherwise
     */
    public boolean isAfk(Player player) {
        return AFKWorker.getInstance().isAfk(player);
    }
    
    /**
     * Checks if the player is vanished
     * @param player Player to look up
     * @return <b>true</b> if the player is vanished, <b>false</b> otherwise
     */
    public boolean isInvisible(Player player) {
        return InvisibleWorker.getInstance().getAllInvisiblePlayers().contains(player);
    }
    
    /**
     * Returns the ban data as a Json array
     * @param playerName Player to look up
     * @return Ban data, or an empty string
     */
    public String getBan(String playerName) {
        IBan ban = ACHelper.getInstance().getBan(playerName);
        if(ban == null) return SerializableBanRecord.serialize(new ArrayList<SerializableBanRecord>());
        SerializableBanRecord banReason = new SerializableBanRecord(ban.getBanner(), ban.getReason(), ban.getDate().getTime() / 1000, -1L);
        return SerializableBanRecord.serialize(banReason);
    }
    
}
