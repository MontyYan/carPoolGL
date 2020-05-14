package com.example.carpoolgl;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.example.carpoolgl.Static.STATIC_USERINFO;
import com.example.carpoolgl.base.activity.baseActivity;
import com.example.carpoolgl.bean.RelOrder;
import com.example.carpoolgl.bean.RelPassMems;
import com.example.carpoolgl.bean.orderBean;
import com.example.carpoolgl.findOrders.passenger.findHandler;
import com.example.carpoolgl.findOrders.passenger.fordersPresenter;
import com.example.carpoolgl.findOrders.passenger.fordersView;
import com.example.carpoolgl.recyclerView.ordersRecycAdapter;
import com.example.carpoolgl.route.RouteActivity;
import com.example.carpoolgl.util.RouteUtil;
import com.example.carpoolgl.util.ToastUtil;
import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pa_OrderDetailActivity extends baseActivity<fordersView, fordersPresenter> implements
        View.OnClickListener, RouteSearch.OnRouteSearchListener, fordersView {

    private static final String TAG="DetailActivity";
    private static final String TAG_2="findOrder_AC";
    private static final int IDENTITY = 5;
    private View bottom_Detail;
    private BottomSheetBehavior mBoSheetBehavior;
    private TextView detail_geton_tv;
    private TextView detail_getoff_tv;
    private TextView start_time;
    private TextView person_num;
    private TextView Fog;
    private TextView money;
    private FloatingActionButton publish_bt;
    private FloatingActionButton fab_route;
    private FloatingActionButton fab_find;
    private FloatingActionMenu menu;

    private fordersPresenter presenter;

    private TextView num_1;
    private TextView num_2;
    private TextView num_3;
    private TextView num_4;
    private Button num_certain;

    private String startAddr;
    private String endAddr;
    private LatLonPoint mStartPoint = new LatLonPoint(39.942295,116.335891);//起点，39.942295,116.335891
    private LatLonPoint mEndPoint = new LatLonPoint(39.995576,116.481288);//终点，39.995576,116.481288
    private DrivePath drivePath;

    private DriveRouteResult mDriveRouteResult;
    private final int ROUTE_TYPE_DRIVE = 2;
    private RouteSearch mRouteSearch;

    private TimePickerView pvTime;

    private RecyclerView recyc;
    private List<orderBean> recycData;
    private ordersRecycAdapter recycAdapter;

    private TextView finding_order_tv;
    private ProgressBar finding_order_pb;

    //初始化订单bean
    private RelOrder order = new RelOrder(STATIC_USERINFO.getUserSeq());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initFvb();
        getSearchInfo();
        searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
        mBoSheetBehavior = BottomSheetBehavior.from(bottom_Detail);
        mBoSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        initOrder();//初始化order默认数据
        findOrders();
        //时间选择器
        timePicker();
        initRecycData();
//        initRecyclerView();
    }

    @Override
    public fordersPresenter createPresenter() {
        return new fordersPresenter();
    }

    @Override
    public fordersView createView() {
        return this;
    }

    public void getSearchInfo(){
        Intent intent = getIntent();
        startAddr = intent.getStringExtra("startAddress");
        endAddr = intent.getStringExtra("endAddress");
        detail_geton_tv.setText(startAddr);
        detail_getoff_tv.setText(endAddr);
        mStartPoint = intent.getParcelableExtra("startLatLon");
        Log.i("DetailActivity",mStartPoint.toString());
        mEndPoint = intent.getParcelableExtra("endLatLon");
        Log.i("DetailActivity",mEndPoint.toString());
    }
    //*************************************************************************
    //对order进行部分数据进行默认初始化（默认当前时间，默认乘客人数为1）
    public void initOrder(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        order.setStartTime(df.format(new Date()));
        order.setPassNum(1);
        order.setCondition(0);  //仅发布，等待接单
    }
    //*************************************************************************
    public void timePicker(){
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
                String da= sdf.format(date);

                start_time.setText(da.substring(5)+" >");   //截去年份，剩下字段显示在ui
                order.setStartTime(da);

                Toast.makeText(Pa_OrderDetailActivity.this,order.getStartTime(),Toast.LENGTH_SHORT).show();
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
    public void initFvb(){
        bottom_Detail = findViewById(R.id.bottom_detail);
        detail_geton_tv = findViewById(R.id.detail_geton_tv);
        detail_geton_tv.setOnClickListener(this);
        detail_getoff_tv = findViewById(R.id.detail_getoff_tv);
        detail_getoff_tv.setOnClickListener(this);
        Fog = findViewById(R.id.fog);
        Fog.setOnClickListener(this);
        money = findViewById(R.id.money);
        start_time = findViewById(R.id.start_time);
        start_time.setOnClickListener(this);
        person_num = findViewById(R.id.person_num);
        person_num.setOnClickListener(this);
//        menu = findViewById(R.id.fab);
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
        recyc = findViewById(R.id.detailRecycView);
        fab_route = findViewById(R.id.fab_route);
        fab_route.setOnClickListener(this);
        fab_find = findViewById(R.id.fab_find);
        fab_find.setOnClickListener(this);

        finding_order_tv = findViewById(R.id.finding_order_tv);
        finding_order_pb = findViewById(R.id.finding_order_pb);
    }

    @Override
    public void onClick(View v) {
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
            case R.id.publish_bt:
                Intent intent = new Intent(Pa_OrderDetailActivity.this,PublishActivity.class);
                ReinitlOrder(); //对订单order的剩余数据进行赋值
                Bundle bundle = new Bundle();
                bundle.putParcelable("relorder",order);
                intent.putExtra("data",bundle);
                intent.putExtra("identity",IDENTITY);
//                Log.i(TAG,order.toString());
//                intent.putExtra("geton",detail_geton_tv.getText());
//                intent.putExtra("getoff",detail_getoff_tv.getText());
//                intent.putExtra("mStartPoint",mStartPoint);
//                intent.putExtra("mEndPoint",mEndPoint);
//                intent.putExtra("time",start_time.getText());
//                intent.putExtra("number",person_num.getText());
//                intent.putExtra("money",money.getText());
                startActivity(intent);
            case R.id.detail_geton_tv:
                finish();
                break;
            case R.id.detail_getoff_tv:
                finish();
                break;

            case R.id.fog:
                Fog.setVisibility(View.INVISIBLE);
                mBoSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.num_1:
                nums_color_set(num_1);
                num_certain.setText("1人乘车");
                order.setPassNum(1);
                break;
            case R.id.num_2:
                nums_color_set(num_2);
                num_certain.setText("2人乘车");
                order.setPassNum(2);
                break;
            case R.id.num_3:
                nums_color_set(num_3);
                num_certain.setText("3人乘车");
                order.setPassNum(3);
                break;
            case R.id.num_4:
                nums_color_set(num_4);
                num_certain.setText("4人乘车");
                order.setPassNum(4);
                break;
            case R.id.num_certain:
                Fog.setVisibility(View.INVISIBLE);
                mBoSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                person_num.setText(num_certain.getText()+" >");
                break;
            case R.id.fab_route:
                Intent intent2 = new Intent(Pa_OrderDetailActivity.this, RouteActivity.class);
                intent2.putExtra("mStartPoint",mStartPoint);
                intent2.putExtra("mEndPoint",mEndPoint);
                intent2.putExtra("drivePath",drivePath);
//                intent2.putExtra("mDriveRouteResult",mDriveRouteResult);
                startActivity(intent2);
                break;
            case R.id.fab_find:

                break;
        }
    }
    //*************************************************************************
    public void findOrders(){
        Log.i(TAG_2,order.toString());
//        getPresenter().findOrder(Pa_OrderDetailActivity.this,finding_order_tv,finding_order_pb,recyc,order);
        presenter = getPresenter();
        presenter.findOrder(Pa_OrderDetailActivity.this,order);
    }
    //*************************************************************************
    public void ReinitlOrder(){
        order.setStartLoc(startAddr);
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

    //*************************************************************************
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
        if(errorCode == AMapException.CODE_AMAP_SUCCESS){
            Log.i("DetailActivity",errorCode+"");
            if(result != null&&result.getPaths() !=null){
                Log.i("DetailActivity","result"+result+" result.getPaths()"+result.getPaths());
                if( result.getPaths().size()>0 ){
                    Log.i("DetailActivity","result.getPaths().size()"+result.getPaths().size());
                    mDriveRouteResult = result;
                    drivePath = mDriveRouteResult.getPaths().get(0);
                    Log.i("DetailActivity","drivePath "+drivePath);
                    if(drivePath == null) {
                        return;
                    }
                    int taxiCost = (int)mDriveRouteResult.getTaxiCost();
                    Log.i("DetailActivity","taxiCost "+taxiCost);
                    money.setText(taxiCost+"");
                    order.setMoney(taxiCost);
//                    Log.i("money",money+"");
                }else if (result != null && result.getPaths() == null) {
                    Toast.makeText(Pa_OrderDetailActivity.this,"无数据",Toast.LENGTH_SHORT).show();
//                    Log.i("DetailActivity","result.getPaths() 小于0，无数据");

                }
//                Log.i("DetailActivity","result.getPaths().size()"+result.getPaths().size()+"");
            }else {
                Toast.makeText(Pa_OrderDetailActivity.this,"没有搜到数据",Toast.LENGTH_SHORT).show();
                Log.i("DetailActivity","result result.getPaths 空null无数据");
            }
            Log.i("DetailActivity","result"+result+" result.getPaths()"+result.getPaths());
        }else {
            Log.i("DetailActivity","errorCode 空null");
            Toast.makeText(Pa_OrderDetailActivity.this,errorCode,Toast.LENGTH_SHORT).show();
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
            Toast.makeText(Pa_OrderDetailActivity.this,"稍后再试",Toast.LENGTH_SHORT).show();
//            ToastUtil.show(mContext, "定位中，稍后再试...");
            return;
        }
        if (mEndPoint == null) {
            Toast.makeText(Pa_OrderDetailActivity.this,"终点未设置",Toast.LENGTH_SHORT).show();
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
    public void initRecycData(){
        recycData = new ArrayList<>();
        String st= "七星区  桂林电子科技大学（花江）";
        String en = "雁山区  桂林师范大学（雁山）";
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        Date date = new Date(System.currentTimeMillis());
        Integer num = 3;
        for(int i=0;i<10;i++){
            recycData.add(new orderBean(st,en,format.format(date),num));
        }
    }

    public void initRecyclerView(){
//        recycAdapter = new ordersRecycAdapter(DetailActivity.this, recycData);
//        recyc.setAdapter(recycAdapter);
//        recyc.setLayoutManager(new LinearLayoutManager(
//                DetailActivity.this,
//                LinearLayoutManager.VERTICAL, //垂直方向
//                false             //非倒序
//                ));
    }
    //**V层***********************************************************************
    @Override
    public void setRecyclerView(final Integer result, final List<RelOrder> orders){
        if(result.equals(0)){
            ToastUtil.show(this,"查找失败");
            finding_order_tv.setText("查找失败");
//            finding_order_tv.setVisibility(View.GONE);
            finding_order_pb.setVisibility(View.GONE);
        }else{
            finding_order_tv.setVisibility(View.GONE);
            finding_order_pb.setVisibility(View.GONE);
            ordersRecycAdapter adapter = new ordersRecycAdapter(Pa_OrderDetailActivity.this,orders,IDENTITY);
            recyc.setAdapter(adapter);
            recyc.setLayoutManager(new LinearLayoutManager(
                    Pa_OrderDetailActivity.this,
                    LinearLayoutManager.VERTICAL, //垂直方向
                    false             //非倒序
            ));
            recyc.setVisibility(View.VISIBLE);
            adapter.setOnItemClickListener(new ordersRecycAdapter.OrderClickLisener() {
                @Override
                public void OnRouteClick(RelOrder order) {
                    Intent intent = new Intent(Pa_OrderDetailActivity.this, RouteActivity.class);
                    LatLonPoint mstartlatlon = RouteUtil.getLatLon(order.getStartLonLat());
                    LatLonPoint mendlatlon = RouteUtil.getLatLon(order.getEndLonLat());
                    DrivePath drivePath = RouteUtil.getDrivePath(order.getListSteps());

                    intent.putExtra("mStartPoint",mstartlatlon);
                    intent.putExtra("mEndPoint",mendlatlon);
                    intent.putExtra("drivePath",drivePath);
                    startActivity(intent);
                }
                @Override
                public void OnJoinClick(RelOrder order) {
                    quitDialog();
                }
            });
        }
    }

    @Override
    public void setJoinResult(String result){
        ToastUtil.show(this,result);
    }

    //*************************************************************************
    //主页面点击退出登录时的弹出窗口，用于再次确认
    public void quitDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(Pa_OrderDetailActivity.this);
        dialog.setTitle("确认加入");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //初始化加入成员信息
                RelPassMems mems = new RelPassMems(order.getReOrSeq(),
                        STATIC_USERINFO.getUserSeq(),
                        STATIC_USERINFO.getName(),
                        STATIC_USERINFO.getPhone(),
                        0
                );
                presenter.JoinOrder(Pa_OrderDetailActivity.this, mems);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }
}
