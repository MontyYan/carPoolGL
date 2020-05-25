package com.example.carpoolgl.bean;

/*
* 默认地址类
* 设置默认地址信息，例如：学校、车站、家.....
* */
public class DefaultLoc {
    private Integer DeId;
    private String LocName;
    private mLonLat LonLat;

    public DefaultLoc(String locName, mLonLat lonLat) {
        LocName = locName;
        LonLat = lonLat;
    }

    public String getLocName() {
        return LocName;
    }

    public void setLocName(String locName) {
        LocName = locName;
    }

    public mLonLat getLonLat() {
        return LonLat;
    }

    public void setLonLat(mLonLat lonLat) {
        LonLat = lonLat;
    }
}
