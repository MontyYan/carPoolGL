package com.example.carpoolgl.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.example.carpoolgl.R;
import com.example.carpoolgl.bean.RelOrder;
import com.example.carpoolgl.bean.mLonLat;
import com.example.carpoolgl.route.RouteActivity;
import com.example.carpoolgl.util.RouteUtil;

import java.util.List;
import java.util.Set;

/*
* 显示其他用户已经发布的订单信息。
* 司机与乘客使用同一个查看adapter
* */

public class ordersRecycAdapter extends RecyclerView.Adapter<ordersRecycAdapter.myHolder> {

    private static final int PA_IDENTITY = 5;
    private static final int DR_IDENTITY = 6;
    private Context context;
//    private List<orderBean> datas;
    private List<RelOrder> datas;
    private RelOrder order;
    private Integer IDENTITY =0;

    public ordersRecycAdapter(Context context, List<RelOrder> datas){
        this.context = context;
        this.datas = datas;
    }

    public ordersRecycAdapter(Context context, List<RelOrder> datas,Integer id){
        this.context = context;
        this.datas = datas;
        this.IDENTITY = id;
    }

    class myHolder extends RecyclerView.ViewHolder{
        private TextView startLoc;
        private TextView endLoc;
        private TextView date;
        private TextView num;
        private Button route;
        private Button join;

        public myHolder(@NonNull View itemView){
            super(itemView);
            startLoc = itemView.findViewById(R.id.tv_start_loc);
            endLoc = itemView.findViewById(R.id.tv_end_loc);
            date = itemView.findViewById(R.id.tv_date);
            num = itemView.findViewById(R.id.tv_num);
            route = itemView.findViewById(R.id.route_bt);
            join = itemView.findViewById(R.id.join_bt);

            route.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(orderClickLisener!=null){
                        order = datas.get(getLayoutPosition());
                        orderClickLisener.OnRouteClick(order);  //回调‘查看路线’方法
                    }
                }
            });

            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(orderClickLisener!=null){
                        order = datas.get(getLayoutPosition());
                        orderClickLisener.OnJoinClick(order);   //回调'加入'方法
                    }
                }
            });

        }
    }

    private OrderClickLisener orderClickLisener;
    public interface OrderClickLisener{
        /**
         * 当recyclerview某个被点击的时候回调
         * @param order  点击视图中button
         *
         */
        void OnRouteClick(RelOrder order);
        void OnJoinClick(RelOrder order);
    }
    //设置recyclerview某条的监听
    public void setOnItemClickListener(ordersRecycAdapter.OrderClickLisener orderClickLisener) {
        this.orderClickLisener = orderClickLisener;
    }

    @NonNull
    @Override
    public ordersRecycAdapter.myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_orders_recyc,parent,false);
        myHolder holder = new myHolder(itemview);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ordersRecycAdapter.myHolder holder, int position) {
        order = datas.get(position);
        Log.i("adapter",order.getStartLonLat());
        Log.i("adapter",order.getEndLonLat());
        holder.startLoc.setText(order.getStartLoc());
        holder.endLoc.setText(order.getEndLoc());
        holder.date.setText(order.getStartTime().substring(5));
        holder.num.setText(order.getPassNum()+"");
        if(IDENTITY.equals(PA_IDENTITY)){
            holder.join.setText("加入");
        }else if(IDENTITY.equals(DR_IDENTITY)){
            holder.join.setText("接单");
        }
    }

    @Override
    public int getItemCount() {
        return this.datas.size();
    }

}
