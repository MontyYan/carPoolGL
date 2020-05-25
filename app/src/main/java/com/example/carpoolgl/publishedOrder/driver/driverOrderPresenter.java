package com.example.carpoolgl.publishedOrder.driver;

import android.app.Activity;
import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.carpoolgl.base.activity.basePresenter;

public class driverOrderPresenter extends basePresenter<driverOrderView> {
    private driverOrderModel model;

    public driverOrderPresenter(){
        this.model = new driverOrderModel();
    }

    public void DrgetPassenInfo(Context context, TextView textView, ProgressBar progressBar, RecyclerView recyclerView,String seq){
        this.model.getPassenInfo(context,
                textView,
                progressBar,
                recyclerView,
                getView(),
                seq);

    }

    public void DrUpdateOrderInfo(Activity activity, String seq){
        this.model.getOrderInfo(activity,
                seq,
                getView());
    }
}
