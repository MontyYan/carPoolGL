package com.example.carpoolgl.dragLoc;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.example.carpoolgl.R;

public class chooseLocPresenter implements
        LocationSource,
        AMapLocationListener,
        GeocodeSearch.OnGeocodeSearchListener,
        AMap.OnCameraChangeListener {

    private Context mcontext;
    private chooseLocView chooseLocV;
    private int[] Screen;

    private Marker mGPSMarker;
    private MarkerOptions markOptions;
    private AMap aMap;
    private MapView mapView;
    private AMapLocationClient mLocClient;
    private LocationSource.OnLocationChangedListener mLocListener;
    private AMapLocationClientOption mLocOption;
    private AMapLocation location_;
    private String address;
    private LatLng latLng;
    private GeocodeSearch mGeocoSearch;
    //坐标，传给DetailActivity，
    private double latitude;
    private double longitude;

    public chooseLocPresenter(MapView mapView){
        this.mapView = mapView;
    }

    public void initChooseLocPresenter(Context context,int[] Screen){
        this.mcontext = context;
//        this.mapView = mapView;
        this.Screen = Screen;
        initMap();
    }

    //********************************************************************************************************
    private void initMap() {
        mGeocoSearch = new GeocodeSearch(mcontext);
        aMap = mapView.getMap();
        aMap.setLocationSource(this);
        aMap.setOnCameraChangeListener(this);
        mGeocoSearch.setOnGeocodeSearchListener(this);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));//缩放比例
        //设置style把定位marker外面的圈去掉
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.strokeColor(Color.argb(0,0,0,0));
        myLocationStyle.radiusFillColor(Color.argb(0,0,0,0));
        aMap.setMyLocationStyle(myLocationStyle);
        //设置amap属性
        UiSettings settings = aMap.getUiSettings();
        settings.setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
    }
    //******************************************************************************************************

    public void setMarker(LatLng latLng, String title, String content){
        if(mGPSMarker!=null){
            mGPSMarker.remove();
//            aMap.clear();     //可以直接移除当前位置图标
//            Log.e("移除maker",mGPSMarker.toString());
        }

        markOptions = new MarkerOptions();
        markOptions.draggable(true);
        markOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(mcontext.getResources(),R.drawable.round_location_on_black_24))).anchor(0.5f,0.7f);
        //设置角标
        mGPSMarker = aMap.addMarker(markOptions);
        //设置marker在屏幕的像素坐标
        mGPSMarker.setPosition(latLng);
        mGPSMarker.setTitle(title);
        mGPSMarker.setSnippet(content); //江苏省苏州市姑苏区双塔街道肇源弄杨枝二村苏大家属区     内容有些多，再减
        //设置像素坐标
        mGPSMarker.setPositionByPixels(Screen[0]/2,Screen[1]/2);
        mGPSMarker.setVisible(true);
        if(!TextUtils.isEmpty(content)){
//            Log.e("textutil",TextUtils.isEmpty(content)+"");
            mGPSMarker.showInfoWindow();
        }
        mapView.invalidate();

    }

    //***AMapLocationListener***************************************************************************************
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        location_=aMapLocation;
        if(mLocListener!=null&&location_!=null){
            if(location_!=null&&location_.getErrorCode()==0){
                mLocListener.onLocationChanged(location_);// 显示系统箭头
                LatLng la = new LatLng(location_.getLatitude(), location_.getLongitude());
//                setMarker(la, location_.getCity(), location_.getAddress());
                StringBuffer buffer = new StringBuffer();
                buffer.append(aMapLocation.getCountry() + " "
                        + aMapLocation.getProvince() + " "
                        + aMapLocation.getCity() + " "
                        + aMapLocation.getProvince() + " "
                        + aMapLocation.getDistrict() + " "
                        + aMapLocation.getStreet() + " "
                        + aMapLocation.getStreetNum());
                Toast.makeText(mcontext, buffer.toString(), Toast.LENGTH_SHORT).show();
                mLocClient.stopLocation();
            }
            else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                Toast.makeText(mcontext, "定位失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //***AMap.OnCameraChangeListener****************************************************************************
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        latLng = cameraPosition.target;
        latitude = latLng.latitude;
        longitude = latLng.longitude;
        chooseLocV.setLatLon(longitude,latitude);
        Log.e("latitude",latitude+"");
        Log.e("longitude",longitude+"");
        getAddress(latLng);
    }

    //***LocationSource********************************************************************************************
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mLocListener = onLocationChangedListener;
        //初始化定位
        mLocClient = new AMapLocationClient(mcontext);
        //设置定位回调监听
        mLocClient.setLocationListener(this);
        //初始化定位参数
        mLocOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocClient.setLocationOption(mLocOption);
        //启动定位
        mLocClient.startLocation();
    }

    @Override
    public void deactivate() {
            mLocListener = null;
    }

    //***GeocodeSearch.OnGeocodeSearchListener****************************************************************************
    @Override
    public void onRegeocodeSearched(RegeocodeResult Result, int rCode) {
        if(rCode==1000){
            if(Result!=null&&Result.getRegeocodeAddress()!=null
                    && Result.getRegeocodeAddress().getFormatAddress()!=null){
                String[] ad = Result.getRegeocodeAddress().getFormatAddress().split("道");
//                address = Result.getRegeocodeAddress().getDistrict()+" "+
//                        Result.getRegeocodeAddress().getTownship()+" "+
//                        ad[1];
                address = Result.getRegeocodeAddress().getDistrict()+" "+ ad[1];
                Log.e("逆编码回调的地址0",address);
                Log.e("逆编码回调的地址1",Result.getRegeocodeAddress().getFormatAddress());//江苏省苏州市姑苏区双塔街道杨枝二村苏大家属区18幢杨枝二村苏大家属区
                Log.e("逆编码回调的地址2",Result.getRegeocodeAddress().getBuilding());//
                Log.e("逆编码回调的地址3",Result.getRegeocodeAddress().getCity());//苏州市
                Log.e("逆编码回调的地址4",Result.getRegeocodeAddress().getCountry());//中国
                Log.e("逆编码回调的地址5",Result.getRegeocodeAddress().getDistrict());//姑苏区
                Log.e("逆编码回调的地址6",Result.getRegeocodeAddress().getNeighborhood());//
                Log.e("逆编码回调的地址7",Result.getRegeocodeAddress().getProvince());//江苏省
                Log.e("逆编码回调的地址8",Result.getRegeocodeAddress().getTownship());//双塔街道
            }
            chooseLocV.LocInfo_SetText(address);

            setMarker(latLng,location_.getCity(),address);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    public void attachView(chooseLocView view){
        this.chooseLocV = view;
    }

    public void detachView(){
        this.chooseLocV = null;
    }

    public void destory(){
        if(mLocClient!=null)
        mLocClient.stopLocation();
        mapView.onDestroy();
    }

    public void resume(){
        mapView.onResume();
    }

    public void pause(){
        mapView.onPause();
    }

    public void saveInstanceState(Bundle outState){
        mapView.onSaveInstanceState(outState);
    }

    public void getAddress(final LatLng latLng){
        RegeocodeQuery query = new RegeocodeQuery(
                new LatLonPoint(latLng.latitude,latLng.longitude),
                200,
                GeocodeSearch.AMAP);
        mGeocoSearch.getFromLocationAsyn(query);
    }

}
