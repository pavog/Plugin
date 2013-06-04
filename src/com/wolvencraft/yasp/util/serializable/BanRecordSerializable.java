package com.wolvencraft.yasp.util.serializable;

import java.util.ArrayList;
import java.util.List;

import com.wolvencraft.yasp.util.Util;

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
     * @param origin Ban records
     * @return Json array
     */
    public static String serialize(List<BanRecordSerializable> origin) {
        return Util.toJsonArray(origin);
    }
    
    /**
     * Serializes a ban record into a Json array
     * @param origin Ban record
     * @return Json array
     */
    public static String serialize(BanRecordSerializable origin) {
        List<BanRecordSerializable> originList = new ArrayList<BanRecordSerializable>();
        originList.add(origin);
        return Util.toJsonArray(originList);
    }
    
}
