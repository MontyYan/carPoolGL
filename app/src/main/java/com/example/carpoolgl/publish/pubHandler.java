package com.example.carpoolgl.publish;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.services.route.DriveStep;
import com.example.carpoolgl.bean.RelOrder;

import org.w3c.dom.Text;

import java.util.List;

public class pubHandler extends Handler {

    //用于测试 删
    private static final int PUBLISH_SUS = 1;
    //用于测试 删
    private static final int ORDER_JSON = 9;

    private static final int UPDATA_TV = 0;
    private static final int PASSEN_IDENTITY = 5;
    private static final int DR_IDENTITY = 6;

    /*
    * 身份
    * 5  代表乘客
    * 6  代表司机
    * -1 出错
    * 用于本地判断保存发布的订单是司机/乘客发布的
    * */
    private int IDENTITY = -1; //
    private LinearLayout linearLayout;
    private Context context;
    private TextView textView;
    private ProgressBar progress;
    private CardView cardView;
    private RelOrder order;


    public pubHandler(){
    }

    public pubHandler(Context context,TextView textView, ProgressBar progress, CardView card,int id){
        this.context = context;
        this.cardView = card;
        this.textView = textView;
        this.progress = progress;
        this.IDENTITY = id;
    }

    //用于测试 删
    public pubHandler(RelOrder order){
        this.order = order;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            //测试，暂时未使用
            case PUBLISH_SUS:
                linearLayout.setVisibility(View.GONE);
                cardView.setVisibility(View.VISIBLE);
                break;
                //输出测试用
            case ORDER_JSON:
                RelOrder relOrder = (RelOrder) msg.obj;
                String ss = JSON.toJSONString(relOrder);
//                Log.i("PublishActivity",ss);
                i("PublishActivity>>>>>>>>",ss);
                break;
            case UPDATA_TV:
                String response = (String)msg.obj;//接收到的json数据
                JSONObject jsonObject = JSONObject.parseObject(response);   //解析json
                Integer result = jsonObject.getInteger("publishResult");
                String orserJson = jsonObject.getString("orderInfo");
//                RelOrder order = JSONObject.parseObject(orserJson,RelOrder.class);
                updataUI(result,orserJson);
                break;
        }
    }

    //发布页面更新ui
    public void updataUI(Integer res,String order){
        if(res.equals(1)){//发布成功
            textView.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
            //成功发布，将发布order数据保存至sharePreference中，供主页面显示数据
            saveOderSp(order);
        }else{          //发布失败
            textView.setText("发布失败");
            progress.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
        }
    }

    //发布成功后，将当前发布的订单信息（不是订单记录）保存到SharePreference,不用db存储，因为只存储一条信息
    public void saveOderSp(String orderJson){
        SharedPreferences.Editor shaEdit;
        switch (IDENTITY){
                //乘客身份则保存至‘pa_orderinfo’
            case PASSEN_IDENTITY:
                shaEdit = context.getSharedPreferences("pa_orderinfo",Context.MODE_PRIVATE).edit();
                break;
                //司机身份则保存至‘dr_orderinfo’
            case DR_IDENTITY:
                shaEdit = context.getSharedPreferences("dr_orderinfo",Context.MODE_PRIVATE).edit();
                break;
                //-1或者其他，出错
            default:
                return;
        }
//        SharedPreferences.Editor shaEdit = context.getSharedPreferences("pa_orderinfo",Context.MODE_PRIVATE).edit();
        shaEdit.putInt("orderConCode",1);   //发布订单状态，1表示新发布
        shaEdit.putString("orderJson",orderJson);   //发布订单状态，1表示新发布
        shaEdit.apply();
    }

    //打印超长Log
    public static void i(String tag, String msg) {  //信息太长,分段打印
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数
        int max_str_length = 2001 - tag.length();
        //大于4000时
        while (msg.length() > max_str_length) {
            Log.i(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        //剩余部分
        Log.i(tag, msg);
    }



}
