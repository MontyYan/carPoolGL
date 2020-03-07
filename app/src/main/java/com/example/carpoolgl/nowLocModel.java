package com.example.carpoolgl;

import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class nowLocModel {

    AMapLocationClient client;
    AMapLocationClientOption option;
    public nowLocModel(){

    }

//    public void initLocaListener(AMapLocationClient client, AMapLocationClientOption option){
//        client = new AMapLocationClient(mContext);
//        client.setLocationListener(mLocationListener);
//        option = new AMapLocationClientOption();
//        option = new AMapLocationClientOption();
//        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
//        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置是否返回地址信息（默认返回地址信息）
//        option.setNeedAddress(true);
//        //设置是否只定位一次,默认为false
//        option.setOnceLocation(false);
//        //设置是否强制刷新WIFI，默认为强制刷新
//        option.setWifiActiveScan(true);
//        //设置是否允许模拟位置,默认为false，不允许模拟位置
//        option.setMockEnable(false);
//        //设置定位间隔,单位毫秒,默认为2000ms
//        option.setInterval(2000);
//        client.setLocationOption(option);
//        //启动定位
//        client.startLocation();
//    }
//
//    AMapLocationListener mLocationListener = new AMapLocationListener() {
//        @Override
//        public void onLocationChanged(AMapLocation aMapLocation) {
//            if(aMapLocation!=null){
//                if(aMapLocation.getErrorCode()==0){
////                    LatLng la = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
//                    StringBuffer buffer = new StringBuffer();
//                    buffer.append(aMapLocation.getCity() + " "
//                            + aMapLocation.getDistrict() + " "
//                            + aMapLocation.getStreet() + " "
//                            + aMapLocation.getStreetNum());
//                    Log.e("地理位置",buffer.toString());
//
//                    Toast.makeText(mContext, buffer.toString(), Toast.LENGTH_LONG).show();
//                    mLocationClient.stopLocation();
//                }
//                else {
//                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
//                    Log.e("AmapError", "location Error, ErrCode:"
//                            + aMapLocation.getErrorCode() + ", errInfo:"
//                            + aMapLocation.getErrorInfo());
//                    Toast.makeText(mContext, "定位失败", Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }
//    };

}
