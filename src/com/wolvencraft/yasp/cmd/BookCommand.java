/*
 * Statistics
 * Copyright (C) 2013 bitWolfy <http://www.wolvencraft.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

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
