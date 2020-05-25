package com.example.carpoolgl.bean;

import java.util.Date;

/*
* 最近搜索地址类，存储用户最近搜索过的地点
* */
public class RecentLoc {
    private Integer RecId;
    private String LocName;
    private mLonLat LonLat;
    private Date OpTime;

    public RecentLoc(String locName, mLonLat lonLat, Date opTime) {
        LocName = locName;
        LonLat = lonLat;
        OpTime = opTime;
    }

    public Integer getRecId() {
        return RecId;
    }

    public void setRecId(Integer recId) {
        RecId = recId;
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

    public Date getOpTime() {
        return OpTime;
    }

    public void setOpTime(Date opTime) {
        OpTime = opTime;
    }
}
