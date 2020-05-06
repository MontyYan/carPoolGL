package com.example.carpoolgl.fragment.passenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.carpoolgl.Static.STATIC_USERINFO;
import com.example.carpoolgl.bean.RelOrder;

public class passengerModel {
    /*
     * 判断用户是否已经登录过
     * */
    public void findUInfo(Context context, passengerView passV){
        SharedPreferences shapre = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        STATIC_USERINFO.setCon(shapre.getInt("conCode",0)); //0：没有用户信息---用户没有登录
        if(STATIC_USERINFO.getCon().equals(1)){                             //1：用户已经登录
            STATIC_USERINFO.setPhone(shapre.getString("phone",""));
            STATIC_USERINFO.setRegisterDate(shapre.getString("registerDate","2000-1-1"));
            STATIC_USERINFO.setUserSeq(shapre.getString("sequence",""));
            Log.i("MainActivity",STATIC_USERINFO.getCon()+"");
            Log.i("MainActivity",STATIC_USERINFO.getPhone());
            Log.i("MainActivity",STATIC_USERINFO.getRegisterDate());
            Log.i("SetUInfo",STATIC_USERINFO.getUserSeq());
            passV.SetUInfo();
        }
    }

    /*
     * 查找用户是否已经发布订单，
     *   如果已经发布订单，则将订单信息显示在主页，且不允许再次发布订单，直到订单完成
     * */
    public void findOrder(Context context,passengerView passV){
        SharedPreferences shapre = context.getSharedPreferences("orderinfo",Context.MODE_PRIVATE);
        Integer result = shapre.getInt("orderConCode",0);
        if(result.equals(1)){
            String orderJson = shapre.getString("orderJson","").replaceAll("&quot;","");
            RelOrder order = JSONObject.parseObject(orderJson,RelOrder.class);//relorder Json格式映射
            passV.SetOrder(order);
        }
    }
}
