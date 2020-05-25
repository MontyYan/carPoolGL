package com.example.carpoolgl.findOrders.passenger;

import android.app.Activity;
import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.carpoolgl.base.activity.basePresenter;
import com.example.carpoolgl.bean.RelOrder;
import com.example.carpoolgl.bean.RelPassMems;

public class fordersPresenter extends basePresenter<fordersView> {
    private static final String TAG="findOrder_P";
    private fordersModel model;
    public fordersPresenter(){
        this.model = new fordersModel();
    }

//    public void findOrder(RelOrder order){
//        Log.i(TAG,order.toString());
//        this.model.findOrder(getView(),order);
//    }

//    public void findOrder(Context context, TextView textView, ProgressBar progress, RecyclerView recyclerView, RelOrder order){
//        this.model.findOrder(context,
//                textView,
//                progress,
//                recyclerView,
//                getView(),
//                order);
//    }

    public void findOrder(Context context, RelOrder order){
        this.model.findOrder(
                context,
                getView(),
                order);
    }

    public void JoinOrder(Activity activity, RelPassMems mems){
        this.model.joinOrder(activity,mems,getView());
    }
}
