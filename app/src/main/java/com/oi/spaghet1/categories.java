package com.oi.spaghet1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class categories {
    static public  Map<String, Collection<String>> map = new HashMap<String, Collection<String>>();
    static public void addSubcatToCat(String cat, String sub){
        Collection<String> values = map.get(cat);
        if (values == null) {
            values = new ArrayList<String>();
            map.put(cat, values);
        }
        values.add(sub);
    }
    static public void clearAllCategories(){
        map.clear();
    }
    static public Collection<String> getSubcat(String main){
        return map.get(main);
    }
    static public Map<String, Collection<String>>  getMap(){
        return map;
    }
}
