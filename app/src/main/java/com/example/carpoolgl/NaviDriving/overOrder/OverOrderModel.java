package com.example.carpoolgl.NaviDriving.overOrder;

import android.app.Activity;

import com.alibaba.fastjson.JSONObject;
import com.example.carpoolgl.Static.STATIC_CLASS;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OverOrderModel {

    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "OverOrderModel";
    public static final String FinishUrl= STATIC_CLASS.getUrl() +"/finishRequest";

    public void FinishOrder(final Activity activity, final String seq, final OverOrderView overOrderV){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("orderSeq",seq);

                    RequestBody requestBody = RequestBody.create(JSON,jsonObject.toJSONString());
                    Request request = new Request.Builder()
                            .url(FinishUrl)
                            .post(requestBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    String data = response.body().string();

                    Integer result = 0;
                    String info ="";
                    jsonObject = JSONObject.parseObject(data);

                    if(!jsonObject.isEmpty()){
                        result = jsonObject.getInteger("result");
                        info = jsonObject.getString("info");
                    }
                    final Integer tempResult = result;
                    final String tempInfo = info;

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            overOrderV.Finishresponse(tempResult,tempInfo);
                        }
                    });


                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();

    }
}
