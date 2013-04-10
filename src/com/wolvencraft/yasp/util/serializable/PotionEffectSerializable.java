/*
 * PotionEffectSerializable.java
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

package com.wolvencraft.yasp.util.serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.potion.PotionEffect;

import com.wolvencraft.yasp.util.Util;

/**
 * Simple class intended to temporarily store basic information about a potion effect
 * @author bitWolfy
 *
 */
@SuppressWarnings("unused")
public class PotionEffectSerializable {
    
    private int effect_id;
    private int time;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new SimplePotionEffect based on a PotionEffect provided
     * @param effect
     */
    private PotionEffectSerializable(PotionEffect effect) {
        effect_id = effect.getType().getId();
        time = effect.getDuration() / 20;
    }
    
    /**
     * Compresses a Collection into a single-line json array.<br />
     * Wraps around <code>Util.toJsonArray(List&lt;?&gt; source);</code><br />
     * Stores only potion ID and duration.
     * @param effects Effects to compress
     * @return String json array
     */
    public static String toJsonArray(Collection<PotionEffect> effects) {
        List<PotionEffectSerializable> potEffects = new ArrayList<PotionEffectSerializable>();
        for(PotionEffect eff : effects) potEffects.add(new PotionEffectSerializable(eff));
        return Util.toJsonArray(potEffects);
    }
}