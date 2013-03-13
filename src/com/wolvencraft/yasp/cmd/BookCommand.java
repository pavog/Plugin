package com.wolvencraft.yasp.cmd;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.Util;

public class BookCommand implements BaseCommand {

	@Override
	public boolean run(String[] args) {
		if(!(CommandManager.getSender() instanceof Player)) {
			Message.sendFormattedError(CommandManager.getSender(), "This command can only be executed by a living player");
			return false;
		}
		Player player = (Player) CommandManager.getSender();
		player.getInventory().addItem(Util.compileStatsBook(player));
		return false;
	}

	@Override
	public void getHelp() {
		Message.formatHelp("book", "", "Get a book with all your statistical information");
	}

}
