package com.example.carpoolgl.publish;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.carpoolgl.base.basePresenter;
import com.example.carpoolgl.bean.RelOrder;

public class publishPresenter extends basePresenter<publishView> {
    private publishModel model;

    public publishPresenter(){
        this.model = new publishModel();
    }

    public void publishing(RelOrder order, TextView text, ProgressBar progress, CardView card){
        this.model.publish(
                order,
                text,
                progress,
                card,
                getView()
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
