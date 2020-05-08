package com.example.carpoolgl.fragment.driver;

import android.content.Context;

import com.example.carpoolgl.base.fragment.baseFragPresenter;
import com.example.carpoolgl.fragment.passenger.passengerView;

public class driverPresenter extends baseFragPresenter<driverView> {
    private driverModel model;

    public driverPresenter(){
        this.model = new driverModel();
    }

    public void findDr_OrderInfo(Context context){
        this.model.findOrder(context,getView());
    }
}
