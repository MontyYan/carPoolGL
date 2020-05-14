package com.example.carpoolgl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
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
import com.example.carpoolgl.Static.STATIC_CLASS;
import com.example.carpoolgl.Static.STATIC_USERINFO;
import com.example.carpoolgl.bean.RelOrder;
import com.example.carpoolgl.route.RouteActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dr_OrderDetailActivity extends AppCompatActivity implements View.OnClickListener,RouteSearch.OnRouteSearchListener {

    private static final int IDENTITY = 6;
    private TextView detail_geton_tv;
    private TextView detail_getoff_tv;
    private TextView start_time;
    private TextView money;
    private Button publish_bt;
    public Button route_bt;

    private String startAddr;
    private String endAddr;
    private LatLonPoint mStartPoint = new LatLonPoint(39.942295,116.335891);//起点，39.942295,116.335891
    private LatLonPoint mEndPoint = new LatLonPoint(39.995576,116.481288);//终点，39.995576,116.481288
    private DrivePath drivePath;

    private DriveRouteResult mDriveRouteResult;
    private final int ROUTE_TYPE_DRIVE = 2;
    private RouteSearch mRouteSearch;

    private TimePickerView pvTime;

    //初始化订单bean
    private RelOrder order = new RelOrder(STATIC_USERINFO.getUserSeq());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr__order_detail);
        initController();   //初始化控件
        getSearchInfo();    //获取searchAc传递的数据
        searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);

        initOrder();
        //时间选择器
        timePicker();

    }

    public void initController(){
        detail_geton_tv = findViewById(R.id.dr_detail_geton_tv);
        detail_getoff_tv = findViewById(R.id.dr_detail_getoff_tv);
        money = findViewById(R.id.dr_money);
        start_time = findViewById(R.id.dr_start_time);
        publish_bt = findViewById(R.id.dr_publish);
        route_bt = findViewById(R.id.dr_route);
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);

        setOnClick();
    }

    public void setOnClick(){
        start_time.setOnClickListener(this);
        publish_bt.setOnClickListener(this);
        route_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dr_publish:
                Intent intent = new Intent(this,PublishActivity.class);
                ReinitlOrder();
                Bundle bundle = new Bundle();
                bundle.putParcelable("relorder",order);
                intent.putExtra("data",bundle);
                intent.putExtra("identity",IDENTITY);
                startActivity(intent);
                break;
            case R.id.dr_route:
                Intent intent2 = new Intent(this, RouteActivity.class);
                intent2.putExtra("mStartPoint",mStartPoint);
                intent2.putExtra("mEndPoint",mEndPoint);
                intent2.putExtra("drivePath",drivePath);
//                intent2.putExtra("mDriveRouteResult",mDriveRouteResult);
                startActivity(intent2);
                break;
            case R.id.dr_start_time:
                pvTime.show();
                break;
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode ) {
        if(errorCode== AMapException.CODE_AMAP_SUCCESS){
            if(result != null&&result.getPaths() !=null){
                if( result.getPaths().size()>0 ){
                    mDriveRouteResult = result;
                    drivePath = mDriveRouteResult.getPaths().get(0);
                    if(drivePath == null) {
                        return;
                    }
                    int taxiCost = (int)mDriveRouteResult.getTaxiCost();
                    money.setText(taxiCost+"");
                    order.setMoney(taxiCost);
                }else if (result != null && result.getPaths() == null) {
                    Toast.makeText(Dr_OrderDetailActivity.this,"无数据",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(Dr_OrderDetailActivity.this,"没有搜到数据",Toast.LENGTH_SHORT).show();
                Log.i("DetailActivity","result result.getPaths 空null无数据");
            }
            Log.i("DetailActivity","result"+result+" result.getPaths()"+result.getPaths());
        }else {
            Toast.makeText(Dr_OrderDetailActivity.this,errorCode,Toast.LENGTH_SHORT).show();
            Log.i("DetailActivity","errorCode 空null");
        }

    }

    public void ReinitlOrder(){
        order.setStartLoc(startAddr);
        order.setDrSeq(STATIC_USERINFO.getUserSeq());//司机发布的订单，直接赋值
        order.setEndLoc(endAddr);
//        order.setStartLonLat(mStartPoint.toString());
        order.setStartLonLat(JSON.toJSONString(mStartPoint));
//        order.setEndLonLat(mEndPoint.toString());d
        order.setEndLonLat(JSON.toJSONString(mEndPoint));
//                order.setListstep(drivePath.getSteps());
//        List<DriveStep> list = drivePath.getSteps();
        String steps = JSON.toJSONString(drivePath);
        order.setListSteps(steps);
    }

    public void getSearchInfo(){
        Intent intent = getIntent();
        startAddr = intent.getStringExtra("startAddress");
        endAddr = intent.getStringExtra("endAddress");
        detail_geton_tv.setText(startAddr);
        detail_getoff_tv.setText(endAddr);
        mStartPoint = intent.getParcelableExtra("startLatLon");
        Log.i("DrDetailActivity",mStartPoint.toString());
        Log.i("DrDetailActivity",mEndPoint.toString());
        mEndPoint = intent.getParcelableExtra("endLatLon");
    }
    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            Toast.makeText(Dr_OrderDetailActivity.this,"稍后再试",Toast.LENGTH_SHORT).show();
//            ToastUtil.show(mContext, "定位中，稍后再试...");
            return;
        }
        if (mEndPoint == null) {
            Toast.makeText(Dr_OrderDetailActivity.this,"终点未设置",Toast.LENGTH_SHORT).show();
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

    //*************************************************************************
    //对order进行部分数据进行默认初始化（默认当前时间，默认乘客人数为1）
    public void initOrder(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        order.setStartTime(df.format(new Date()));
        order.setPassNum(0);
        order.setCondition(1);  //司机发布的订单
    }
    //*************************************************************************
    public void timePicker(){
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
                String da= sdf.format(date);
//                Date date_=null;
//                try{
//                    date_ = sdf.parse(da);
//                    Log.i(TAG,">>>>>"+date_.toString());
//                }catch (Exception e){
//                    e.printStackTrace();
//                }

//                tvTime.setText(getTime(date));
                start_time.setText(da.substring(5)+" >");   //截去年份，剩下字段显示在ui
                order.setStartTime(da);

                Toast.makeText(Dr_OrderDetailActivity.this,order.getStartTime(),Toast.LENGTH_SHORT).show();
            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {
                Log.i("pvTime", "onTimeSelectChanged");
            }
        })
                .setType(new boolean[]{false, false, true, true, true, false})
                .setOutSideColor(Color.parseColor("#99FFFFFF"))
                .setTitleText("出发时间").setTitleColor(Color.parseColor("#000000"))
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

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
