package com.example.carpoolgl.route;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RouteSearch;
import com.example.carpoolgl.R;
import com.example.carpoolgl.util.AMapUtil;
import com.example.carpoolgl.util.ToastUtil;

import java.util.List;

public class RouteActivity extends AppCompatActivity implements AMap.OnMapClickListener,
        AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener,
        AMap.InfoWindowAdapter{

    /*
    * 用于显示地图路线，单独一个页面
    * */
    private AMap aMap;
    private MapView mapView;
    private Context mContext;
    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private LatLonPoint mStartPoint = new LatLonPoint(39.942295,116.335891);//起点，39.942295,116.335891
    private LatLonPoint mEndPoint = new LatLonPoint(39.995576,116.481288);//终点，39.995576,116.481288
    private DrivePath drivePath;
    private final int ROUTE_TYPE_DRIVE = 2;
    private ProgressDialog progDialog = null;// 搜索时进度条

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        mContext = this.getApplicationContext();
        mapView = findViewById(R.id.route_map);
        mapView.onCreate(savedInstanceState);
        Intent intent = getIntent();
//        LatLonPoint mStartPoint,mEndPoint;
        mStartPoint = intent.getParcelableExtra("mStartPoint");
        mEndPoint = intent.getParcelableExtra("mEndPoint");
        drivePath = intent.getParcelableExtra("drivePath");
        Log.i("findOrder_rou",drivePath.toString());
        List<DriveStep> driveList = drivePath.getSteps();
//        Log.i("findOrder_rou>",driveList.get(0).getPolyline().get(0).getLatitude()+"");
        for(DriveStep dr:driveList){
            List<LatLonPoint> ll = dr.getPolyline();
            int i=0;
            for(LatLonPoint lp:ll){
                Log.i("findOrder_rou",i+" "+lp.toString());
                i++;
            }
        }
//        mDriveRouteResult = intent.getParcelableExtra("mDriveRouteResult");
        init();
        setfromandtoMarker();
        drawRoute();
//        searchRouteResult(ROUTE_TYPE_DRIVE,RouteSearch.DrivingDefault);
    }
    //**初始化AMap对象******************************************************************************
    public void init(){
        if(aMap == null){
            aMap = mapView.getMap();
        }
        registerListener();
        mRouteSearch = new RouteSearch(this);
//        mRouteSearch.setRouteSearchListener(this);
    }

    private void setfromandtoMarker() {
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
    }
    /**
     * 注册监听
     */
    private void registerListener() {
        aMap.setOnMapClickListener(RouteActivity.this);
        aMap.setOnMarkerClickListener(RouteActivity.this);
        aMap.setOnInfoWindowClickListener(RouteActivity.this);
        aMap.setInfoWindowAdapter(RouteActivity.this);

    }

    //********************************************************************************
    public void searchRouteResult(int routeType, int mode){
        if(mStartPoint ==null){
            ToastUtil.show(mContext,"定位中");
            return;
        }
        if(mEndPoint==null){
            ToastUtil.show(mContext,"终点未设置");
        }
        showProgressDialog();
//        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint,mEndPoint);
//        if(routeType==ROUTE_TYPE_DRIVE){    //驾车路径规划
//            // 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
//            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo,mode,null,
//                    null,"");
//            mRouteSearch.calculateDriveRouteAsyn(query);//异步路径规划驾车模式查询
//        }
        drawRoute();
    }

    //********************************************************************************

    public void drawRoute(){
        if(mStartPoint ==null){
            ToastUtil.show(mContext,"定位中");
            return;
        }
        if(mEndPoint==null){
            ToastUtil.show(mContext,"终点未设置");
        }
        showProgressDialog();
        if(drivePath==null){
            return;
        }else{
            dissmissProgressDialog();
            aMap.clear();
            //*****************
            List<DriveStep> driveList = drivePath.getSteps();
            for(DriveStep dr:driveList){
                List<LatLonPoint> ll = dr.getPolyline();
                int i=0;
                for(LatLonPoint lp:ll){
                    Log.i("route",i+" "+lp.toString());
                    i++;
                }
                aMap.addMarker(new MarkerOptions()
                        .position(AMapUtil.convertToLatLng(ll.get(0)))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
            }
            //*****************
//            DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
//                    mContext, aMap, drivePath,
//                    mDriveRouteResult.getStartPos(),
//                    mDriveRouteResult.getTargetPos(), null);
//            drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
//            drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
//            drivingRouteOverlay.removeFromMap();
//            drivingRouteOverlay.addToMap();
//            drivingRouteOverlay.zoomToSpan();
            DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                    mContext, aMap, drivePath,
                    mStartPoint,
                    mEndPoint, null);
            drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
            drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
            drivingRouteOverlay.removeFromMap();
            drivingRouteOverlay.addToMap();
            drivingRouteOverlay.zoomToSpan();
        }

    }

    //**AMap.OnMapClickListener*******************************************************
    @Override
    public void onMapClick(LatLng latLng) {

    }

    //**AMap.OnMarkerClickListener*********************************************************
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    //**AMap.OnInfoWindowClickListener*******************************************************
    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    //**AMap.InfoWindowAdapter*******************************************************
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    //*********************************************************
    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null) {
            progDialog = new ProgressDialog(this);
        }
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }
    //*********************************************************

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    //*********************************************************
    //*********************************************************
}
