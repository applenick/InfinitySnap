package com.applenick.InfinitySnap.snap;

import java.util.List;

import com.applenick.InfinitySnap.command.exceptions.UnknownSnapTypeException;
import com.google.common.collect.Lists;

public enum SnapType {
    KICK,
    BAN,
    BROADCAST;
    
    public static List<String> getTabOptions(){
        List<String> options = Lists.newArrayList();
        
        for(SnapType t : SnapType.values()) {
            options.add(t.toString().toLowerCase());
        }
        
        return options;
    }
        
    public static SnapType parseConfig(String value) throws UnknownSnapTypeException {
        if(value.toLowerCase().startsWith("k")) {
            return KICK;
        }else if(value.toLowerCase().startsWith("ba")) {
            return BAN;
        }else if(value.toLowerCase().startsWith("br") || value.toLowerCase().startsWith("m")) {
            return BROADCAST;
        }else {
        	throw new UnknownSnapTypeException(value);
        }
    }
}
