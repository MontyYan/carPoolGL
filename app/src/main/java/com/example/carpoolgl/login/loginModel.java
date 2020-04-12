package com.example.carpoolgl.login;

import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.example.carpoolgl.bean.User;
import com.example.carpoolgl.bean.User_;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class loginModel {
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private static final int UPDATA_TV = 0;

    public static final String loginUrl="http://192.168.0.107:8080/login";
    public void login_(final String phoneNum, final String password, final loginView loginV, TextView loginResult){
        final mHandler handler = new mHandler(loginResult);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    User u = new User(phoneNum,password,1);
//                    User u = new User("8008208820","123");
                    Gson gson = new Gson();
                    RequestBody requestBody = RequestBody.create(JSON,gson.toJson(u));
                    Log.i("user",gson.toJson(u));
                    Request request = new Request.Builder()
                            .url(loginUrl)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.i("LoginActivity",responseData);

                    loginV.onloginResult(responseData);
//                    responseData信息放入msg
                    Message msg = handler.obtainMessage();
                    msg.what = UPDATA_TV;
                    msg.obj = responseData;
                    Looper.prepare();
                    handler.sendMessage(msg);
                    Looper.loop();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
