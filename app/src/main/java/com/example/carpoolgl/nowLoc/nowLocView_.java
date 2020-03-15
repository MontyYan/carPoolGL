package com.example.carpoolgl.nowLoc;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class nowLocView_ {
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    private Context mContext;

    public nowLocView_(Context context){
        this.mContext = context;
        initLocaListener();
    }

    public AMapLocationClient getmLocationClient() {
        return mLocationClient;
    }

    public void setmLocationClient(AMapLocationClient mLocationClient) {
        this.mLocationClient = mLocationClient;
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

                    Toast.makeText(mContext, buffer.toString(), Toast.LENGTH_LONG).show();
                    mLocationClient.stopLocation();
                }
                else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                    Toast.makeText(mContext, "定位失败", Toast.LENGTH_LONG).show();
                }

            }
        }
    };

    private void initLocaListener(){
        mLocationClient = new AMapLocationClient(mContext);
        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();
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
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

}
