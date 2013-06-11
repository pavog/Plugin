/*
 * EffectsSerializable.java
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

import com.wolvencraft.yasp.util.ExceptionHandler;
import com.wolvencraft.yasp.util.Util;

/**
 * Provides means to serialize a <code>Collection&lt;PotionEffect&gt;</code> into a Json array
 * @author bitWolfy
 *
 */
@SuppressWarnings("unused")
public class EffectsSerializable {
    
    private int effect_id;
    private int time;
    
    /**
     * <b>Default constructor</b>
     * @param effect Potion effect type
     */
    private EffectsSerializable(PotionEffect effect) {
        effect_id = effect.getType().getId();
        time = effect.getDuration() / 20;
    }
    
    /**
     * Compresses a Collection into a single-line json array.<br />
     * Wraps around <code>Util.toJsonArray(List&lt;?&gt; source);</code><br />
     * Stores only potion ID and duration.
     * @param effects Effects to compress
     * @return String Json array
     */
    public static String serialize(Collection<PotionEffect> effects) {
        List<EffectsSerializable> potEffects = new ArrayList<EffectsSerializable>();
        for(PotionEffect eff : effects) {
            try { potEffects.add(new EffectsSerializable(eff)); }
            catch (Throwable t) {
                ExceptionHandler.handle(t, true);
                continue;
            }
        }
        return Util.toJsonArray(potEffects);
    }
}
