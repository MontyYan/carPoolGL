package com.example.carpoolgl.publishedOrder.driver;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.example.carpoolgl.Static.STATIC_CLASS;
import com.example.carpoolgl.base.activity.baseModel;
import com.example.carpoolgl.bean.RelOrder;
import com.example.carpoolgl.bean.RelPassMems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class driverOrderModel extends baseModel {
    private static final String TAG="driverOrderr_M";
    public static final MediaType JSON_=MediaType.parse("application/json; charset=utf-8");
    public static final String findUrl= STATIC_CLASS.getUrl()+"/findPassens";
    public static final String findorderUrl= STATIC_CLASS.getUrl()+"/findOrderBySeq";

    public void getPassenInfo(Context context, TextView textView, ProgressBar progressBar, RecyclerView recyclerView, final driverOrderView driverOrderV, final String passenSeq){
        Log.i(TAG,passenSeq);
        final driverHandler handler = new driverHandler(context,textView,progressBar,recyclerView);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Map<String,String> map  = new  HashMap<>();
                    map.put("seq",passenSeq);
                    String seqJson = JSONObject.toJSONString(map);

                    RequestBody requestBody = RequestBody.create(JSON_,seqJson);
                    Request request = new Request.Builder()
                            .url(findUrl)
                            .post(requestBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string(); //获取返回值
                    Log.i(TAG+"1",responseData);

                    JSONObject jsonObject = JSONObject.parseObject(responseData);
                    Integer result = 0;
                    List<RelPassMems> passMems = new ArrayList<>();
                    if(!jsonObject.isEmpty()){
                        result = jsonObject.getInteger("result");
                        String s = jsonObject.getString("passensData");
                        Log.i(TAG+"2",s);
                        passMems = JSONObject.parseArray(s,RelPassMems.class);
                        Log.i(TAG+"3",passMems.toString());
                    }
                    Message msg = handler.obtainMessage();
                    msg.what = result;
                    msg.obj = passMems;
                    Looper.prepare();
                    handler.sendMessage(msg);
                    Looper.loop();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getOrderInfo(final Activity activity, final String seq, final driverOrderView driverOrderV){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Map<String,String> map  = new  HashMap<>();
                    map.put("seq",seq);
                    String seqJson = JSONObject.toJSONString(map);

                    RequestBody requestBody = RequestBody.create(JSON_,seqJson);
                    Request request = new Request.Builder()
                            .url(findorderUrl)
                            .post(requestBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string(); //获取返回值
                    Log.i(TAG+"O",responseData);
                    JSONObject jsonObject = JSONObject.parseObject(responseData);
                    Integer result = 0;
                    String totalMoney="";
                    String passenNum="";
                    if(!jsonObject.isEmpty()){
                        result = jsonObject.getInteger("result");
                        totalMoney = jsonObject.getString("totalMoney");
                        passenNum = jsonObject.getString("passenNum");
                        Log.i(TAG+"O",totalMoney+" "+passenNum);
                    }
                    final String tempTotal = totalMoney;
                    final String tempNum = passenNum;

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            driverOrderV.setDrOrderInfo(tempTotal,tempNum);
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        }).start();

    }

}
