package com.example.carpoolgl.publish;

import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.carpoolgl.base.activity.basePresenter;
import com.example.carpoolgl.bean.RelOrder;

public class publishPresenter extends basePresenter<publishView> {
    private publishModel model;

    public publishPresenter(){
        this.model = new publishModel();
    }

    public void publishing(Context context, RelOrder order, TextView text, ProgressBar progress, CardView card,int id){
        this.model.publish(
                context,
                order,
                text,
                progress,
                card,
                getView(),
                id
        );

    }



//    public void publishing_(RelOrder order, LinearLayout linear, CardView card){
//        this.model.publish(
//                order,
//                linear,
//                card,
//                getView()
//        );
//
//    }
}
