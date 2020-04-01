package com.example.carpoolgl.bean;

import java.util.Date;

public class orderBean {
    private String startLoc;
    private String endLoc;
    private String date;
    private Integer num;

    public orderBean(String startLoc, String endLoc, String date, Integer num) {
        this.startLoc = startLoc;
        this.endLoc = endLoc;
        this.date = date;
        this.num = num;
    }

    public String getStartLoc() {
        return startLoc;
    }

    public void setStartLoc(String startLoc) {
        this.startLoc = startLoc;
    }

    public String getEndLoc() {
        return endLoc;
    }

    public void setEndLoc(String endLoc) {
        this.endLoc = endLoc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
