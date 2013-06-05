package com.wolvencraft.yasp.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.listeners.handlers.HandlerManager;
import com.wolvencraft.yasp.listeners.handlers.HandlerManager.ExtraChecks;
import com.wolvencraft.yasp.listeners.handlers.SessionHandlers.PlayerLogin;
import com.wolvencraft.yasp.listeners.handlers.SessionHandlers.PlayerLogout;
import com.wolvencraft.yasp.settings.Constants.StatPerms;
import com.wolvencraft.yasp.settings.RemoteConfiguration;
import com.wolvencraft.yasp.util.Message;

public class SessionListener implements Listener {
    
    public SessionListener(Statistics plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(!HandlerManager.playerLookup(player, new ExtraChecks() {
            
            @Override
            public boolean check(Player player) {
                return StatPerms.Statistics.has(player);
            }
        })) return;
        HandlerManager.runAsyncTask(new PlayerLogin(player));
        
        if(RemoteConfiguration.ShowWelcomeMessages.asBoolean())
            Message.send(player, RemoteConfiguration.WelcomeMessage.asString().replace("<PLAYER>", player.getPlayerListName()));
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(!HandlerManager.playerLookup(player, new ExtraChecks() {
            
            @Override
            public boolean check(Player player) {
                return StatPerms.Statistics.has(player);
            }
        })) return;
        HandlerManager.runAsyncTask(new PlayerLogout(player));
    }
    
}
