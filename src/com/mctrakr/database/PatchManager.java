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

package com.mctrakr.database;

import java.io.File;

import com.mctrakr.Statistics;
import com.mctrakr.util.Message;

/**
 * First copies the patch files from jar to the plugin directory. Then checks the download server for updates.
 * @author bitWolfy
 *
 */
public class PatchManager {

    public static final String PATCH_KEY = "yaspx";
    private static File patchDir = new File(Statistics.getInstance().getDataFolder(), "patches");
    static {
        if(!patchDir.exists()) patchDir.mkdir();
    }
    
    private PatchManager() { }
    
    /**
     * Fetches the patch of the specified type
     * @param extension Patch extension
     */
    public static void fetch(String extension) {
        int i = 1;
        while(Statistics.getInstance().getResource("patches/" + i + "." + extension + ".sql") != null) {
            if(localFileExists(i + "." + extension + ".sql")) {
                i++;
                continue;
            }
            Message.log("|  |-- Copying " + Message.fillString(i + "." + extension + ".sql", 26) + "|");
            Statistics.getInstance().saveResource("patches/" + i + "." + extension + ".sql", false);
            i++;
        }
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
