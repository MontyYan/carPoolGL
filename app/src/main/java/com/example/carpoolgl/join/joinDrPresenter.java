package com.example.carpoolgl.join;

import android.app.Activity;

import com.example.carpoolgl.base.activity.basePresenter;
import com.example.carpoolgl.bean.CarInfo;

public class joinDrPresenter extends basePresenter<joinDrView> {
    private joinDrModel model;

    public joinDrPresenter(){
        this.model = new joinDrModel();

    }

    public void joinDriver(Activity activity, CarInfo carInfo){
        this.model.JoinDriver(activity,carInfo,getView());

    }

}
