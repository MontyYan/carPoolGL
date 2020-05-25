package com.example.carpoolgl.NaviDriving.overOrder;

import android.app.Activity;

import com.example.carpoolgl.base.activity.basePresenter;

public class OverOrderPresenter extends basePresenter<OverOrderView> {

    private OverOrderModel model;

    public OverOrderPresenter(){
        this.model = new OverOrderModel();
    }

    public void OverOrder(Activity activity,String OrderSeq){
        this.model.FinishOrder(activity,OrderSeq,getView());



    }
}
