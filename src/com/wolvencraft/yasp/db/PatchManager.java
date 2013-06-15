/*
 * PatchFetcher.java
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

package com.wolvencraft.yasp.db;

import java.io.File;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.util.Message;

/**
 * First copies the patch files from jar to the plugin directory. Then checks the download server for updates.
 * @author bitWolfy
 *
 */
public class PatchManager {

    public static final String PATCH_KEY = "yaspx";
    private static File patchDir;
    
    /**
     * <b>Default constructor</b><br />
     * Copies the patch files to the plugin directory
     */
    public PatchManager() {
        patchDir = new File(Statistics.getInstance().getDataFolder(), "patches");
        if(!patchDir.exists()) patchDir.mkdir();
        fetch(PATCH_KEY);
    }
    
    /**
     * Fetches the patch of the specified type
     * @param extension Patch extension
     */
    public static void fetch(String extension) {
        Message.log("+------ [ Fetching Patches ] ------+");
        int j = 1;
        while(Statistics.getInstance().getResource("patches/" + j + "." + extension + ".sql") != null) {
            if(localFileExists(j + "." + extension + ".sql")) { j++; continue; }
            Message.log("|" + Message.centerString("Copying " + j + "." + extension + ".sql", 34) + "|");
            Statistics.getInstance().saveResource("patches/" + j + "." + extension + ".sql", false);
            j++;
        }
        Message.log("|  All patch files are up to date  |");
        Message.log("+----------------------------------+");
    }
    
    /**
     * Checks if the file exists in the plugin directory
     * @param filename Name of the file (i.e. <code>1.yasp.sql</code>)
     * @return <b>true</b> if the file exists, <b>false</b> otherwise
     */
    private static boolean localFileExists(String filename) {
        return new File(patchDir.getAbsoluteFile() + "/" + filename).exists();
    }
    
}
