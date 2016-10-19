/*
 * Statistics Bukkit Plugin
 *
 * V2 Copyright (c) 2016 Paul <pavog> Vogel <http://www.paulvogel.me> and contributors.
 * V1 Copyright (c) 2016 bitWolfy <http://www.wolvencraft.com> and contributors.
 * Contributors are: Mario <MarioG1> Gallaun, Christian <Dazzl> Swan, Cory <Coryf88> Finnestad, Crimsonfoxy
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.wolvencraft.yasp.util.serializable;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.wolvencraft.yasp.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Requires <b>WorldGuardHook</b><br />
 * Stores all flags from the ApplicableRegionSet in a Json array
 *
 * @author bitWolfy
 */
@SuppressWarnings("unused")
public class FlagsSerializable {

    private String flag;
    private Object value;


    private FlagsSerializable(String flag, Object value) {
        this.flag = flag;
        this.value = value;
    }

    /**
     * Fetches the flags from the ApplicableRegionSet and stores them in a Json array.<br />
     * Wraps around <code>Util.toJsonArray(List&lt;?&gt; source);</code>
     *
     * @param set Applicable region set
     * @return Json array
     */
    public static String serialize(ApplicableRegionSet set) {
        List<FlagsSerializable> flags = new ArrayList<FlagsSerializable>();
        for (Flag<?> flag : DefaultFlag.flagsList) {
            flags.add(new FlagsSerializable(flag.getName(), set.getFlag(flag)));
        }
        return Util.toJsonArray(flags);
    }

}
