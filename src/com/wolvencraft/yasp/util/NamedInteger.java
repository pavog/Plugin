/*
 * NamedInteger.java
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

package com.wolvencraft.yasp.util;

/**
 * Represents an integer that has a String attached to it
 * @author bitWolfy
 *
 */
public class NamedInteger {
    
    private String name;
    private String[] names;
    private Integer value;
    
    /**
     * <b>Default constructor</b><br />
     * Creates an empty NamedInteger with default data.
     */
    public NamedInteger() {
        name = "NO_NAME";
        value = -1;
        names = new String[] {"NO_NAME"};
    }
    
    /**
     * <b>Constructor</b><br />
     * Creates a NamedInteger with pre-defined data
     * @param name Integer name
     * @param value Integer value
     */
    public NamedInteger(String name, Integer value) {
        this.name = name;
        this.value = value;
        names = new String[] { name };
    }
    
    /**
     * Returns the name of the Integer
     * @return Name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the value of the Integer
     * @return Value
     */
    public Integer getValue() {
        return value;
    }
    
    /**
     * Returns the list of alternative names for the Integer
     * @return Names
     */
    public String[] getPossibleNames() {
        return names;
    }
    
    /**
     * Sets the name and value for the Integer
     * @param name New name
     * @param value New value
     */
    public void setData(String name, Integer value) {
        this.name = name;
        this.value = value;
    }
    
    /**
     * Sets the possible names for the Integer
     * @param names Possible names
     */
    public void setPossibleNames(String... names) {
        this.names = names;
    }
    
}
