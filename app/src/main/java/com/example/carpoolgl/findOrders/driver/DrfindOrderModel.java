package com.example.carpoolgl.findOrders.driver;

import android.app.Activity;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.carpoolgl.Static.STATIC_CLASS;
import com.example.carpoolgl.base.activity.baseModel;
import com.example.carpoolgl.bean.RelOrder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DrfindOrderModel extends baseModel {
    private static final String TAG="DrfindOrder_M";
    public static final String findUrl= STATIC_CLASS.getUrl()+"/DrfindOrders";
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");

    public void findOrders(final Activity activity,final DrfindOrderView drfindOrderV){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
//                    RequestBody requestBody = RequestBody.create(JSON,"");
                    Request request = new Request.Builder()
                            .url(findUrl)
//                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string(); //获取返回值
                    JSONObject jsonObject = JSONObject.parseObject(responseData);
                    Log.i(TAG,responseData);
                    Integer result = 0;
                    List<RelOrder> orders = new ArrayList<>();
                    if(!jsonObject.isEmpty()){
                        result = jsonObject.getInteger("result");
                        String s = jsonObject.getString("ordersData");
                        orders = jsonObject.parseArray(s,RelOrder.class);
                    }
                    final Integer tempResult = result;
                    final List<RelOrder> temporder = orders;

                    //主线程回调方法，更新页面，显示recyclerview
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            drfindOrderV.setRecyclerView(tempResult,temporder);
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();


    }



}
