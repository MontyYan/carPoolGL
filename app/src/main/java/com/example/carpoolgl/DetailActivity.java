package com.example.carpoolgl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity implements
        View.OnClickListener, RouteSearch.OnRouteSearchListener {

    private View bottom_Detail;
    private BottomSheetBehavior mBoSheetBehavior;
    private TextView detail_geton_tv;
    private TextView detail_getoff_tv;
    private TextView start_time;
    private TextView person_num;
    private TextView Fog;
    private TextView money;
    private Button publish_bt;

    private TextView num_1;
    private TextView num_2;
    private TextView num_3;
    private TextView num_4;
    private Button num_certain;

    private String startAddr;
    private String endAddr;
    private LatLonPoint mStartPoint = new LatLonPoint(39.942295,116.335891);//起点，39.942295,116.335891
    private LatLonPoint mEndPoint = new LatLonPoint(39.995576,116.481288);//终点，39.995576,116.481288

    private DriveRouteResult mDriveRouteResult;
    private final int ROUTE_TYPE_DRIVE = 2;
    private RouteSearch mRouteSearch;

    TimePickerView pvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initFvb();
        setStartEndTv();
        searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
        mBoSheetBehavior = BottomSheetBehavior.from(bottom_Detail);
        mBoSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        //时间选择器
        timePicker();
    }

    public void setStartEndTv(){
        Intent intent = getIntent();
        startAddr = intent.getStringExtra("startAddress");
        endAddr = intent.getStringExtra("endAddress");
        detail_geton_tv.setText(startAddr);
        detail_getoff_tv.setText(endAddr);
    }
    //*************************************************************************
    public void timePicker(){
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd  HH:mm");
                String da= sdf.format(date);
//                tvTime.setText(getTime(date));
                start_time.setText(da+" >");
//                Toast.makeText(DetailActivity.this,da,Toast.LENGTH_SHORT).show();
            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {
                Log.i("pvTime", "onTimeSelectChanged");
            }
        })
                .setType(new boolean[]{false, false, true, true, true, false})
                .setOutSideColor(Color.parseColor("#99FFFFFF"))
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("pvTime", "onCancelClickListener");
                    }
                })
                .setItemVisibleCount(5) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .build();
    }
    //*************************************************************************
    public void initFvb(){
        bottom_Detail = findViewById(R.id.bottom_detail);
        detail_geton_tv = findViewById(R.id.detail_geton_tv);
        detail_getoff_tv = findViewById(R.id.detail_getoff_tv);
        Fog = findViewById(R.id.fog);
        Fog.setOnClickListener(this);
        money = findViewById(R.id.money);
        start_time = findViewById(R.id.start_time);
        start_time.setOnClickListener(this);
        person_num = findViewById(R.id.person_num);
        person_num.setOnClickListener(this);
        publish_bt = findViewById(R.id.publish_bt);
        publish_bt.setOnClickListener(this);

        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);

        num_1 = findViewById(R.id.num_1);
        num_1.setOnClickListener(this);
        num_2 = findViewById(R.id.num_2);
        num_2.setOnClickListener(this);
        num_3 = findViewById(R.id.num_3);
        num_3.setOnClickListener(this);
        num_4 = findViewById(R.id.num_4);
        num_4.setOnClickListener(this);
        num_certain = findViewById(R.id.num_certain);
        num_certain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        GradientDrawable mdraw;
        switch (v.getId()){
            case R.id.start_time:
//                mBoSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                pvTime.show();
//                Fog.setVisibility(View.VISIBLE);
                break;
            case R.id.person_num:
                mBoSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                Fog.setVisibility(View.VISIBLE);
                break;
            case R.id.fog:
                Fog.setVisibility(View.INVISIBLE);
                mBoSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.num_1:
                nums_color_set(num_1);
                num_certain.setText("1人乘车");
                break;
            case R.id.num_2:
                nums_color_set(num_2);
                num_certain.setText("2人乘车");
                break;
            case R.id.num_3:
                nums_color_set(num_3);
                num_certain.setText("3人乘车");
                break;
            case R.id.num_4:
                nums_color_set(num_4);
                num_certain.setText("4人乘车");
                break;
            case R.id.num_certain:
                Fog.setVisibility(View.INVISIBLE);
                mBoSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                person_num.setText(num_certain.getText()+" >");
                break;
        }
    }

    public void nums_color_set(TextView tv){
        GradientDrawable mdraw;
        TextView[] tvs = {num_1,num_2,num_3,num_4};
        for(int i=0;i<tvs.length;i++){
            if(tv==tvs[i]){
                mdraw = (GradientDrawable) tvs[i].getBackground();
                mdraw.setColor(getResources().getColor(R.color.num_pitch));
            }else {
                mdraw = (GradientDrawable) tvs[i].getBackground();
                mdraw.setColor(getResources().getColor(R.color.num_color));
            }
        }
    }

    //*************************************************************************
    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode ) {
        Log.i("DetailActivity","onDriveRouteSearched");
        if(errorCode== AMapException.CODE_AMAP_SUCCESS){
            Log.i("DetailActivity",errorCode+"");
            if(result != null&&result.getPaths() !=null){
                Log.i("DetailActivity","result"+result+" result.getPaths()"+result.getPaths());
                if( result.getPaths().size()>0 ){
                    Log.i("DetailActivity","result.getPaths().size()"+result.getPaths().size());
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths().get(0);
                    Log.i("DetailActivity","drivePath "+drivePath);
                    if(drivePath == null) {
                        return;
                    }
                    int taxiCost = (int)mDriveRouteResult.getTaxiCost();
                    Log.i("DetailActivity","taxiCost "+taxiCost);
                    money.setText(taxiCost+"");
                    Log.i("money",money+"");
                }else if (result != null && result.getPaths() == null) {
                    Toast.makeText(DetailActivity.this,"无数据",Toast.LENGTH_SHORT).show();
                    Log.i("DetailActivity","result.getPaths() 小于0，无数据");

                }
                Log.i("DetailActivity","result.getPaths().size()"+result.getPaths().size()+"");
            }else {
                Toast.makeText(DetailActivity.this,"没有搜到数据",Toast.LENGTH_SHORT).show();
                Log.i("DetailActivity","result result.getPaths 空null无数据");
            }
            Log.i("DetailActivity","result"+result+" result.getPaths()"+result.getPaths());
        }else {
            Toast.makeText(DetailActivity.this,errorCode,Toast.LENGTH_SHORT).show();
            Log.i("DetailActivity","errorCode 空null");
        }

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
    //*************************************************************************
    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            Toast.makeText(DetailActivity.this,"稍后再试",Toast.LENGTH_SHORT).show();
//            ToastUtil.show(mContext, "定位中，稍后再试...");
            return;
        }
        if (mEndPoint == null) {
            Toast.makeText(DetailActivity.this,"终点未设置",Toast.LENGTH_SHORT).show();
//            ToastUtil.show(mContext, "终点未设置");
        }
//        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        Log.i("DetailActivity","fromto "+fromAndTo+"");
        if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            Log.i("DetailActivity","规划");
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        }
    }
}
