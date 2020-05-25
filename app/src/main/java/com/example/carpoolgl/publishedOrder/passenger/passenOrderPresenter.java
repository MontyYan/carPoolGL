package com.example.carpoolgl.publishedOrder.passenger;

import android.app.Activity;

import com.example.carpoolgl.base.activity.basePresenter;
import com.example.carpoolgl.bean.PayOrder;
import com.example.carpoolgl.bean.payRequestInfo;

public class passenOrderPresenter extends basePresenter<passenOrderView> {
    private passenOrderModel model;

    public passenOrderPresenter(){
        this.model = new passenOrderModel();
    }

    public void getNewOrder(Activity activity,String seq,String passenSeq){
        this.model.getNewOrder(
                activity,
                seq,
                passenSeq,
                getView()
        );
    }

    public void payRequest(Activity activity, payRequestInfo payReInfo){
        this.model.payRequest(activity,payReInfo,getView());
    }

    public void payOrder(Activity activity, PayOrder payOrder){
        this.model.payInfo(activity,payOrder,getView());
    }

}
