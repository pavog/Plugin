/*
 * PauseCommand.java
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
import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.util.Message;

/**
 * Pause command.<br />
 * Effectively pauses database synchronization. Does not pause data collection.
 * @author bitWolfy
 *
 */
public class PauseCommand implements BaseCommand {

    @Override
    public boolean run(String[] args) {
        if(Statistics.getPaused()) {
            Statistics.setPaused(false);
            Message.send(CommandManager.getSender(), "Data collection is unpaused");
        } else {
            Statistics.setPaused(true);
            Message.send(CommandManager.getSender(), "Data collection is paused");
        }
        return true;
    }

    @Override
    public void getHelp() {
        Message.formatHelp("pause", "", "Toggles the data collection on and off");
    }

}
