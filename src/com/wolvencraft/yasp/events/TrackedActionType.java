/*
 * StatisticsPlayerEvent.java
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

package com.wolvencraft.yasp.events;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PUBLIC)
public enum TrackedActionType {
    
    UNKNOWN             (0),
    
    LOGIN               (1),
    LOGOUT              (2),
    
    BLOCK_BREAK         (3, true),
    BLOCK_PLACE         (4, true),
    
    DEATH               (5, true),
    PVE                 (6, true),
    PVP                 (7),
    
    ITEM_DROP           (8, true),
    ITEM_PICKUP         (9, true),
    ITEM_USE            (10, true),
    ;
    
    private int typeId;                 // Action type ID
    private boolean complex;            // Does the action take properties, i.e. type of block, etc
    
    TrackedActionType(int typeId) {
        this.typeId = typeId;
        this.complex = false;
    }
    
    TrackedActionType(int typeId, boolean complex) {
        this.typeId = typeId;
        this.complex = complex;
    }
    
    public static TrackedActionType get(int typeId) {
        for(TrackedActionType type : TrackedActionType.values()) {
            if(type.getTypeId() == typeId) return type;
        }
        return TrackedActionType.UNKNOWN;
    }
    
}
