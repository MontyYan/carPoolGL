package com.example.carpoolgl.publish;


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

    private static final int PUBLISH_SUS = 1;
    private static final int UPDATA_TV = 0;
    //用于测试 删
    private static final int ORDER_JSON = 9;

    private LinearLayout linearLayout;
    private TextView textView;
    private ProgressBar progress;
    private CardView cardView;
    private RelOrder order;

    public pubHandler(){
    }

    public pubHandler(TextView textView, ProgressBar progress, CardView card){
        this.cardView = card;
        this.textView = textView;
        this.progress = progress;
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
                updataUI(result);
                break;
        }
    }

    public void updataUI(Integer res){
        if(res.equals(1)){
            textView.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
        }else{
            textView.setText("发布失败");
            progress.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
        }
    }

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
