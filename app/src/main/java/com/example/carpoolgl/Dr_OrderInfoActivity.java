package com.example.carpoolgl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.example.carpoolgl.NaviDriving.GPSNaviActivity;
import com.example.carpoolgl.base.activity.baseActivity;
import com.example.carpoolgl.publishedOrder.driver.driverOrderPresenter;
import com.example.carpoolgl.publishedOrder.driver.driverOrderView;
import com.example.carpoolgl.route.RouteActivity;
import com.example.carpoolgl.util.RouteUtil;

public class Dr_OrderInfoActivity extends baseActivity<driverOrderView, driverOrderPresenter> implements driverOrderView,
        View.OnClickListener
{

    private static final String TAG="DetailActivity";

    private TextView dr_order_seq_tv;
    private TextView dr_order_geton_tv;
    private TextView dr_order_getoff_tv;
    private TextView dr_order_time_tv;
    private TextView dr_order_num_tv;
    private TextView dr_order_money_tv;
    private Button dr_order_route_bt;
    private Button startNav_bt;

    private TextView dr_finding_order_tv;   //查询过程中信息提示，“查询中”，“无记录”
    private ProgressBar dr_finding_order_pb;
    private RecyclerView dr_order_RecycView;

    private String orderSeq;
    private String passenSeq;
    private String startAddr;
    private String endAddr;
    private String startTime;
    private Integer passenNum;
    private Integer money;
    private LatLonPoint mStartPoint = new LatLonPoint(39.942295,116.335891);//起点，39.942295,116.335891
    private LatLonPoint mEndPoint = new LatLonPoint(39.995576,116.481288);//终点，39.995576,116.481288
    private DrivePath drivePath;

    private driverOrderPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr__order_info);
        initController();
        getDrOrderInfo();

        upDateOrder();
    }

    public void initController(){
        dr_order_seq_tv = findViewById(R.id.dr_order_seq_tv);
        dr_order_geton_tv = findViewById(R.id.dr_order_geton_tv);
        dr_order_getoff_tv = findViewById(R.id.dr_order_getoff_tv);
        dr_order_money_tv = findViewById(R.id.dr_order_money_tv);
        dr_order_num_tv = findViewById(R.id.dr_order_num_tv);
        dr_order_route_bt =findViewById(R.id.dr_order_route_bt);
        dr_order_time_tv =findViewById(R.id.dr_order_time_tv);
        startNav_bt = findViewById(R.id.startNav_bt);

        dr_finding_order_tv = findViewById(R.id.dr_finding_order_tv);
        dr_finding_order_pb = findViewById(R.id.dr_finding_order_pb);
        dr_order_RecycView = findViewById(R.id.dr_order_RecycView);

        setOnClick();
    }

    public void setOnClick(){
        dr_order_route_bt.setOnClickListener(this);
        startNav_bt.setOnClickListener(this);
    }

    public void getDrOrderInfo(){
        Intent intent = getIntent();
        startAddr = intent.getStringExtra("startAddress");
        endAddr = intent.getStringExtra("endAddress");
        orderSeq = intent.getStringExtra("orderSeq");
        passenSeq = intent.getStringExtra("passenSeq");
        startTime = intent.getStringExtra("startTime");
        passenNum = intent.getIntExtra("passenNum",0);
        money = intent.getIntExtra("money",0);
        drivePath = intent.getParcelableExtra("drivePath");
        mStartPoint = intent.getParcelableExtra("mStartPoint");
        mEndPoint = intent.getParcelableExtra("mEndPoint");

        dr_order_seq_tv.setText("订单编号："+orderSeq);
        dr_order_geton_tv.setText(startAddr);
        dr_order_getoff_tv.setText(endAddr);
        dr_order_money_tv.setText(money+"");
        dr_order_time_tv.setText(startTime.substring(5));
        dr_order_num_tv.setText("人数："+passenNum);

    }

    public void upDateOrder(){
        presenter = getPresenter();
        presenter.DrgetPassenInfo(this,dr_finding_order_tv,dr_finding_order_pb,dr_order_RecycView,passenSeq);
        presenter.DrUpdateOrderInfo(this,orderSeq);
//        presenter
    }

    @Override
    public driverOrderPresenter createPresenter() {
        return new driverOrderPresenter();
    }

    @Override
    public driverOrderView createView() {
        return this;
    }

    @Override
    public void setDrOrderInfo(String totalMoney, String passenNum) {
        dr_order_num_tv.setText("人数："+passenNum);
        dr_order_money_tv.setText(totalMoney);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.dr_order_route_bt:
                intent = new Intent(this, RouteActivity.class);
                intent.putExtra("mStartPoint",mStartPoint);
                intent.putExtra("mEndPoint",mEndPoint);
                intent.putExtra("drivePath",drivePath);
                startActivity(intent);
                break;
            case R.id.startNav_bt:
                intent = new Intent(this, GPSNaviActivity.class);
                intent.putExtra("mStartPoint",mStartPoint);
                intent.putExtra("mEndPoint",mEndPoint);
                startActivity(intent);

//                Navi();
                break;
        }
    }


}
