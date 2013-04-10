/*
 * SyncCommand.java
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

import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.DatabaseTask;
import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.StatsSignFactory;

/**
 * Synchronization command.<br />
 * Forces an immediate database synchronization.
 * @author bitWolfy
 *
 */
public class SyncCommand implements BaseCommand {

    @Override
    public boolean run(String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(Statistics.getInstance(), new Runnable() {

            @Override
            public void run() {
                DatabaseTask.commit();
            }
            
        });
        StatsSignFactory.updateAll();
        Message.sendFormattedSuccess(CommandManager.getSender(), "Synchronization complete");
        return true;
    }

    @Override
    public void getHelp() { Message.formatHelp("sync", "", "Forces the plugin to push data to the database"); }

}
