/* 
 * EntityCache.java
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

package com.mctrakr.cache;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.EntityType;

import com.mctrakr.database.Query;
import com.mctrakr.database.ConfigTables.EntitiesTable;
import com.mctrakr.managers.CacheManager.Type;

/**
 * Caches entity IDs server-side
 * @author bitWolfy
 *
 */
public class EntityCache extends CachedData {
    
    private static List<Integer> entities;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new List for data storage
     */
    public EntityCache() {
        super(Type.ENTITY, (long)(24 * 3600 * 20));
        entities = new ArrayList<Integer>();
    }
    
    @Override
    public void clearCache() {
        entities.clear();
    }
    
    /**
     * Parses the entity type and returns a valid entity ID
     * @param type Entity type
     * @return Entity ID
     */
    public static String parse(EntityType type) {
        int typeId = type.getTypeId();
        if(entities.contains(typeId)) return typeId + "";
        entities.add(typeId);
        if(!Query.table(EntitiesTable.TableName).condition(EntitiesTable.EntityId, typeId).exists()) {
            Query.table(EntitiesTable.TableName)
                 .value(EntitiesTable.EntityId, typeId)
                 .value(EntitiesTable.TpName, "custom_" + type.getName().toLowerCase().replace(" ", "_"))
                 .insert();
        }
        return typeId + "";
    }
    
}
