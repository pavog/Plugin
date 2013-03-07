package com.wolvencraft.yasp;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.wolvencraft.yasp.util.Message;

public class DisplaySignFactory implements Runnable {
	
	public DisplaySignFactory() {
		signs = loadAll();
	}
	
	private static List<DisplaySign> signs;
	
	@Override
	public void run() {
		updateAll();
	}
	
	/**
	 * Saves all the sign data to disc
	 */
	public static void saveAll() {
		for (DisplaySign sign : signs) sign.saveFile();
	}
	
	/**
	 * Loads the sign data from disc
	 * @return Loaded list of signs
	 */
	public static List<DisplaySign> loadAll() {
		List<DisplaySign> signs = new ArrayList<DisplaySign>();
		File signFolder = new File(StatsPlugin.getInstance().getDataFolder(), "signs");
		if (!signFolder.exists() || !signFolder.isDirectory()) {
			signFolder.mkdir();
			return signs;
		}
		File[] signFiles;
			signFiles = signFolder.listFiles(new FileFilter() {
			   public boolean accept(File file) { return file.getName().contains(".sign.yml"); }
			});
		
		for (File signFile : signFiles) {
			try {
				FileConfiguration mineConf = YamlConfiguration.loadConfiguration(signFile);
				Object sign = mineConf.get("displaysign");
				if (sign instanceof DisplaySign) signs.add((DisplaySign) sign);
			} catch (IllegalArgumentException ex) {
				Message.log(Level.SEVERE, ex.getMessage());
				continue;
			}
		}
		return signs;
	}
	
	/**
	 * Returns the list of signs in the query
	 * @return List of DisplaySigns
	 */
	public static List<DisplaySign> getSigns() {
		List<DisplaySign> temp = new ArrayList<DisplaySign>();
		for(DisplaySign sign : signs) temp.add(sign);
		return temp;
	}
	
	/**
	 * Adds a display sign to the refresh query
	 * @param sign Sign to add
	 * @return <b>true</b> if the sign was added, <b>false</b> if an error occurred
	 */
	public static boolean addSign(DisplaySign sign) {
		return signs.add(sign);
	}
	
	/**
	 * Removes a diplay sign from the refresh query
	 * @param sign Sign to remove
	 * @return <b>true</b> if the sign was removed, <b>false</b> if an error occurred
	 */
	public static boolean removeSign(DisplaySign sign) {
		return signs.remove(sign);
	}
	
	/**
	 * Checks if a DisplaySign exists at the specified location
	 * @param loc Location to check
	 * @return <b>true</b> if there is an initialized DisplaySign at the location, <b>false</b> otherwise
	 */
	public static boolean exists(Location loc) {
		for(DisplaySign sign : signs) { if(sign.getLocation().equals(loc)) return true; }
		return false;
	}
	
	/**
	 * Checks if a DisplaySign with the specified ID exists
	 * @param id ID to check
	 * @return <b>true</b> if there is a DisplaySign with the specified, <b>false</b> otherwise
	 */
	public static boolean exists(String id) {
		for(DisplaySign sign : signs) { if(sign.getId().equals(id)) return true; }
		return false;
	}
	
	/**
	 * Returns the DisplaySign at the following location, if it exists
	 * @param loc Location to check
	 * @return <b>DisplaySign</b>, if there is one at the specified location, <b>null</b> otherwise
	 */
	public static DisplaySign get(Location loc) {
		for(DisplaySign sign : signs) { if(sign.getLocation().equals(loc)) return sign; }
		return null;
	}
	
	/**
	 * Returns the DisplaySign associated with the specified Sign object
	 * @param sign Sign to check
	 * @return <b>DisplaySign</b>, if one is associated with the specified Sign, <b>null</b> otherwise
	 */
	public static DisplaySign get(Sign sign) { return get(sign.getLocation()); }
	
	/**
	 * Returns the DisplaySign with the specified ID, if there is one
	 * @param id ID to check
	 * @return <b>DisplaySign</b>, if there is one with the specified ID, <b>null</b> otherwise
	 */
	public static DisplaySign get(String id) { 
		for(DisplaySign sign : signs) { if(sign.getId().equals(id)) return sign; }
		return null;
	}
	
	/**
	 * Updates all the DisplaySigns with appropriate variables
	 */
	public static void updateAll() {
		for(DisplaySign sign : signs) sign.update();
	}
}
