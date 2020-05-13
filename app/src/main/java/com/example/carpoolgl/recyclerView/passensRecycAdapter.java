package com.example.carpoolgl.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.example.carpoolgl.R;
import com.example.carpoolgl.bean.RelOrder;
import com.example.carpoolgl.bean.RelPassMems;
import com.example.carpoolgl.bean.mLonLat;
import com.example.carpoolgl.route.RouteActivity;

import org.w3c.dom.Text;

import java.util.List;

public class passensRecycAdapter extends RecyclerView.Adapter<passensRecycAdapter.myHolder> {

    private Context context;
    private List<RelPassMems> datas;

    public passensRecycAdapter(Context context, List<RelPassMems> datas){
        this.context = context;
        this.datas = datas;
        Log.i("driverOrderr_M5",datas.toString());
    }

    class myHolder extends RecyclerView.ViewHolder{
        private TextView passName;
        private TextView passPhone;


        public myHolder(@NonNull View itemView){
            super(itemView);
            passName = itemView.findViewById(R.id.passen_name_tv);
            passPhone = itemView.findViewById(R.id.passen_phone_tv);

        }
    }

    @NonNull
    @Override
    public passensRecycAdapter.myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_passens_recyc,parent,false);
//        View itemview = View.inflate(context,R.layout.item_orders_recyc,null);
        myHolder holder = new myHolder(itemview);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull passensRecycAdapter.myHolder holder, int position) {
        final RelPassMems passMems = datas.get(position);
        Log.i("driverOrderr_M5",passMems.toString());
        holder.passName.setText(passMems.getName());
        holder.passPhone.setText(passMems.getPhone());
    }


    @Override
    public int getItemCount() {
        return this.datas.size();
    }

}
