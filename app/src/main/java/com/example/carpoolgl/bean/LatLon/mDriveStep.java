package com.example.carpoolgl.bean.LatLon;

import com.amap.api.services.core.LatLonPoint;

import java.util.ArrayList;
import java.util.List;

//未使用
public class mDriveStep {
    private List<LatLonPoint> stepList = new ArrayList<>();

    public List<LatLonPoint> getStepList() {
        return stepList;
    }

    public void setStepList(List<LatLonPoint> stepList) {
        this.stepList = stepList;
    }
}
