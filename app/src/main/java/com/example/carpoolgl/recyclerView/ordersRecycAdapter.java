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

import java.util.List;

public class ordersRecycAdapter extends RecyclerView.Adapter<ordersRecycAdapter.myHolder> {

    private Context context;
//    private List<orderBean> datas;
    private List<RelOrder> datas;

//    public ordersRecycAdapter(DetailActivity context, List<orderBean> datas){
//        this.context = context;
//        this.datas = datas;
//    }

    public ordersRecycAdapter(Context context, List<RelOrder> datas){
        this.context = context;
        this.datas = datas;
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
            route = itemView.findViewById(R.id.bt_route);
            join = itemView.findViewById(R.id.bt_join);

        }
    }

    @NonNull
    @Override
    public ordersRecycAdapter.myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_orders_recyc,parent,false);
//        View itemview = View.inflate(context,R.layout.item_orders_recyc,null);
        myHolder holder = new myHolder(itemview);
        return holder;
    }

@Override
public void onBindViewHolder(@NonNull ordersRecycAdapter.myHolder holder, int position) {
    final RelOrder order = datas.get(position);
//    Log.i("adapter",order.toString());
    Log.i("adapter",order.getStartLonLat());
    Log.i("adapter",order.getEndLonLat());
    holder.startLoc.setText(order.getStartLoc());
    holder.endLoc.setText(order.getEndLoc());
    holder.date.setText(order.getStartTime().substring(5));
    holder.num.setText(order.getPassNum()+"");
    holder.route.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Toast.makeText(context,"点击", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, RouteActivity.class);
            mLonLat start = JSONObject.parseObject(order.getStartLonLat(), mLonLat.class);
            LatLonPoint mstartlatlon = new LatLonPoint(start.getLatitude(),start.getLongitude());
            intent.putExtra("mStartPoint",mstartlatlon);
            mLonLat end = JSONObject.parseObject(order.getEndLonLat(),mLonLat.class);
            LatLonPoint mendlatlon = new LatLonPoint(end.getLatitude(),end.getLongitude());
            intent.putExtra("mEndPoint",mendlatlon);
            intent.putExtra("drivePath",getPath(order.getListSteps()));
            context.startActivity(intent);
//            intent.putExtra("mDriveRouteResult","");
            Log.i("adapter","点击");
        }
    });
}
    public DrivePath getPath(String json){
        DrivePath path=new DrivePath();
//        List<DriveStep> steps = new ArrayList<>();
//        DriveStep step = new DriveStep();
        try{
//            steps = JSONObject.parseArray(json,DriveStep.class);
            path= JSONObject.parseObject(json,DrivePath.class);
//            path.setSteps(steps);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.i("findOrder_recyc",path.toString());
        Log.i("findOrder_recyc",path.getStrategy());
//        DrivePath path = JSON.parse(json,DrivePath.class);
        return path;
    }

    @Override
    public int getItemCount() {
        return this.datas.size();
    }

}
