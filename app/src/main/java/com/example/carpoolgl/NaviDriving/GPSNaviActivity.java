package com.example.carpoolgl.NaviDriving;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.core.LatLonPoint;
import com.example.carpoolgl.R;
import com.example.carpoolgl.util.RouteUtil;
import com.example.carpoolgl.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class GPSNaviActivity extends BaseActivity implements View.OnClickListener{

    protected final List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    protected final List<NaviLatLng> endList = new ArrayList<NaviLatLng>();

    private AMap aMap;
    private Button route_over_bt;

    private TextView tips_tv;
    private ProgressBar requesting_pb;
    private Button finish_cancel_bt;
    private Button finish_dialog_bt;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    private String orderSeq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsnavi);

//        Intent intent = getIntent();
//        orderSeq = intent.getStringExtra("orderSeq");
        initController();
        getRouteInfo();
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);

        AMapNaviViewOptions options = new AMapNaviViewOptions();
        options.setScreenAlwaysBright(false);
        options.setAutoDrawRoute(false);
        mAMapNaviView.setViewOptions(options);

    }

    public void initController(){
        mAMapNaviView = findViewById(R.id.navi_view);
        route_over_bt = findViewById(R.id.route_over_bt);
        route_over_bt.setOnClickListener(this);
    }


    public void getRouteInfo(){
        Intent intent = getIntent();
        LatLonPoint spoint  = intent.getParcelableExtra("mStartPoint");
        LatLonPoint epoint  = intent.getParcelableExtra("mEndPoint");
        startList.add(RouteUtil.PointToNaviLL(spoint));
        endList.add(RouteUtil.PointToNaviLL(epoint));
        orderSeq = intent.getStringExtra("orderSeq");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.route_over_bt:
                setDialog();

                break;
        }
    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        /**
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
         */
        int strategy = 0;
        try {
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.setCarNumber("桂", "DFZ588");
//        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);
        mAMapNavi.calculateDriveRoute(startList, endList, null, strategy);

    }

    @Override
    public void onCalculateRouteSuccess(int[] ids) {
        //开启实时导航
        super.onCalculateRouteSuccess(ids);
        //开启导航
//        mAMapNavi.startNavi(NaviType.GPS);
    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {
        aMap = mAMapNaviView.getMap();

        int[] routIds = aMapCalcRouteResult.getRouteid();
        int routeId = routIds[0];

        AMapNaviPath aMapNaviPath = mAMapNavi.getNaviPaths().get(routeId);

        RouteOverLay routeOverLay = new RouteOverLay(aMap, aMapNaviPath, this);

        routeOverLay.addToMap();

        mAMapNavi.startNavi(NaviType.GPS);
    }


    public void finishRequest(){
        getPresenter().OverOrder(this,orderSeq);
        tips_tv.setVisibility(View.GONE);
        requesting_pb.setVisibility(View.VISIBLE);
        finish_dialog_bt.setVisibility(View.GONE);
        finish_cancel_bt.setVisibility(View.GONE);
    }

    @Override
    public void Finishresponse(Integer result,String response) {
        ToastUtil.show(this,"结束行程成功");
        dialog.dismiss();
        finish();
    }


    public void setDialog(){
        builder = new AlertDialog.Builder(this);
        dialog = builder.create();
        dialog.setTitle("结束行程");
        View view = LayoutInflater.from(this).inflate(R.layout.finishorder_dialog,null);
        tips_tv = view.findViewById(R.id.tips_tv);
        requesting_pb = view.findViewById(R.id.requesting_pb);
        finish_dialog_bt = view.findViewById(R.id.finish_dialog_bt);
        finish_cancel_bt = view.findViewById(R.id.finish_cancel_bt);

        tips_tv.setText("确定结束行程");

        tips_tv.setVisibility(View.VISIBLE);
        requesting_pb.setVisibility(View.GONE);
        finish_dialog_bt.setVisibility(View.VISIBLE);
        finish_cancel_bt.setVisibility(View.VISIBLE);

        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        finish_dialog_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(GPSNaviActivity.this,"确定结束");
                finishRequest();
            }
        });
        finish_cancel_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setCanceledOnTouchOutside(true);
                dialog.dismiss();
            }
        });


    }

    public void overOrder(){


    }

}
