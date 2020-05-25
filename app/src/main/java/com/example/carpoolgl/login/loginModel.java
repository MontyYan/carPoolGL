package com.example.carpoolgl.login;

import android.app.Activity;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.carpoolgl.Static.STATIC_CLASS;
import com.example.carpoolgl.base.activity.baseModel;
import com.example.carpoolgl.bean.User;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class loginModel extends baseModel implements baseModel.backJson{
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private static final int UPDATA_TV = 0;
    private static final String TAG="loginModel";

//    public static final String loginUrl="http://192.168.0.107:8080/login";
    public static final String loginUrl= STATIC_CLASS.getUrl() +"/login";

    public void login_test(final String phoneNum, final String password, final loginView loginV, TextView loginResult, ProgressBar Login_progress, Button Login_button){
        final mHandler handler = new mHandler(loginResult,Login_progress, Login_button);
        Log.i(TAG,phoneNum+" "+password);
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


    public static final String registerUrl= STATIC_CLASS.getUrl() +"/register";
    private static final String REG = "registerModel";
    private Activity activity;
    private loginView loginView;

    public void register(Activity activity,User user,loginView loginV){
        Log.i(REG+"00",user.toString());
        this.activity = activity;
        this.loginView = loginV;
        String jsonString = JSONObject.toJSONString(user);
        PostResponse(registerUrl,REG,jsonString,this);


    }

    @Override
    public void setJOSNObject(JSONObject jsonObject) {
        Integer result = 0;
        String info = "注册失败_";

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
//                loginView.

            }
        });
    }


    /*
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
    }*/

}
