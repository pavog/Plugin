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

import com.wolvencraft.yasp.util.Util;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class BanRecordSerializable {

    private String issuer;
    private String reason;
    private long issue_time;
    private long expire_time;

    public BanRecordSerializable(String issuer, String reason, long issueTime, long expireTime) {
        this.issuer = issuer;
        this.reason = reason;
        this.issue_time = issueTime;
        this.expire_time = expireTime;
    }

    /**
     * Serializes a list of ban records into a Json array
     *
     * @param origin Ban records
     * @return Json array
     */
    public static String serialize(List<BanRecordSerializable> origin) {
        return Util.toJsonArray(origin);
    }

    /**
     * Serializes a ban record into a Json array
     *
     * @param origin Ban record
     * @return Json array
     */
    public static String serialize(BanRecordSerializable origin) {
        List<BanRecordSerializable> originList = new ArrayList<BanRecordSerializable>();
        originList.add(origin);
        return Util.toJsonArray(originList);
    }

}
