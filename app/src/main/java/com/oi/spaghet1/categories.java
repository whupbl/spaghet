package com.oi.spaghet1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class categories {
    public static Map<String, Collection<String>> map = new HashMap<String, Collection<String>>();
    static public void addSubcatToCat(String main, String sub){
        Collection<String> values = map.get(main);
        if (values==null) {
            values = new ArrayList<String>();
            map.put(main, values);
        }
        values.add(sub);
    }
    static public Collection<String> getSubcat(String main){
        return map.get(main);
    }
    static public Map<String, Collection<String>>  getMap(){
        return map;
    }
}
