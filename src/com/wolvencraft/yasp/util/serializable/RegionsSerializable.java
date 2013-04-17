package com.wolvencraft.yasp.util.serializable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.wolvencraft.yasp.util.Util;

public class RegionsSerializable {
    
    String region_name;
    int priority;
    
    private RegionsSerializable(String regionName, int priority) {
        this.region_name = regionName;
        this.priority = priority;
    }
    
    public static String serialize(Map<String, Integer> regions) {
        List<RegionsSerializable> values = new LinkedList<RegionsSerializable>();
        Iterator<Entry<String, Integer>> it = regions.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>) it.next();
            values.add(new RegionsSerializable(pairs.getKey(), pairs.getValue()));
            it.remove();
        }
        return Util.toJsonArray(values);
    }
    
}
