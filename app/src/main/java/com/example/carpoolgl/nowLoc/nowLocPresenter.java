package com.example.carpoolgl.nowLoc;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;

public class nowLocPresenter {
    private AMapLocationClient mClient;
    private AMapLocationClientOption mOption;
    private Context mContext;

    private nowLocModel nowLocModel;
    private nowLocView nowLocView;

    public nowLocPresenter(Context context){
        this.mContext = context;
//        this.nowLocModel = new nowLocModel();
        initLocListener();
    }

    AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if(aMapLocation!=null){
                if(aMapLocation.getErrorCode()==0){
//                    LatLng la = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    StringBuffer buffer = new StringBuffer();
//                    buffer.append(aMapLocation.getCity() + " "  //苏州市
//                            + aMapLocation.getDistrict() + " "  //姑苏区
//                            + aMapLocation.getStreet() + " "    //肇源弄
//                            + aMapLocation.getStreetNum());     //33号
                    buffer.append(aMapLocation.getDistrict() + " "  //姑苏区
                                + aMapLocation.getAoiName());    //杨枝二村苏大家属区
                    Log.e("地理位置",buffer.toString());
                    Log.e("地理位置",aMapLocation.getAddress());//江苏省苏州市姑苏区肇源弄33号靠近杨枝二村苏大家属区
                    Log.e("地理位置",aMapLocation.getAoiName());//杨枝二村苏大家属区
                    Log.e("地理位置",aMapLocation.getCity());//苏州市
                    Log.e("地理位置",aMapLocation.getCountry());//中国
                    Log.e("地理位置",aMapLocation.getDescription());//在杨枝二村苏大家属区附近
                    Log.e("地理位置",aMapLocation.getDistrict());//姑苏区
                    Log.e("地理位置f",aMapLocation.getFloor());//
                    Log.e("地理位置de",aMapLocation.getLocationDetail());//#id:KLFZvY1RBTE52OEE0REFDcXRDa1VMS1pUQQ==#csid:3c449df8c24143b980146ab6b5d78b63
                    Log.e("地理位置", aMapLocation.getPoiName());//杨枝二村苏大家属区
                    Log.e("地理位置",aMapLocation.getProvider());//lbs
                    Log.e("地理位置",aMapLocation.getProvince());//江苏省
                    Log.e("地理位置",aMapLocation.getStreetNum());//33号


                    nowLocView.setGetonText(buffer.toString());
//                    LatLonPoint latLonPoint = new LatLonPoint(aMapLocation.getLongitude(),aMapLocation.getLatitude());
                    nowLocView.setLatLon(aMapLocation.getLongitude(),aMapLocation.getLatitude());
//                    Toast.makeText(mContext, buffer.toString(), Toast.LENGTH_LONG).show();
                    mClient.stopLocation();
                }
                else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                    Toast.makeText(mContext, "定位失败", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };
    public void initLocListener(){
        mClient = new AMapLocationClient(mContext);
        mClient.setLocationListener(mLocationListener);
        mOption = new AMapLocationClientOption();
        mOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mOption.setInterval(2000);
        mClient.setLocationOption(mOption);
        //启动定位
        mClient.startLocation();
    }

    public void attachView(nowLocView view){
        this.nowLocView = view;
    }

    public void detachView(){
        this.nowLocView = null;

    }

    public void destory(){
        this.mClient.stopLocation();
    }

}
