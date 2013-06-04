package com.wolvencraft.yasp.util.hooks;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import be.Balor.Player.IBan;
import be.Balor.bukkit.AdminCmd.ACHelper;
import be.Balor.bukkit.AdminCmd.AdminCmd;
import belgium.Balor.Workers.AFKWorker;

import com.wolvencraft.yasp.HookManager.ApplicableHook;
import com.wolvencraft.yasp.util.serializable.BanRecordSerializable;

public class AdminCmdHook extends PluginHook {
    
    public AdminCmdHook() {
        super(ApplicableHook.ADMIN_CMD);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(type.getPluginName());
        
        if (plugin != null && plugin instanceof AdminCmd) {
            type.getModule().setActive(true);
        }
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
     * Returns the ban data as a Json array
     * @param player Player to look up
     * @return Ban data, or an empty string
     */
    public String getBan(Player player) {
        IBan ban = ACHelper.getInstance().getBan(player.getName());
        if(ban == null) return BanRecordSerializable.serialize(new ArrayList<BanRecordSerializable>());
        BanRecordSerializable banReason = new BanRecordSerializable(ban.getBanner(), ban.getReason(), ban.getDate().getTime() / 1000, -1L);
        return BanRecordSerializable.serialize(banReason);
    }
    
}
