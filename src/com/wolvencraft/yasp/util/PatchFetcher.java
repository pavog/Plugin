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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.Settings.LocalConfiguration;

/**
 * First copies the patch files from jar to the plugin directory. Then checks the download server for updates.
 * @author bitWolfy
 *
 */
public class PatchFetcher {
    
    private static File patchDir;
    
    /**
     * <b>Default constructor</b><br />
     * Copies the patch files to the plugin directory
     */
    public PatchFetcher() {
        patchDir = new File(Statistics.getInstance().getDataFolder(), "patches");
        if(!patchDir.exists()) patchDir.mkdir();
        fetch(PatchType.YASP);
    }
    
    /**
     * Fetches the patch of the specified type
     * @param type Patch type
     */
    public static void fetch(PatchType type) {
        Message.log("+-------] Fetching Patches [-------+");
        int j = 1;
        while(Statistics.getInstance().getResource("patches/" + j + "." + type.extension + ".sql") != null) {
            if(localFileExists(j + "." + type.extension + ".sql")) { j++; continue; }
            Message.log("|" + Message.centerString("Copying " + j + "." + type.extension + ".sql", 34) + "|");
//            Message.log("|       Copying " + j + "." + type.extension + ".sql        |");
            Statistics.getInstance().saveResource("patches/" + j + "." + type.extension + ".sql", false);
            j++;
        }
        int i = 1;
        while(remoteFileExists(i + "." + type.extension + ".sql")) {
            if(localFileExists(i + "." + type.extension + ".sql")) { i++; continue; }
            Message.log("|" + Message.centerString("Downloading " + i + "." + type.extension + ".sql", 34) + "|");
//            Message.log("|      Downloading " + i + "." + type.extension + ".sql      |");
            try { download(i + "." + type.extension + ".sql"); }
            catch (MalformedURLException e) {
                Message.log("Downloaded " + i + " patch files");
                break;
            } catch (IOException e) {
                Message.log("An error occurred while downloading patch files");
                return;
            }
            i++;
        }
        Message.log("|  All patch files are up to date  |");
        Message.log("+----------------------------------+");
    }
    
    /**
     * Downloads a file from the download server
     * @param filename Name of the file (i.e. <code>1.yasp.sql</code>)
     * @throws MalformedURLException Thrown if the file does not exist
     * @throws IOException Thrown if an error occurred while downloading the file
     */
    private static void download(String filename) throws MalformedURLException, IOException {
        String urlString = LocalConfiguration.PatchServer.asString() + filename;
        BufferedInputStream inputStream = null;
        FileOutputStream fileOut = null;
        try {
            inputStream = new BufferedInputStream(new URL(urlString).openStream());
            fileOut = new FileOutputStream(patchDir + "/" + filename);
            
            byte data[] = new byte[1024];
            int count;
            while ((count = inputStream.read(data, 0, 1024)) != -1) {
                fileOut.write(data, 0, count);
            }
        } finally {
            if (inputStream != null) inputStream.close();
            if (fileOut != null) fileOut.close();
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
    
    /**
     * Checks if the file exists on the download server
     * @param filename Name of the file (i.e. <code>1.yasp.sql</code>)
     * @return <b>true</b> if the file exists, <b>false</b> otherwise
     */
    private static boolean remoteFileExists(String filename) {
        filename = LocalConfiguration.PatchServer.asString() + filename;
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(filename).openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) { return false;}
    }
    
    /**
     * Specifies the different types of patches and their extensions
     * @author bitWolfy
     *
     */
    public enum PatchType {
        YASP("yasp"),
        Vault("vault"),
        WorldGuard("wg"),
        Factions("factions");
        
        private String extension;
        
        /**
         * <b>Default constructor</b>
         * @param extension Patch extension
         */
        PatchType(String extension) {
            this.extension = extension;
        }
    }
    
}
