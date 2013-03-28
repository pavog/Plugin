/*
 * ReconnectCommand.java
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

import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.util.Message;

/**
 * Reconnect command.<br />
 * Attempts to reconnect to the database.
 * @author bitWolfy
 *
 */
public class ReconnectCommand implements BaseCommand {

    @Override
    public boolean run(String[] args) {
        try {
            Database.getInstance().reconnect();
            Message.sendFormattedSuccess(CommandManager.getSender(), "Re-established the database connection");
            return true;
        } catch (Exception ex) {
            Message.sendFormattedError(CommandManager.getSender(), "An error occurred while reconnecting to the database");
            return false;
        }
    }

    @Override
    public void getHelp() { Message.formatHelp("reconnect", "", "Attempts to reconnect to the database"); }

}
