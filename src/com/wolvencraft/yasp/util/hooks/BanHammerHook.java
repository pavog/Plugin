package com.wolvencraft.yasp.util.hooks;

import java.util.ArrayList;
import java.util.List;

import name.richardson.james.bukkit.banhammer.BanHammer;
import name.richardson.james.bukkit.banhammer.persistence.BanRecord;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.wolvencraft.yasp.HookManager.ApplicableHook;
import com.wolvencraft.yasp.util.serializable.BanRecordSerializable;

public class BanHammerHook extends PluginHook {
    
    private BanHammer instance;
    
    public BanHammerHook() {
        super(ApplicableHook.BAN_HAMMER);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(type.getPluginName());
        
        if (plugin != null && plugin instanceof BanHammer) {
            type.getModule().setActive(true);
            instance = (BanHammer) plugin;
        }
    }
    
    /**
     * Returns the ban data as a Json array
     * @param player Player to look up
     * @return Ban data, or an empty string
     */
    public String getBan(Player player) {
        List<BanRecordSerializable> records = new ArrayList<BanRecordSerializable>();
        for(BanRecord record : instance.getHandler().getPlayerBans(player.getName())) {
            records.add(new BanRecordSerializable(
                    record.getCreator().getName(),
                    record.getReason(),
                    record.getCreatedAt().getTime() / 1000,
                    record.getExpiresAt().getTime() / 1000));
        }
        return BanRecordSerializable.serialize(records);
    }
    
}
