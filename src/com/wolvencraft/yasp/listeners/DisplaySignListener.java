package com.wolvencraft.yasp.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.wolvencraft.yasp.DisplaySign;
import com.wolvencraft.yasp.DisplaySignFactory;
import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.util.Message;

public class DisplaySignListener implements Listener {
	
	public DisplaySignListener(StatsPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.isCancelled()) return;
		if(!event.getPlayer().isOp()) return;
		
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Block block = event.getClickedBlock();
			
			if(block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST) {
		        Message.debug("SignClickEvent passed");
				
		     	DisplaySign sign = DisplaySignFactory.get(block.getLocation());
				if(sign == null) {
					Message.debug("No registered sign found at this location");
					if(block.getState() instanceof Sign) {
						Message.debug("Checking to see if the sign is valid");
						Sign s = (Sign) block.getState();
						for(String line : s.getLines()) {
							if(line.startsWith("<Y>")) {
								Message.debug("Registering a new DisplaySign");
								DisplaySignFactory.addSign(new DisplaySign(s));
								return;
							}
						}
					}
				}
			}
			return;
		}
		else return;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onSignBreak(BlockBreakEvent event) {
		if(event.isCancelled()) return;
        BlockState b = event.getBlock().getState();
        if(b instanceof Sign) {
        	Message.debug("Checking for defined signs...");
        	DisplaySign sign = DisplaySignFactory.get((Sign) b);
        	if(sign == null) return;
        	
        	if(sign.deleteFile()) {
        		DisplaySignFactory.removeSign(sign);
        		Message.sendFormattedSuccess(event.getPlayer(), "Sign successfully removed");
        	}
        	else {
        		Message.sendFormattedError(event.getPlayer(), "Error removing sign!");
        		event.setCancelled(true);
        	}
        	return;
        }
        else return;
	}
}
