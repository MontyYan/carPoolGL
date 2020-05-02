package com.example.carpoolgl.findOrders;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.carpoolgl.base.basePresenter;
import com.example.carpoolgl.bean.RelOrder;

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

    public void findOrder(Context context, TextView textView, ProgressBar progress, RecyclerView recyclerView, RelOrder order){
        this.model.findOrder(context,
                textView,
                progress,
                recyclerView,
                getView(),
                order);
    }
}
