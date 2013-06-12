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

package com.wolvencraft.yasp.util;

import java.io.File;

import com.wolvencraft.yasp.Statistics;

/**
 * First copies the patch files from jar to the plugin directory. Then checks the download server for updates.
 * @author bitWolfy
 *
 */
public class PatchManager {
    
    private static File patchDir;
    
    /**
     * <b>Default constructor</b><br />
     * Copies the patch files to the plugin directory
     */
    public PatchManager() {
        patchDir = new File(Statistics.getInstance().getDataFolder(), "patches");
        if(!patchDir.exists()) patchDir.mkdir();
        fetch(PatchType.YASPX);
    }
    
    /**
     * Fetches the patch of the specified type
     * @param type Patch type
     */
    public static void fetch(PatchType type) {
        Message.log("+------ [ Fetching Patches ] ------+");
        int j = 1;
        while(Statistics.getInstance().getResource("patches/" + j + "." + type.EXTENSION + ".sql") != null) {
            if(localFileExists(j + "." + type.EXTENSION + ".sql")) { j++; continue; }
            Message.log("|" + Message.centerString("Copying " + j + "." + type.EXTENSION + ".sql", 34) + "|");
            Statistics.getInstance().saveResource("patches/" + j + "." + type.EXTENSION + ".sql", false);
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
    
    /**
     * Specifies the different types of patches and their extensions
     * @author bitWolfy
     *
     */
    public enum PatchType {
        YASPX           ("yaspx"),
        
        AdminCmd        ("admincmd"),
        Awardments      ("awardments"),
        BanHammer       ("banhammer"),
        Citizens        ("citizens"),
        CommandBook     ("commandbook"),
        Factions        ("factions"),
        Jobs            ("jobs"),
        MCBans          ("mcbans"),
        MCMMO           ("mcmmo"),
        MobArena        ("mobarena"),
        PvpArena        ("pvparena"),
        Vanish          ("vanish"),
        Vault           ("vault"),
        Votifier        ("votifier"),
        WorldGuard      ("worldguard");
        
        public final String EXTENSION;
        
        /**
         * <b>Default constructor</b>
         * @param extension Patch extension
         */
        PatchType(String extension) {
            this.EXTENSION = extension;
        }
    }
    
}
