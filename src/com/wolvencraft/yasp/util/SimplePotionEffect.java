/*
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

import org.bukkit.potion.PotionEffect;

/**
 * Simple class intended to temporarily store basic information about a potion effect
 * @author bitWolfy
 *
 */
public class SimplePotionEffect {
	
	/**
	 * <b>Default constructor</b><br />
	 * Creates a new SimplePotionEffect based on a PotionEffect provided
	 * @param effect
	 */
	public SimplePotionEffect(PotionEffect effect) {
		this.id = effect.getType().getId();
		this.time = effect.getDuration() / 20;
	}
	
	private int id;
	private int time;
	
	/**
	 * Returns the ID of the effect
	 * @return Effect ID
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Returns the duration (in seconds) of the effect
	 * @return Effect duration
	 */
	public int getDuration() {
		return time;
	}
}
