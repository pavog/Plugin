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

/**
 * First copies the patch files from jar to the plugin directory. Then checks the download server for updates.
 * @author bitWolfy
 *
 */
public class PatchFetcher {
    
    private static String downloadServer = "http://dl.wolvencraft.com/raw/Statistics/";
    private static File patchDir;
    
    /**
     * <b>Default constructor</b><br />
     * Copies the patch files to the plugin directory
     */
    public PatchFetcher() {
        patchDir = new File(Statistics.getInstance().getDataFolder(), "patches");
        if(!patchDir.exists()) patchDir.mkdir();
        Message.log("+-------] Fetching Patches [-------+");
        int j = 1;
        while(Statistics.getInstance().getResource("patches/" + j + ".yasp.sql") != null) {
            if(localFileExists(j + ".yasp.sql")) { j++; continue; }
            Message.log("|        Copying " + j + ".yasp.sql        |");
            Statistics.getInstance().saveResource("patches/" + j + ".yasp.sql", false);
            j++;
        }
        int i = 1;
        while(remoteFileExists(i + ".yasp.sql")) {
            if(localFileExists(i + ".yasp.sql")) { i++; continue; }
            Message.log("|      Downloading " + i + ".yasp.sql      |");
            try { download(i + ".yasp.sql"); }
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
        String urlString = downloadServer + filename;
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
        filename = downloadServer + filename;
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(filename).openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) { return false;}
    }
    
}
