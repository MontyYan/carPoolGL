package com.example.carpoolgl.util;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;

public class RouteUtil {

    public static DrivePath getDrivePath(String pathJson){
        DrivePath path = JSONObject.parseObject(pathJson,DrivePath.class);
        return path;
    }

    public static LatLonPoint getLatLon(String LatLonJson){
        LatLonPoint point = JSONObject.parseObject(LatLonJson,LatLonPoint.class);
        return point;
    }

    public static LatLng PointToLL(LatLonPoint point){
        return new LatLng(point.getLatitude(),point.getLongitude());
    }

    public static NaviLatLng PointToNaviLL(LatLonPoint point){
        return new NaviLatLng(point.getLatitude(),point.getLongitude());
    }
}
