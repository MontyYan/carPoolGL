package com.example.carpoolgl.publishedOrder.driver;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carpoolgl.bean.RelPassMems;
import com.example.carpoolgl.recyclerView.passensRecycAdapter;

import java.util.List;

public class driverHandler extends Handler {

    private static final int EXIST_DATAS=1; //查到数据
    private static final int NO_DATAS=0;    //没有数据/查询出错
    private Context context;
    private TextView textView;
    private ProgressBar progress;
    private RecyclerView recyclerView;
    private List<RelPassMems> passenMems;


    public driverHandler(Context context, TextView textView, ProgressBar progress, RecyclerView recyclerView){
        this.context = context;
        this.textView = textView;
        this.progress = progress;
        this.recyclerView = recyclerView;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case EXIST_DATAS:
                textView.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                passenMems = (List<RelPassMems>)msg.obj;
                Log.i("driverOrderr_M4",passenMems.toString());
                passensRecycAdapter adapter = new passensRecycAdapter(this.context,passenMems);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(
                        this.context,
                        LinearLayoutManager.VERTICAL, //垂直方向
                        false             //非倒序
                ));
                textView.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                break;

            case NO_DATAS:


            default:
                break;
        }
    }


}
