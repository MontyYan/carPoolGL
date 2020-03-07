package com.example.carpoolgl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class SearchActivity extends AppCompatActivity implements
        View.OnClickListener,
        LocationSource,
        AMapLocationListener,
        GeocodeSearch.OnGeocodeSearchListener,
        AMap.OnCameraChangeListener
{
    private View bottomSheet;
    private BottomSheetBehavior mBoSheetBehavior;
    private EditText getOn_Edit;
    private EditText getOff_Edit;
    private TextView choose_Tv;
    private Marker mGPSMarker;
    private MarkerOptions markOptions;
    private AMap aMap = null;
    private MapView mapView = null;
    private AMapLocationClient mLocationClient = null;
    private LocationSource.OnLocationChangedListener mListener=null;
    private AMapLocationClientOption mLocationOption=null;
    private AMapLocation location_;
    private String addressName;
    private LatLng latLng;
    private GeocodeSearch geocoderSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mapView = findViewById(R.id.map);
        getOn_Edit = findViewById(R.id.getOn_edit);
        getOff_Edit = findViewById(R.id.getOff_edit);
        choose_Tv = findViewById(R.id.drawer_loc);
        bottomSheet = findViewById(R.id.bottom_sheet);
        mBoSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBoSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mBoSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                bottomSheet.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = MotionEventCompat.getActionMasked(event);
                        switch (action){
                            case MotionEvent.ACTION_DOWN:
                                return false;
                                default:
                                    return true;
                        }
                    }
                });
            }
        });
        getOn_Edit.setOnClickListener(this);
        getOff_Edit.setOnClickListener(this);
        choose_Tv.setOnClickListener(this);
        initMap(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.getOn_edit:
                mBoSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                break;
            case R.id.getOff_edit:
                mBoSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.drawer_loc:
                mBoSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
        }
    }

    private void initMap(Bundle savedInstanceState) {
        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        geocoderSearch = new GeocodeSearch(this);
        aMap = mapView.getMap();
        aMap.setLocationSource(this);
        aMap.setOnCameraChangeListener(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
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

    public void setMarker(LatLng latLng, String title, String content){
        if(mGPSMarker!=null){
            mGPSMarker.remove();
//            aMap.clear();     //可以直接移除当前位置图标
//            Log.e("移除maker",mGPSMarker.toString());
        }
        Display display = getWindowManager().getDefaultDisplay();
        Point outsize = new Point();
        display.getSize(outsize);
        int Screenwidth = outsize.x;
        int ScreenHight = outsize.y;
//        Log.e("手机尺寸",Screenwidth+"-"+ScreenHight);
        markOptions = new MarkerOptions();
        markOptions.draggable(true);
        markOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.round_location_on_black_24))).anchor(0.5f,0.7f);
        //设置角标
        mGPSMarker = aMap.addMarker(markOptions);
        //设置marker在屏幕的像素坐标
        mGPSMarker.setPosition(latLng);
        mGPSMarker.setTitle(title);
        mGPSMarker.setSnippet(content); //江苏省苏州市姑苏区双塔街道肇源弄杨枝二村苏大家属区     内容有些多，再减
        //设置像素坐标
        mGPSMarker.setPositionByPixels(Screenwidth/2,ScreenHight/2);
        mGPSMarker.setVisible(true);
        if(!TextUtils.isEmpty(content)){
//            Log.e("textutil",TextUtils.isEmpty(content)+"");
            mGPSMarker.showInfoWindow();
        }
        mapView.invalidate();

    }
    //销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 停止定位
        mLocationClient.stopLocation();
        // 销毁地图
        mapView.onDestroy();
    }

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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        location_=aMapLocation;
        if(mListener!=null&&location_!=null){
            if(location_!=null&&location_.getErrorCode()==0){
                mListener.onLocationChanged(location_);// 显示系统箭头
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
                Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                mLocationClient.stopLocation();
            }
            else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
    @Override
    public void deactivate() {
        mListener = null;
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult Result, int rCode) {
        if(rCode==1000){
            if(Result!=null&&Result.getRegeocodeAddress()!=null
                    && Result.getRegeocodeAddress().getFormatAddress()!=null){
                String[] ad = Result.getRegeocodeAddress().getFormatAddress().split("道");
                addressName = Result.getRegeocodeAddress().getDistrict()+" "+
                            Result.getRegeocodeAddress().getTownship()+" "+
                            ad[1];
                Log.e("逆编码回调的地址0",addressName);
                Log.e("逆编码回调的地址1",Result.getRegeocodeAddress().getFormatAddress());
                Log.e("逆编码回调的地址2",Result.getRegeocodeAddress().getBuilding());
                Log.e("逆编码回调的地址2",ad[1]);
            }
            //地址名称太长，EditText一行放不下
            if(addressName.length()>18){
                getOn_Edit.setText(addressName.substring(0,18)+"...");
            }else{
                getOn_Edit.setText(addressName);
            }
            setMarker(latLng,location_.getCity(),addressName);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        latLng = cameraPosition.target;
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        Log.e("latitude",latitude+"");
        Log.e("longitude",longitude+"");
        getAddress(latLng);
    }

    public void getAddress(final LatLng latLng){
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude,latLng.longitude),200,GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }
}
