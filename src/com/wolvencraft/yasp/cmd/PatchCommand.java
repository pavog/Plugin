/*
 * PatchCommand.java
 * 
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
import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.settings.Constants.StatPerms;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.cache.OnlineSessionCache;
import com.wolvencraft.yasp.util.tasks.DatabaseTask;

/**
 * Patch command.<br />
 * Executes the specified patch
 * @author bitWolfy
 *
 */
public class PatchCommand implements BaseCommand {

    @Override
    public boolean run(final String[] args) {
        if(args.length != 1) {
            Message.sendFormattedError("Invalid parameter count");
            return false;
        }
        
        Message.sendFormattedSuccess(CommandManager.getSender(), "Attempting to patch the database (" + args[0] + ")");
        Statistics.setPaused(true);
        Bukkit.getScheduler().runTaskAsynchronously(Statistics.getInstance(), new Runnable() {

            @Override
            public void run() {
                try { Database.executePatch(args[0]); }
                catch (Exception ex) { Message.sendFormattedError(CommandManager.getSender(), "Patch failed!"); }
                finally {
                    for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                        if(StatPerms.Statistics.has(player)) OnlineSessionCache.fetch(player);
                    }
                    DatabaseTask.getStats().pushStaticData();
                    Message.sendFormattedSuccess(CommandManager.getSender(), "Patching finished.");
                    Statistics.setPaused(false);
                }
            }
            
        });
        return true;
    }

    @Override
    public void getHelp() {
        Message.formatHelp("patch", "[id]", "Exectutes a database patch with the specified ID");
    }

}
