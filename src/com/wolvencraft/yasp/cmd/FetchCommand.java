/*
 * FetchCommand.java
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

import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.PatchFetcher;

/**
 * Fetch command.<br />
 * Downloads the patch from the remote server
 * @author bitWolfy
 *
 */
public class FetchCommand implements BaseCommand {

    @Override
    public boolean run(final String[] args) {
        if(args.length != 1) {
            Message.sendFormattedError("Invalid parameter count");
            return false;
        }
        
        PatchFetcher.fetch(args[0]);
        
        Message.log("Downloaded " + args[0] + " from the remote server");
        return true;
    }

    @Override
    public void getHelp() {
        Message.formatHelp("fetch", "[id]", "Fetches the patch with the specified ID from the download server");
    }

}
