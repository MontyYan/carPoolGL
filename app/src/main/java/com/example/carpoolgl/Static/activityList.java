package com.example.carpoolgl.Static;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class activityList {
    private static Map<String,Activity> activityMap = new HashMap<>();
    public static void AddActivity(String activityName,Activity activity){
        activityMap.put(activityName,activity);
    }
    public static void DestoryActivity(String name){
        Set<String> key = activityMap.keySet();
        if(key.size()>0){
            for(String k:key){
                if(name.equals(k)){
                    if(activityMap.get(k)!=null)
                        activityMap.get(k).finish();
                }
            }
        }
    }

}
