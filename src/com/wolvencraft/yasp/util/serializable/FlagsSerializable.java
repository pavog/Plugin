package com.wolvencraft.yasp.util.serializable;

import java.util.ArrayList;
import java.util.List;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.wolvencraft.yasp.util.Util;

@SuppressWarnings("unused")
public class FlagsSerializable {
    
    private String flag;
    private Object value;
    
    
    private FlagsSerializable(String flag, Object value) {
        this.flag = flag;
        this.value = value;
    }
    
    public static String serialize(ApplicableRegionSet set) {
        List<FlagsSerializable> flags = new ArrayList<FlagsSerializable>();
        for(Flag<?> flag : DefaultFlag.flagsList) {
            flags.add(new FlagsSerializable(flag.getName(), set.getFlag(flag)));
        }
        return Util.toJsonArray(flags);
    }
    
}
