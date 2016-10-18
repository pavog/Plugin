package com.wolvencraft.yasp.util.serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor(access=AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
public class PartySerializable {
    
    private String partyName;
    private List<String> partyMembers;
    
}
