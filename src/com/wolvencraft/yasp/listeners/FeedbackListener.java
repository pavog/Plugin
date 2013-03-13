package com.wolvencraft.yasp.listeners;

import net.minecraft.server.v1_4_R1.NBTTagCompound;
import net.minecraft.server.v1_4_R1.NBTTagList;
import net.minecraft.server.v1_4_R1.NBTTagString;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_4_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.DisplaySign;
import com.wolvencraft.yasp.DisplaySignFactory;
import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.Util;

public class FeedbackListener implements Listener {
	
	public FeedbackListener(StatsPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDisplaySignInit(PlayerInteractEvent event) {
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
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDisplaySignBreak(BlockBreakEvent event) {
		BlockState b = event.getBlock().getState();
		if(b instanceof Sign) {
			Message.debug("Checking for defined signs...");
			DisplaySign sign = DisplaySignFactory.get((Sign) b);
			if(sign == null) return;
			sign.deleteFile();
			DisplaySignFactory.removeSign(sign);
			Message.sendFormattedSuccess(event.getPlayer(), "Sign successfully removed");
			return;
		}
		else return;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBookOpen(PlayerInteractEvent event) {
		if (!(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR))) return;
		ItemStack craftItem = event.getItem();
		if(craftItem == null || craftItem.getTypeId() != 387) return;
		
		net.minecraft.server.v1_4_R1.ItemStack item = CraftItemStack.asNMSCopy(craftItem);
		
		NBTTagCompound tags = item.getTag();
        if (tags == null) {
        	tags = new NBTTagCompound();
            item.setTag(tags);
        }
        
        String author = tags.getString("author");
        String title = tags.getString("title");
        Player player = event.getPlayer();
        
        if(!(author.equals("YASP"))) return;
        Message.debug("Player " + player.getPlayerListName() + " read the book '" + title + "' by " + author);
        String playerName = title.split(" ")[0];
        
    	NBTTagList pages = new NBTTagList("pages");
    	String[] newPages = Util.getBookPages(playerName);
    	
        for(int i = 0; i < newPages.length; i++) {
        	pages.add(new NBTTagString("" + i + "", newPages[i]));
        }
    	tags.set("pages", pages);
    	item.setTag(tags);
    	
    	player.getInventory().remove(player.getItemInHand());
    	ItemStack newItem = CraftItemStack.asBukkitCopy(item);
    	player.getInventory().setItemInHand(newItem);
    	Message.debug("Refreshed the book contents");
	}
}
