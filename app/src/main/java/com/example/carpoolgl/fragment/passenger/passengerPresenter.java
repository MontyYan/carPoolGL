package com.example.carpoolgl.fragment.passenger;

import android.content.Context;

import com.example.carpoolgl.base.fragment.baseFragPresenter;

public class passengerPresenter extends baseFragPresenter<passengerView> {
    private passengerModel model;

    public passengerPresenter(){
        this.model = new passengerModel();
    }

    public void findPa_OrderInfo(Context context){
//        passengerView view = getView();
//        this.model.findUInfo(context,view);
        this.model.findOrder(context,getView());
    }
}
