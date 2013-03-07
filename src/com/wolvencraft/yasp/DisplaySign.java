package com.wolvencraft.yasp;

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

import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.Util;

/**
 * A virtual representation of the dynamically-updated signs that are used to display information about mines
 * @author bitWolfy
 *
 */
@SerializableAs("DisplaySign")
public class DisplaySign implements ConfigurationSerializable  {
	private String signId;
	private Sign sign;
	private List<String> originalText;
	
	/**
	 * Standard constructor for new signs
	 * @param sign Sign object
	 */
	public DisplaySign(Sign sign) {
		this.signId = generateId();
		this.sign = sign;
		
		originalText = new ArrayList<String>();
		for(String line : sign.getLines()) { originalText.add(line); }
		
		saveFile();
	}
	
	/**
	 * Constructor for deserialization from a map
	 * @param map Map to deserialize from
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public DisplaySign(Map<String, Object> me) throws Exception {
		signId = (String) me.get("id");
		
		World world = Bukkit.getWorld((String) me.get("world"));
		Block signBlock = world.getBlockAt(((Vector) me.get("loc")).toLocation(world));
		if(!(signBlock.getState() instanceof Sign)) {
			deleteFile();
			throw new Exception("No sign found at the stored location");
		}
		sign = (Sign) signBlock.getState();
		originalText = (List<String>) me.get("lines");
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

	public String getId() 			{ return signId; }
	public Location getLocation() 	{ return sign.getLocation(); }
	
	public List<String> getLines() 	{ return originalText; }
	
	/**
	 * Updates the DisplaySign's lines with the appropriate variables
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
		File signFile = new File(new File(StatsPlugin.getInstance().getDataFolder(), "signs"), signId + ".sign.yml");
		FileConfiguration signConf =  YamlConfiguration.loadConfiguration(signFile);
		signConf.set("displaysign", this);
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
		File signFolder = new File(StatsPlugin.getInstance().getDataFolder(), "signs");
		if(!signFolder.exists() || !signFolder.isDirectory()) return false;
		
		File[] signFiles = signFolder.listFiles(new FileFilter() {
			public boolean accept(File file) { return file.getName().contains(".sign.yml"); }
		});
		
		for(File signFile : signFiles) {
			if(signFile.getName().equals(signId+ ".sign.yml")) {
				DisplaySignFactory.removeSign(this);
				return signFile.delete();
			}
		}
		return false;
	}
	
	/**
	 * Creates a pseudo-random ID with a 32-bit key. ID is guaranteed to be unique
	 * @return <b>String</b> random ID
	 */
	private static String generateId() {
		boolean unique = false;
		String id = "";
		do {
			id = Long.toString(Math.abs(new Random().nextLong()), 32);
			if(!DisplaySignFactory.exists(id)) unique = true;
		} while (!unique);
		return Long.toString(Math.abs(new Random().nextLong()), 32);
	}
}
