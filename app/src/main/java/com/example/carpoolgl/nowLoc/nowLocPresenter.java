package com.example.carpoolgl.nowLoc;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

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
                    buffer.append(aMapLocation.getCity() + " "
                            + aMapLocation.getDistrict() + " "
                            + aMapLocation.getStreet() + " "
                            + aMapLocation.getStreetNum());
                    Log.e("地理位置",buffer.toString());

                    nowLocView.setGetonText(buffer.toString());
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
