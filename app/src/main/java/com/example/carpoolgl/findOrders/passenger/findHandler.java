package com.example.carpoolgl.findOrders.passenger;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.example.carpoolgl.Dr_OrderDetailActivity;
import com.example.carpoolgl.bean.RelOrder;
import com.example.carpoolgl.recyclerView.ordersRecycAdapter;
import com.example.carpoolgl.route.RouteActivity;
import com.example.carpoolgl.util.RouteUtil;

import java.util.List;

/*
* 用于更新findorder页面ui
* */
public class findHandler extends Handler {

    private static final int EXIST_DATAS=1; //查到数据
    private static final int NO_DATAS=0;    //没有数据/查询出错
    private Context context;
    private TextView textView;
    private ProgressBar progress;
    private RecyclerView recyclerView;
    private List<RelOrder> orderList;

    public findHandler(){
    }


    public findHandler(Context context,TextView textView,ProgressBar progress,RecyclerView recyclerView){
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
                orderList = (List<RelOrder>)msg.obj;
                ordersRecycAdapter adapter = new ordersRecycAdapter(this.context,orderList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(
                        this.context,
                        LinearLayoutManager.VERTICAL, //垂直方向
                        false             //非倒序
                ));
//                adapter.setOnItemClickListener(new ordersRecycAdapter.OrderClickLisener() {
//                    @Override
//                    public void OnRouteClick(RelOrder order) {
//                        Intent intent = new Intent(context, RouteActivity.class);
//                        LatLonPoint mstartlatlon = RouteUtil.getLatLon(order.getStartLonLat());
//                        LatLonPoint mendlatlon = RouteUtil.getLatLon(order.getEndLonLat());
//                        DrivePath drivePath = RouteUtil.getDrivePath(order.getListSteps());
//
//                        intent.putExtra("mStartPoint",mstartlatlon);
//                        intent.putExtra("mEndPoint",mendlatlon);
//                        intent.putExtra("drivePath",drivePath);
//                        context.startActivity(intent);
//                    }
//                    @Override
//                    public void OnJoinClick(RelOrder order) {
//
//                    }
//                });

                recyclerView.setVisibility(View.VISIBLE);
//                initRecyc(orderList);

                break;
            case NO_DATAS:

                break;
            default:
        }
    }

//    public void initRecyc(List<RelOrder> orders){
//        recycAdapter = new ordersRecycAdapter(DetailActivity, orders);
//        recyc.setAdapter(recycAdapter);
//        recyc.setLayoutManager(new LinearLayoutManager(
//                DetailActivity.this,
//                LinearLayoutManager.VERTICAL, //垂直方向
//                false             //非倒序
//        ));
//    }
}
