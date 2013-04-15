/*
 * SignRefreshTask.java
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

package com.wolvencraft.yasp.util.tasks;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.Vector;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.Util;

/**
 * Handles all StatsSign functionality.
 * @author bitWolfy
 *
 */
public class SignRefreshTask implements Runnable {
    
    private static List<StatsSign> signs;
    private static SignRefreshTask instance;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a list of active signs and loads stored sign information from file
     */
    public SignRefreshTask() {
        instance = this;
        signs = new ArrayList<StatsSign>();
        File signFolder = new File(Statistics.getInstance().getDataFolder(), "signs");
        if (!signFolder.exists() || !signFolder.isDirectory()) signFolder.mkdir();
        else {
            File[] signFiles;
                signFiles = signFolder.listFiles(new FileFilter() {
                   public boolean accept(File file) { return file.getName().contains(".sign.yml"); }
                });
            for (File signFile : signFiles) {
                try {
                    FileConfiguration mineConf = YamlConfiguration.loadConfiguration(signFile);
                    Object sign = mineConf.get("StatsSign");
                    if (sign instanceof StatsSign) signs.add((StatsSign) sign);
                } catch (IllegalArgumentException ex) {
                    Message.log(Level.SEVERE, ex.getMessage());
                    continue;
                }
            }
        }
    }
    
    @Override
    public void run() {
        updateAll();
    }
    
    /**
     * Saves all sign information to file
     * @return <b>true</b> if all data was saved, <b>false</b> if an error occurred
     */
    public static boolean saveAll() {
        boolean result = true;
        for (StatsSign sign : signs) {
            if(!sign.saveFile()) result = false;
        }
        return result;
    }
    
    /**
     * Updates all signs with the valid variable information
     * @return <b>true</b> if all signs were updated, <b>false</b> if an error occurred
     */
    public static boolean updateAll() {
        boolean result = true;
        for(StatsSign sign : signs) {
            if(!sign.update()) result = false;
        }
        return result;
    }
    
    /**
     * Adds a new sign to the StatsSign update query
     * @param sign Sign to add
     * @return <b>true</b> if the sign was added successfully, <b>false</b> if an error occurred
     */
    public static boolean add(Sign sign) {
        return signs.add(instance.new StatsSign(sign));
    }
    
    /**
     * Removes the Sign from the StatsSign update query
     * @param sign Sign to remove
     * @return <b>true</b> if the sign was removed successfully, <b>false</b> if an error occurred
     */
    public static boolean remove(Sign sign) {
        Location loc = sign.getLocation();
        List<StatsSign> temp = new ArrayList<StatsSign>();
        for(StatsSign statsSign : signs) temp.add(statsSign);
        for(StatsSign statsSign : temp) {
            if(statsSign.getLocation().equals(loc)) {
                statsSign.deleteFile();
                return signs.remove(statsSign);
            }
        }
        return false;
    }
    
    /**
     * Checks if a certain Sign is in the StatsSign update query
     * @param sign Sign to check
     * @return <b>true</b> if the sign is in the update query, <b>false</b> otherwise
     */
    public static boolean isValid(Sign sign) {
        Location loc = sign.getLocation();
        for(StatsSign statsSign : signs) {
            if(loc.equals(statsSign.getLocation())) return true;
        }
        return false;
    }
    
    /**
     * Checks if the specified ID is unique
     * @param id ID to check
     * @return <b>true</b> if the ID is unique, <b>false</b> if there is a duplicate
     */
    private static boolean isUnique(String id) {
        for(StatsSign statsSign : signs) {
            if(statsSign.getId().equals(id)) return false;
        }
        return true;
    }
    
    /**
     * Generates a random unique 8-bit unique ID for signs.<br />
     * The IDs are checked to be unique.
     * @return Unique sign ID
     */
    private static String generateId() {
        String id = "";
        do { id = Long.toString(Math.abs(new Random().nextLong()), 8); }
        while (!isUnique(id));
        return id;
    }
    
    @SerializableAs("StatsSign")
    public class StatsSign implements ConfigurationSerializable  {
        private String signId;
        private Sign sign;
        private List<String> originalText;
        
        /**
         * <b>Default constructor</b><br />
         * Creates a new StatsSign instance from the Sign object
         * @param sign Sign to base the StatsSign on
         */
        public StatsSign(Sign sign) {
            this.signId = generateId();
            this.sign = sign;
            
            originalText = new ArrayList<String>();
            for(String line : sign.getLines()) { originalText.add(line); }
            
            saveFile();
        }
        
        /**
         * Constructor for de-serialization from a map
         * @param map Map to de-serialize from
         * @throws Exception 
         */
        @SuppressWarnings("unchecked")
        public StatsSign(Map<String, Object> map) throws Exception {
            signId = (String) map.get("id");
            
            World world = Bukkit.getWorld((String) map.get("world"));
            Block signBlock = world.getBlockAt(((Vector) map.get("loc")).toLocation(world));
            if(!(signBlock.getState() instanceof Sign)) {
                deleteFile();
                throw new Exception("No sign found at the stored location");
            }
            sign = (Sign) signBlock.getState();
            originalText = (List<String>) map.get("lines");
        }
        
        /**
         * Serialization method for sign data storage
         * @return Serialization map
         */
        public Map<String, Object> serialize() {
            Map<String, Object> me = new HashMap<String, Object>();
            me.put("id", signId);
            me.put("loc", sign.getLocation().toVector());
            me.put("world", sign.getLocation().getWorld().getName());
            me.put("lines", originalText);
            return me;
        }

        public String getId()             { return signId; }
        public Location getLocation()     { return sign.getLocation(); }
        public List<String> getLines()     { return originalText; }
        
        /**
         * Updates the StatsSign's lines with the appropriate variables
         * @return <b>true</b> if the update was successful, <b>false</b> otherwise
         */
        public boolean update() {
            BlockState b = sign.getBlock().getState();
            if(b instanceof Sign) {
                Sign signBlock = (Sign) b;
                for(int i = 0; i < originalText.size(); i++) { signBlock.setLine(i, Util.parseVars(originalText.get(i))); }
                signBlock.update();
                return true;
            }
            return false;
        }
        
        /**
         * Saves the sign data to file.
         * @return <b>true</b> if the save was successful, <b>false</b> if an error occurred
         */
        public boolean saveFile() {
            File signFile = new File(new File(Statistics.getInstance().getDataFolder(), "signs"), signId + ".sign.yml");
            FileConfiguration signConf =  YamlConfiguration.loadConfiguration(signFile);
            signConf.set("StatsSign", this);
            try {
                signConf.save(signFile);
            } catch (IOException e) {
                Message.log(Level.SEVERE, "Unable to serialize sign '" + signId + "'!");
                e.printStackTrace();
                return false;
            }
            return true;
        }
        
        /**
         * Deletes the sign data file.<br />
         * <b>Warning:</b> invoking this method will not remove the sign from the list of active signs
         * @return <b>true</b> if the deletion was successful, <b>false</b> if an error occurred
         */
        public boolean deleteFile() {
            File signFolder = new File(Statistics.getInstance().getDataFolder(), "signs");
            if(!signFolder.exists() || !signFolder.isDirectory()) return false;
            
            File[] signFiles = signFolder.listFiles(new FileFilter() {
                public boolean accept(File file) { return file.getName().contains(".sign.yml"); }
            });
            
            for(File signFile : signFiles) {
                if(signFile.getName().equals(signId + ".sign.yml")) {
                    return signFile.delete();
                }
            }
            return false;
        }
    }
}
