package com.example.carpoolgl.join;

import android.app.Activity;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.carpoolgl.Static.STATIC_CLASS;
import com.example.carpoolgl.base.activity.baseModel;
import com.example.carpoolgl.bean.CarInfo;

public class joinDrModel extends baseModel implements baseModel.backJson{
    private static final String TAG = "joinDrModel";
    private static final String URL = STATIC_CLASS.getUrl() +"/JoinDriver";


    private Activity activity;
    private joinDrView joinDrView;

    public void JoinDriver(Activity activity, final CarInfo carInfo, final joinDrView joinDrV){
        Log.i(TAG,carInfo.toString());
        this.activity = activity;
        this.joinDrView = joinDrV;
        String jsonString = JSONObject.toJSONString(carInfo);

        //post请求
        PostResponse(URL,TAG,jsonString,this);

    }

    @Override
    public void setJOSNObject(JSONObject jsonObject) {
        Integer result = 0;
        String info = "加入失败_";

        if(!jsonObject.isEmpty()){
            result = jsonObject.getInteger("result");
            info = jsonObject.getString("info");
        }

        final Integer tempresult = result;
        final String tempInfo = info;

        //回主线程进行更新
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                joinDrView.JoinDrResult(tempresult,tempInfo);

            }
        });


    }
}
