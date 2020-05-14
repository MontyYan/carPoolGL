package com.example.carpoolgl.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.example.carpoolgl.DrSearchOrderActivity;
import com.example.carpoolgl.Dr_OrderInfoActivity;
import com.example.carpoolgl.Pa_OrderInfoActivity;
import com.example.carpoolgl.R;
import com.example.carpoolgl.SearchActivity;
import com.example.carpoolgl.base.fragment.baseFragment;
import com.example.carpoolgl.bean.RelOrder;
import com.example.carpoolgl.fragment.driver.driverPresenter;
import com.example.carpoolgl.fragment.driver.driverView;
import com.example.carpoolgl.nowLoc.nowLocPresenter;
import com.example.carpoolgl.nowLoc.nowLocView;
import com.example.carpoolgl.util.RouteUtil;
import com.example.carpoolgl.util.ToastUtil;

public class DriverFragment extends baseFragment<driverView, driverPresenter> implements driverView, nowLocView,View.OnClickListener {

    //根据身份判定跳转的detail页面
    private static final int DRIVERID = 2;
    private nowLocPresenter nowLocP;
    private TextView nowAddress;    //记录当前位置
    private LatLonPoint nowLatLon = new LatLonPoint(39.995576,116.481288);     //记录当前经纬度
    private CardView searchExist_cv;    //司机查看已发布行程
    private CardView driverPublish_cv;  //司机自己发布行程
    private String nowLocation;         //当前地址信息记录，用于传给searchActivity

    //当前发布订单控件
    private CardView dr_published_order_cv;
    private TextView dr_start_loc_tv;
    private TextView dr_end_loc_tv;
    private TextView dr_date_tv;
    private TextView dr_num_tv;
    private Button dr_orderDetail_bt;
    private TextView dr_order_id_tv;
    private TextView dr_money_tv;

    private String  orderSeq;
    private String passenSeq;
    private String  startAddr;
    private String  endAddr;
    private String  startTime;
    private Integer  passenNum;
    private Integer  money;
    private DrivePath drivePath;
    private LatLonPoint mStartPoint = new LatLonPoint(39.942295,116.335891);//起点，39.942295,116.335891
    private LatLonPoint mEndPoint = new LatLonPoint(39.995576,116.481288);//终点，39.995576,116.481288

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initController(view);
//        driver_tv = view.findViewById(R.id.driver_tv);
        init_Info_order();
        location();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Context context = getActivity();
        switch (v.getId()){
            case R.id.searchExist_cv:
                intent = new Intent(context, DrSearchOrderActivity.class);
                startActivity(intent);

                break;
            case R.id.driverPublish_cv:
                intent = new Intent(context, SearchActivity.class);
                intent.putExtra("Edit_select",true);    //true 设置编辑框为‘getoff’
                intent.putExtra("nowLocation",nowLocation);   //传递当前地址
                intent.putExtra("nowLatLon",nowLatLon);       //传递当前经纬度
                intent.putExtra("identity",DRIVERID);       //传递当前身份
                context.startActivity(intent);
                break;
            case R.id.dr_orderDetail_bt:
                intent = new Intent(getActivity(), Dr_OrderInfoActivity.class);
                intent.putExtra("startAddress",startAddr);
                intent.putExtra("endAddress",endAddr);
                intent.putExtra("orderSeq",orderSeq);
                intent.putExtra("passenSeq",passenSeq);
                intent.putExtra("startTime",startTime);
                intent.putExtra("passenNum",passenNum);
                intent.putExtra("money",money);
                intent.putExtra("drivePath",drivePath);
                intent.putExtra("mStartPoint",mStartPoint);
                intent.putExtra("mEndPoint",mEndPoint);
                startActivity(intent);
                break;

        }
    }

    @Override
    public driverPresenter createPresenter() {
        return new driverPresenter();
    }

    @Override
    public driverView createView() {
        return this;
    }

    public void initController(View view){
        nowAddress = view.findViewById(R.id.nowAddress_driver_tv);
        searchExist_cv = view.findViewById(R.id.searchExist_cv);
        driverPublish_cv = view.findViewById(R.id.driverPublish_cv);
        dr_published_order_cv = view.findViewById(R.id.dr_published_order_cv);
        dr_start_loc_tv = view.findViewById(R.id.dr_start_loc_tv);
        dr_end_loc_tv = view.findViewById(R.id.dr_end_loc_tv);
        dr_date_tv = view.findViewById(R.id.dr_date_tv);
        dr_orderDetail_bt = view.findViewById(R.id.dr_orderDetail_bt);
        dr_order_id_tv = view.findViewById(R.id.dr_order_id_tv);

        setOnClick();
    }
    public void setOnClick(){
        searchExist_cv.setOnClickListener(this);
        driverPublish_cv.setOnClickListener(this);
        dr_orderDetail_bt.setOnClickListener(this);
    }

    //定位,实现位置信息显示在tv
    public void location(){
        nowLocP = new nowLocPresenter(getActivity());
        nowLocP.attachView(this);
    }
    //定位P层回调，设置地址信息
    @Override
    public void setGetonText(String addr) {
        nowAddress.setText(addr);   //ui信息更新
        nowLocation = addr;     //将定位数据保存，用于传给下一个activity
    }
    //定位P层回调，设置经纬度
    @Override
    public void setLatLon(double lat, double lon) {
        nowLatLon.setLatitude(lat);
        nowLatLon.setLongitude(lon);
    }

    //销毁
    @Override
    public void onDestroy() {
        super.onDestroy();
        nowLocP.destory();
        nowLocP.detachView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //实现初始化已发布订单信息与已登录用户信息
    public void init_Info_order(){
        getPresenter().findDr_OrderInfo(getActivity());
    }

    @Override
    public void SetOrder(RelOrder order) {
        orderSeq = order.getReOrSeq();
        startAddr = order.getStartLoc();
        endAddr = order.getEndLoc();
        startTime = order.getStartTime();
        passenNum = order.getPassNum();
        money = order.getMoney();
        passenSeq = order.getRelPassSeq();
        drivePath = RouteUtil.getDrivePath(order.getListSteps());
        mStartPoint = RouteUtil.getLatLon(order.getStartLonLat());
        mEndPoint = RouteUtil.getLatLon(order.getEndLonLat());

        dr_order_id_tv.setText(orderSeq);
        dr_published_order_cv.setVisibility(View.VISIBLE);
        dr_start_loc_tv.setText(startAddr);
        dr_end_loc_tv.setText(endAddr);
        dr_date_tv.setText(startTime.substring(5));
    }

}
