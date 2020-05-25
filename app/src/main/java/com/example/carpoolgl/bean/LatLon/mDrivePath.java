package com.example.carpoolgl.bean.LatLon;

import java.util.ArrayList;
import java.util.List;

//未使用
public class mDrivePath {
    List<mDriveStep> stepList = new ArrayList<>();

    public List<mDriveStep> getStepList() {
        return stepList;
    }

    public void setStepList(List<mDriveStep> stepList) {
        this.stepList = stepList;
    }
}
