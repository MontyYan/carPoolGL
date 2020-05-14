package com.example.carpoolgl.publish;

import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import com.alibaba.fastjson.JSON;
import com.amap.api.services.route.DriveStep;
import com.example.carpoolgl.Static.STATIC_CLASS;
import com.example.carpoolgl.bean.RelOrder;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class publishModel {
    public static final MediaType JSON_=MediaType.parse("application/json; charset=utf-8");
    private static final int UPDATA_TV = 0;
    private static final String TAG="publishModel";

//    public static final String publishUrl="http://192.168.0.107:8080/publish";
    public static final String publishUrl= STATIC_CLASS.getUrl()+"/publish";
//    public static final String publishUrl="http://192.168.31.203:8080/publish";

    public void publish(Context context, final RelOrder order, TextView text, ProgressBar progress, CardView card, final publishView publishV,int id){
        //List<DriveStep> steplists = order.getListStep();    //取出坐标信息

        final pubHandler handler = new pubHandler(context,text,progress,card,id);   //使用handler对发布页面ui进行更新
        //
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    String json = JSON.toJSONString(order);
                    RequestBody requestBody = RequestBody.create(JSON_,json);
                    Request request = new Request.Builder()
                            .url(publishUrl)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string(); //获取返回值
                    Log.i("PublishActivity",responseData);
                    publishV.showOrders();                          //回调activity方法

                    //更新ui
                    Message msg = new Message();
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
