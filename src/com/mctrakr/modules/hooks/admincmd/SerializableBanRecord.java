/*
 * SerializableBanRecord.java
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

package com.mctrakr.modules.hooks.admincmd;

import java.util.ArrayList;
import java.util.List;

import com.mctrakr.util.Util;

@SuppressWarnings("unused")
public class SerializableBanRecord {
    
    private String issuer;
    private String reason;
    private long issue_time;
    private long expire_time;
    
    public SerializableBanRecord(String issuer, String reason, long issueTime, long expireTime) {
        this.issuer = issuer;
        this.reason = reason;
        this.issue_time = issueTime;
        this.expire_time = expireTime;
    }
    
    /**
     * Serializes a list of ban records into a Json array
     * @param origin Ban records
     * @return Json array
     */
    public static String serialize(List<SerializableBanRecord> origin) {
        return Util.toJsonArray(origin);
    }
    
    /**
     * Serializes a ban record into a Json array
     * @param origin Ban record
     * @return Json array
     */
    public static String serialize(SerializableBanRecord origin) {
        List<SerializableBanRecord> originList = new ArrayList<SerializableBanRecord>();
        originList.add(origin);
        return Util.toJsonArray(originList);
    }
    
}
