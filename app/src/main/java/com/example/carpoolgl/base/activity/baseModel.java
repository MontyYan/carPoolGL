package com.example.carpoolgl.base.activity;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author: MontyYan
 * @date: 2020/5/18
 * @description:
 */

public class baseModel {
    public static final MediaType JSONTYPE = MediaType.parse("application/json; charset=utf-8");

//    //POST网络请求--------暂时未使用
//    public JSONObject PostResponse(String URL, String requestJson,String LOG_I){
//        JSONObject JsonObj;
//        OkHttpClient client = new OkHttpClient();
//        RequestBody requestBody = RequestBody.create(JSONTYPE,requestJson);
//        Request request = new Request.Builder()
//                .url(URL)
//                .post(requestBody)
//                .build();
//        try{
//            Response response = client.newCall(request).execute();
//            String respStr = response.body().toString();
//            Log.i(LOG_I,respStr);
//            JsonObj = JSONObject.parseObject(respStr);
//        }catch (Exception e){
//            e.printStackTrace();
//            Log.i(LOG_I,"获取返回数据出错");
//            JsonObj = new JSONObject();
//        }
//        return JsonObj;
//    }

    public void GetRequest(String URL, String requestJson){

    }

    public void PostResponse(final String URL,final String LOG_I,final String requestJson,final backJson backJ){
        Log.i(LOG_I+"b0",requestJson);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    JSONObject JsonObj;
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = RequestBody.create(JSONTYPE,requestJson);
                    Request request = new Request.Builder()
                            .url(URL)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String respStr = response.body().string();
                    Log.i(LOG_I+"b1",respStr);
                    JsonObj = JSONObject.parseObject(respStr);
                    //回调model层方法
                    backJ.setJOSNObject(JsonObj);
                }catch (Exception e){
                    JSONObject JsonObj;
                    e.printStackTrace();
                    Log.i(LOG_I+"b2","获取返回数据出错");
                    JsonObj = new JSONObject();
                    //回调model层方法
                    backJ.setJOSNObject(JsonObj);
                }

            }
        }).start();

    }

    public interface backJson{
           void setJOSNObject(JSONObject jsonObject);
    }



//    public void setBackJson(backJson backJson){
//        this.backJson = backJson;
//    }
}

