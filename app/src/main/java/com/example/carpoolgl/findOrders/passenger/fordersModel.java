package com.example.carpoolgl.findOrders.passenger;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.carpoolgl.Static.STATIC_CLASS;
import com.example.carpoolgl.base.activity.baseModel;
import com.example.carpoolgl.bean.RelOrder;
import com.example.carpoolgl.bean.RelPassMems;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class fordersModel extends baseModel {
    private static final int EXIST_DATAS=1; //查到数据
    private static final int NO_DATAS=0;    //没有数据/查询出错
    private static final String TAG="findOrder_M";
    public static final MediaType JSON_=MediaType.parse("application/json; charset=utf-8");
//    public static final String findUrl="http://192.168.0.107:8080/findOrders";
//    public static final String findUrl="http://192.168.31.203:8080/findOrders";
    public static final String findUrl= STATIC_CLASS.getUrl()+"/findOrders";
    public static final String joinUrl= STATIC_CLASS.getUrl()+"/joinOrder";

//    public void findOrder(final Context context, TextView textView, ProgressBar progress, RecyclerView recyclerView, final fordersView fordersV, final RelOrder order){
    public void findOrder(final Context context,  final fordersView fordersV, final RelOrder order){
//    public void findOrder(final fordersView fordersV, final RelOrder order){
        Log.i(TAG,order.toString());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        OkHttpClient client = new OkHttpClient();
                        String json = JSON.toJSONString(order);
                        Log.i(TAG,json);
                        RequestBody requestBody = RequestBody.create(JSON_,json);
                        Request request = new Request.Builder()
                                .url(findUrl)
                                .post(requestBody)
                                .build();
                        Response response = client.newCall(request).execute();
                        String responseData = response.body().string(); //获取返回值
                        Log.i(TAG,responseData);


//                        JSONObject jsonObject = PostResponse(findUrl,json,TAG);
                        JSONObject jsonObject = JSONObject.parseObject(responseData);
                        Integer result = 0;
                        List<RelOrder> orders = new ArrayList<>();
                        if(!jsonObject.isEmpty()){
                            result = jsonObject.getInteger("result");
                            String s = jsonObject.getString("ordersData");
                            Log.i(TAG,s);
                            orders = jsonObject.parseArray(s,RelOrder.class);
                            Log.i(TAG+">>>>>>>>",orders.toString());
                        }

                        final Integer temp = result;
                        final List<RelOrder> order = orders;
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fordersV.setRecyclerView(temp,order);
                            }
                        });

//                        mhandler.sendMessage(msg);
//                        Looper.loop();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
    }

    public void joinOrder(final Activity activity, final RelPassMems mems, final fordersView fordersV){
        Log.i(TAG,mems.toString());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    String json = JSON.toJSONString(mems);
                    Log.i(TAG,json);
                    RequestBody requestBody = RequestBody.create(JSON_,json);
                    Request request = new Request.Builder()
                            .url(joinUrl)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string(); //获取返回值

                    JSONObject jsonObject = JSONObject.parseObject(responseData);
                    Integer result = 0;

                    if(!jsonObject.isEmpty()){
                        result = jsonObject.getInteger("result");
                    }
                    String info ="";
                    switch (result){
                        case 0:
                            info = "加入失败";
                            break;
                        case 1:
                            info = "加入成功";
                            break;
                    }
                    final String temp = info;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fordersV.setJoinResult(temp);
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        }).start();
    }
}
