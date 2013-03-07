package com.wolvencraft.yasp.hooks;

import org.bukkit.entity.Player;

import com.gmail.nossr50.api.ExperienceAPI;
import com.wolvencraft.yasp.db.data.Settings;

public class McMMOHook implements PluginHook {
	
	public McMMOHook() {
		// Thanks to an awesome API, there is nothing to register here.
		Settings.setUsingMcMMO(true);
	}
	
	/**
	 * Get the amount of XP a player has in a specific skill.
	 * @param player The player to get XP for
	 * @param skillType The skill to get XP for
	 * @return the amount of XP in a given skill
	 */
	public static int getXP(Player player, String skillType) {
		return ExperienceAPI.getXP(player, skillType);
	}
	
	/**
	 * Get the amount of XP left before leveling up.
	 * @param player The player to get the XP amount for
	 * @param skillType The skill to get the XP amount for
	 * @return the amount of XP left before leveling up a specific skill
	 */
	public static int getXPToNextLevel(Player player, String skillType) {
		return ExperienceAPI.getXPToNextLevel(player, skillType);
	}
	
	/**
	 * Get the level a player has in a specific skill.
	 * @param player The player to get the level for
	 * @param skillType The skill to get the level for
	 * @return the level of a given skill
	 */
	public static int getLevel(Player player, String skillType) {
		return ExperienceAPI.getLevel(player, skillType);
	}
	
	/**
	 * Get the level cap of a specific skill.
	 * @param skillType The skill to get the level cap for
	 * @return the level cap of a given skill
	 */
	public static int getLevelCap(String skillType) {
		return ExperienceAPI.getLevelCap(skillType);
	}
	
	/**
	 * Gets the power level of a player.
	 * @param player The player to get the power level for
	 * @return the power level of the player
	 */
	public static int getPowerLevel(Player player) {
		return ExperienceAPI.getPowerLevel(player);
	}
	
	/**
	 * Get the power level cap.
	 * @return the power level cap of a given skill
	 */
	public static int getPowerLevelCap() {
		return ExperienceAPI.getPowerLevelCap();
	}
	
	@Override
	public Object getPluginInstance() {
		return null;
	}

	@Override
	public void disable() {
		// Nothing to disable
	}

}
