package com.wolvencraft.yasp.util.serializable;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access=AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
public class PartySerializable {
    
    private String partyName;
    private List<String> partyMembers;
    
}
