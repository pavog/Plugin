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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.AsyncDataCollector;
import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.Util;

public class RepatchCommand implements BaseCommand {

	@Override
	public boolean run(String[] args) {
		Message.sendFormattedSuccess(CommandManager.getSender(), "Attempting to patch the database...");
		AsyncDataCollector.dumpAll();
		StatsPlugin.setPaused(true);
		Bukkit.getScheduler().runTaskAsynchronously(StatsPlugin.getInstance(), new Runnable() {

			@Override
			public void run() {
				try { Database.getInstance().runPatch(true); }
				catch (Exception ex) { Message.sendFormattedError(CommandManager.getSender(), "Patch failed!"); }
				finally {
					for(Player player : Bukkit.getServer().getOnlinePlayers()) {
						if(!Util.isExempt(player)) AsyncDataCollector.get(player);
					}
					Message.sendFormattedSuccess(CommandManager.getSender(), "Patching finished.");
				}
			}
			
		});
		return true;
	}

	@Override
	public void getHelp() { Message.formatHelp("repatch", "", "Attempts to re-patch the database"); }

}
