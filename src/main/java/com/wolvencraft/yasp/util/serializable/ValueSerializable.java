package com.wolvencraft.yasp.util.serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access=AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
public class ValueSerializable {
    
    private String key;
    private Object value;
    
}
