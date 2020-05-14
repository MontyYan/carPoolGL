package com.example.carpoolgl.util;

import java.util.HashMap;
import java.util.Map;

public class RegexUtil {

//    private static Map<String,String> map = new HashMap<>();
//    private static void init(){
//        map.put("桂林电子科技大学","桂电");
//        map.put("桂林师范大学","桂师");
//        map.put("桂林航天航空大学","桂航");
//        map.put("桂林理工大学","桂理");
//    }

    public static String regex(String origin,String key,String Township){
       String str = origin.replaceAll("广[^%]*"+key,"");
       str = str.replaceAll("桂林电子科技大学","桂电");
       str = str.replaceAll(Township,"");
       return key+" "+str;
    }
}
