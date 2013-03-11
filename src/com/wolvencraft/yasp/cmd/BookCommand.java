package com.wolvencraft.yasp.cmd;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.util.BookAPI;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.Util;

public class BookCommand implements BaseCommand {

	@Override
	public boolean run(String[] args) {
		Player player = (Player) CommandManager.getSender();
		BookAPI ermagherdBerks = new BookAPI(new ItemStack(Material.WRITTEN_BOOK));
		ermagherdBerks.addPages(Util.getBookPages(player));
		player.getInventory().addItem(ermagherdBerks.getItemStack());
		return false;
	}

	@Override
	public void getHelp() {
		Message.formatHelp("book", "", "Get a book with all your statistical information");
	}

}
