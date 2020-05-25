package com.example.carpoolgl.findOrders.driver;

import android.app.Activity;

import com.example.carpoolgl.base.activity.basePresenter;

public class DrfindOrderPresenter extends basePresenter<DrfindOrderView> {
    private DrfindOrderModel model;

    public DrfindOrderPresenter(){
        this.model = new DrfindOrderModel();

    }

    public void findOrders(Activity activity){
        this.model.findOrders(activity,getView());
    }
}
