package com.example.carpoolgl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.example.carpoolgl.NaviDriving.GPSNaviActivity;
import com.example.carpoolgl.bean.RecordInfo;
import com.example.carpoolgl.bean.RelPassMems;
import com.example.carpoolgl.bean.simDriverCar;
import com.example.carpoolgl.recyclerView.passensRecycAdapter;
import com.example.carpoolgl.route.RouteActivity;
import com.example.carpoolgl.util.RouteUtil;
import com.example.carpoolgl.util.ToastUtil;


import java.util.ArrayList;
import java.util.List;

public class RecordDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private RecordInfo record;

    private TextView rec_order_seq_tv;
    private TextView rec_order_geton_tv;
    private TextView rec_order_getoff_tv;
    private TextView rec_order_time_tv;
    private TextView rec_order_money_tv;
    private Button rec_order_route_bt;

    private CardView rec_driverCarInfo_cv;
    private TextView rec_driverName_tv;
    private TextView rec_car_describe_tv;
    private TextView rec_car_num_tv;
    private TextView rec_driver_phone_tv;

    private LinearLayout rec_passenInfo_linear;
    private RecyclerView rec_order_RecycView;

    private DrivePath drivePath;
    private LatLonPoint mStartPoint = new LatLonPoint(39.942295,116.335891);//起点，39.942295,116.335891
    private LatLonPoint mEndPoint = new LatLonPoint(39.995576,116.481288);//终点，39.995576,116.481288

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        Intent intent = getIntent();
        record = intent.getParcelableExtra("RecordInfo");
        if(record==null){
            ToastUtil.show(this,"record信息为空");
        }
        else{
            initController();//控件注册
            setDetail();//信息初始化
        }

    }

    public void initController(){
        rec_order_seq_tv = findViewById(R.id.rec_order_seq_tv);
        rec_order_geton_tv = findViewById(R.id.rec_order_geton_tv);
        rec_order_getoff_tv = findViewById(R.id.rec_order_getoff_tv);
        rec_order_time_tv = findViewById(R.id.rec_order_time_tv);
        rec_order_money_tv = findViewById(R.id.rec_order_money_tv);
        rec_order_route_bt = findViewById(R.id.rec_order_route_bt);

        rec_driverCarInfo_cv = findViewById(R.id.rec_driverCarInfo_cv);
        rec_driverName_tv = findViewById(R.id.rec_driverName_tv);
        rec_car_describe_tv = findViewById(R.id.rec_car_describe_tv);
        rec_car_num_tv = findViewById(R.id.rec_car_num_tv);
        rec_driver_phone_tv = findViewById(R.id.rec_driver_phone_tv);

        rec_passenInfo_linear = findViewById(R.id.rec_passenInfo_linear);
        rec_order_RecycView = findViewById(R.id.rec_order_RecycView);
        setOnClick();
    }

    public void setOnClick(){
        rec_order_route_bt.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rec_order_route_bt:
                drivePath = JSONObject.parseObject(record.getRouteJson(),DrivePath.class);
                mStartPoint = RouteUtil.getLatLon(record.getStartLatLon());
                mEndPoint = RouteUtil.getLatLon(record.getEndLatLon());
                Intent intent = new Intent(this, RouteActivity.class);
                intent.putExtra("drivePath",drivePath);
                intent.putExtra("mStartPoint",mStartPoint);
                intent.putExtra("mEndPoint",mEndPoint);
                startActivity(intent);
                break;

        }
    }

    public void setDetail(){
        rec_order_seq_tv.setText(record.getOrderSeq());
        rec_order_geton_tv.setText(record.getStartLoc());
        rec_order_getoff_tv.setText(record.getEndLoc());
        rec_order_time_tv.setText(record.getTime());
        rec_order_money_tv.setText(record.getMoney()+"");

        JsonToBean();

    }

    public void JsonToBean(){
        switch (record.getIdentity()){
            case 0: //乘客订单
                simDriverCar simDrCar = JSONObject.parseObject(record.getSpecialJson(),simDriverCar.class);
                rec_driverName_tv.setText("司机"+simDrCar.getDrName());
                rec_car_describe_tv.setText(simDrCar.getCarType());
                rec_car_num_tv.setText(simDrCar.getCarNum());
                rec_driver_phone_tv.setText(simDrCar.getDrPhone());

                rec_driverCarInfo_cv.setVisibility(View.VISIBLE);
                rec_passenInfo_linear.setVisibility(View.GONE);

                break;

            case 1: //司机订单
//                List<RelPassMems> passenLists = new ArrayList<>();
                List<RelPassMems> passenLists = JSONObject.parseArray(record.getSpecialJson(),RelPassMems.class);
                if(passenLists==null)
                    passenLists = new ArrayList<>();
                passensRecycAdapter adapter = new passensRecycAdapter(this,passenLists);
                rec_order_RecycView.setAdapter(adapter);
                rec_order_RecycView.setLayoutManager(new LinearLayoutManager(
                        this,
                        LinearLayoutManager.VERTICAL, //垂直方向
                        false             //非倒序
                ));

                rec_passenInfo_linear.setVisibility(View.VISIBLE);
                rec_driverCarInfo_cv.setVisibility(View.GONE    );

                break;
        }


    }
}
